package com.example.Employee_Service.model.dto.communicate_kafka.employee;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegistryEmployeeProducer {

  private String account;

  private String email;

  private String telephone;
}
