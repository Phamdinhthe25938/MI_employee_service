package com.example.Employee_Service;

import com.example.Employee_Service.config.web.ApplicationContextProvider;
import com.example.Employee_Service.model.entity.employee.PartEntity;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import java.util.Arrays;
import java.util.Collections;

@SpringBootTest
class EmployeeServiceApplicationTests {
  private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeServiceApplicationTests.class);
  @Test
  void contextLoads() {
  }

  @Test
  void testReverseString() {
    String str = "pham dinh the";
    String[] words = str.split(" ");
    int count = 0;
    String strReverse = words[words.length - 1];
    while (count < words.length - 1) {
      strReverse += " ";
      strReverse += words[count];
      count++;
    }

    String str1 = strReverse.replaceFirst(" ", ".").replaceAll(" ", "");
    System.out.println(str1);

    String hello = "         the           pham            ";

    String me = hello.strip();
    System.out.println(me);
  }

  /**
   * Scope Singleton :  with scope này IOC container sẽ khởi tạo 1 bean duy nhất cho mọi yêu cầu đều sử dụng bean này
   */
  @Test
  void scopeBeanSingleton () {
    ApplicationContext application = ApplicationContextProvider.getApplicationContext();

    PartEntity partEntity1 = (PartEntity) application.getBean("PartEntity");
    partEntity1.setName("the");
    PartEntity partEntity2 = (PartEntity) application.getBean("PartEntity");
    LOGGER.info("scopeBeanSingleton  log " + partEntity2.getName());
  }

  /**
   * Scope Prototype : with scope này IOC sẽ khởi tạo các instance khác nhau mỗi khi có 1 yeu cau mới
   */

  @Test
  void scopeBeanPrototype(){
    ApplicationContext application = ApplicationContextProvider.getApplicationContext();
    PartEntity partEntity1 = (PartEntity) application.getBean("PartEntity");
    partEntity1.setName("the");
    PartEntity partEntity2 = (PartEntity) application.getBean("PartEntity");
    LOGGER.info(" scopeBeanPrototype  log " + partEntity2.getName());
  }
}
