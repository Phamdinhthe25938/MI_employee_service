package com.example.Employee_Service.config.jwt.fillter;

import com.example.Employee_Service.service.jwt.JWTService;
import com.the.common.constant.Constants;
import com.the.common.en_code.Base64Code;
import com.the.common.model.CustomUserDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Nonnull;
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
  private Base64Code base64EnCode;

  @Override
  protected void doFilterInternal(@Nonnull HttpServletRequest request, @Nonnull HttpServletResponse response, @Nonnull FilterChain chain) throws ServletException, IOException {
    LOGGER.info("Request go employee-service filter !");
    String codeDecrypt = base64EnCode.decrypt(request.getHeader(Base64Code.KEY), request.getHeader(Base64Code.BASE64_CODE));
    String userName;
    if (codeDecrypt != null) {
      String token = jwtService.getTokenFromRequest(request);
      if (token != null) {
        userName = jwtService.getSubjectFromToken(token);
        UserDetails userDetails = new CustomUserDetails(userName);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
            userDetails, request.getHeader(Constants.AuthService.AUTHORIZATION), userDetails.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        LOGGER.info("Finish employee-service filter !");
      }
    }
    chain.doFilter(request, response);
  }

//  @Override
//  protected void doFilterInternal(@Nonnull HttpServletRequest request,@Nonnull HttpServletResponse response,@Nonnull FilterChain chain) throws ServletException, IOException {
//     try {
//       String base64 = request.getHeader("En_code");
//       String codeDecrypt = base64EnCode.decrypt(base64);
//       String userName;
//       if (codeDecrypt != null) {
//         String token = jwtService.getTokenFromRequest(request);
//         if (token != null) {
//           if (jwtService.validateToken(token)) {
//             userName = jwtService.getSubjectFromToken(token);
//             if (userName != null) {
//               UserDetails userDetails = new CustomUserDetails(userName);
//               UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
//                   userDetails, request.getHeader(Constants.AuthService.AUTHORIZATION), userDetails.getAuthorities());
//               authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//               SecurityContextHolder.getContext().setAuthentication(authentication);
//               String roles = jwtService.getRole(token);
//               LOGGER.info("Author : ->" + roles);
//               // check role
//               LOGGER.info("URI -----> " + request.getRequestURI());
//               jwtService.checkRole(request.getRequestURI(), roles);
//             }
//             response.setHeader("Access-Control-Allow-Origin", "*");
//             response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
//             response.setHeader("Access-Control-Max-Age", "3600");
//             response.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type, Accept, X-Requested-With, remember-me");
//           } else {
//             LOGGER.error("[JwtAuthenticationFilter][doFilterInternal] Token is invalid !");
//             throw new ErrorV1Exception(messageExceptionFilter(
//                 SystemMessageCode.AuthService.CODE_TOKEN_FAIL,
//                 SystemMessageCode.AuthService.MESSAGE_TOKEN_FAIL
//             ));
//           }
//         }
//       }
//     }catch (Exception e) {
//       BaseResponse<?> errorResponse = new BaseResponse<>();
//       String[] messages = e.getMessage().split("<-->");
//       errorResponse.setCode(messages[0]);
//       errorResponse.setMessage(jwtService.getMessage(messages[1]));
//       LOGGER.error("[JwtAuthenticationFilter][doFilterInternal] Token is exception ---> !");
//       ResponseEntity<BaseResponse<?>> responseEntity = new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
//       response.setContentType(MediaType.APPLICATION_JSON_VALUE);
//       response.getWriter().write(new ObjectMapper().writeValueAsString(responseEntity.getBody()));
//       return;
//     }
//    chain.doFilter(request, response);
//  }

  protected String messageExceptionFilter(String code, String message) {
    return code + "<-->" + message;
  }

}