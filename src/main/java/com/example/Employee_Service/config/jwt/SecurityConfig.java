package com.example.Employee_Service.config.jwt;

import com.example.Employee_Service.config.jwt.fillter.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import javax.annotation.Resource;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
//    @Resource
//    AppUserService appUserService;

  @Resource
  JwtAuthenticationFilter jwtAuthenticationFilter;

  @Bean(BeanIds.AUTHENTICATION_MANAGER)
  @Override
  public AuthenticationManager authenticationManager() throws Exception {
    return super.authenticationManager();
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
//    System.out.println("Http : ---> " + http.toString());
    http.authorizeRequests().antMatchers("/**").permitAll()
//                .and().authorizeRequests().antMatchers("/hello").hasRole("USER")
        .and().authorizeRequests().anyRequest().authenticated()
        .and().csrf().disable();
    http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
        .exceptionHandling();
    http.cors().configurationSource(request -> new CorsConfiguration().applyPermitDefaultValues());
  }

//     xắc thực
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(appUserService).passwordEncoder(NoOpPasswordEncoder.getInstance());
//    }

}
