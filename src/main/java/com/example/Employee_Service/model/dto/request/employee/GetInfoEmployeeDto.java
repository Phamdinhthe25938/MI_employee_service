package com.example.Employee_Service.model.dto.request.employee;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetInfoEmployeeDto {

  private String code;

  private String account;

  private Long salaryTotal;
}
