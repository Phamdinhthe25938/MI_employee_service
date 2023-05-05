package com.example.Employee_Service.model.dto.request.employee;

import com.the.common.validator.annotation.Required;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeleteLogVacationRequest {
  @Required(message = "{is.required}")
  private Long id;
}
