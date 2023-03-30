package com.example.Employee_Service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.Collections;

@SpringBootTest
class EmployeeServiceApplicationTests {

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
  }

}
