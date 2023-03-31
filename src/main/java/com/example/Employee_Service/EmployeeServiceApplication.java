package com.example.Employee_Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.TimeZone;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
@EnableScheduling
public class EmployeeServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(EmployeeServiceApplication.class, args);

    LocalDate currentDate = LocalDate.now();
    LocalDate yesterday = currentDate.minusDays(1);
    int day = yesterday.getDayOfMonth();
    int month = yesterday.getMonthValue();
    int year = yesterday.getYear();
    System.out.println(day + "-----" + month+ "----" + year);
  }
  @PostConstruct
  public void init(){
    TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
  }
  private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeServiceApplication.class);
}
