package com.example.Employee_Service.config.jwt.fillter;

import com.example.Employee_Service.config.jwt.en_code.Base64EnCode;
import com.example.Employee_Service.model.dto.CustomUserDetails;
import com.example.Employee_Service.service.jwt.JWTService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.obys.common.constant.Constants;
import com.obys.common.enums.RoleEnum;
import com.obys.common.exception.ErrorV1Exception;
import com.obys.common.model.payload.response.BaseResponse;
import com.obys.common.system_message.SystemMessageCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
       String userName;
       if (codeDecrypt != null) {
         String token = jwtService.getTokenFromRequest(request);
         if (token != null) {
           if (jwtService.validateToken(token)) {
             userName = jwtService.getSubjectFromToken(token);
             if (userName != null) {
               UserDetails userDetails = new CustomUserDetails(userName);
               UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                   userDetails, request.getHeader(Constants.AuthService.AUTHORIZATION), userDetails.getAuthorities());
               authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
               SecurityContextHolder.getContext().setAuthentication(authentication);
               String roles = jwtService.getRole(token);
               LOGGER.info("Author : ->" + roles);
               // check role
               LOGGER.info("URI -----> " + request.getRequestURI());
               jwtService.checkRole(request.getRequestURI(), roles);
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
     }catch (Exception e) {
       BaseResponse<?> errorResponse = new BaseResponse<>();
       String[] messages = e.getMessage().split("<-->");
       errorResponse.setCode(messages[0]);
       errorResponse.setMessage(jwtService.getMessage(messages[1]));

       ResponseEntity<BaseResponse<?>> responseEntity = new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
       response.setContentType(MediaType.APPLICATION_JSON_VALUE);
       response.getWriter().write(new ObjectMapper().writeValueAsString(responseEntity.getBody()));
       return;
     }
    chain.doFilter(request, response);
  }

  protected String messageExceptionFilter(String code, String message) {
    return code + "<-->" + message;
  }
}