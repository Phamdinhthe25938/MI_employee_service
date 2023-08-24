package com.example.Employee_Service.config.web;


import org.checkerframework.checker.units.qual.C;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class ApplicationContextProvider implements ApplicationContextAware {

  public static ApplicationContext applicationContext;


  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    ApplicationContextProvider.applicationContext = applicationContext;
  }

  public static ApplicationContext getApplicationContext() {
    return applicationContext;
  }
}
