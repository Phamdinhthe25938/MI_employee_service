package com.example.Employee_Service.config.jwt.fillter;

import com.example.Employee_Service.service.jwt.JWTService;
import com.obys.common.exception.ErrorV1Exception;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = jwtService.getTokenFromRequest(request);
            if (token != null) {
                if (jwtService.validateToken(token)) {
                    filterChain.doFilter(request, response);
                } else {
//                    throw new ErrorV1Exception()
                }
            }
        } catch (Exception e) {
            LOGGER.error("Can NOT set user authentication -> Message: {}" + e.getMessage());
        }
    }

}