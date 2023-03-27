package com.example.Employee_Service.config.jwt.fillter;

import com.example.Employee_Service.config.jwt.en_code.Base64EnCode;
import com.example.Employee_Service.model.dto.CustomUserDetails;
import com.example.Employee_Service.service.jwt.JWTService;
import com.obys.common.constant.Constants;
import com.obys.common.exception.ErrorV1Exception;
import com.obys.common.system_message.SystemMessageCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final static Logger LOGGER = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    @Resource
    private JWTService jwtService;
    @Resource
    private Base64EnCode base64EnCode;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        try {
            String base64 = request.getHeader("En_code");
            String codeDecrypt = base64EnCode.decrypt(base64);
            String userName = null;
            if (codeDecrypt != null) {
                String token = jwtService.getTokenFromRequest(request);
                if (token != null) {
                    if (jwtService.validateToken(token)) {
                        userName = jwtService.getSubjectFromToken(token);
                        if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                                UserDetails userDetails = new CustomUserDetails(userName);
                                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                                        userDetails, request.getHeader(Constants.AuthService.AUTHORIZATION), userDetails.getAuthorities());
                                authentication
                                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                                SecurityContextHolder.getContext()
                                        .setAuthentication(authentication);
                            }
                    } else {
                        LOGGER.error("[JwtAuthenticationFilter][doFilterInternal] Token is invalid !");
                        throw new ErrorV1Exception(messageExceptionFilter(
                                SystemMessageCode.AuthService.CODE_TOKEN_FAIL,
                                SystemMessageCode.AuthService.MESSAGE_TOKEN_FAIL
                        ));
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error("Can NOT set user authentication -> Message: {}" + e.getMessage());
        }
        chain.doFilter(request, response);
    }

    protected String messageExceptionFilter(String code, String message) {
        return code + "<-->" + message;
    }

}