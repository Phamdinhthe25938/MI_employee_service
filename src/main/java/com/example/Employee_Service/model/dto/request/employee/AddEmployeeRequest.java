package com.example.Employee_Service.model.dto.request.employee;

import com.the.common.validator.annotation.Gmail;
import com.the.common.validator.annotation.Phone;
import com.the.common.validator.annotation.Required;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AddEmployeeRequest {

  @Required(message = "{is.required}")
  private String fullName;

  @Required(message = "{is.required}")
  private String imageName;

  @Required(message = "{is.required}")
  @Phone(message = "{phone.invalid}")
  private String telephone;

  @Required(message = "{is.required}")
  @Gmail(message = "{gmail.invalid}")
  private String emailPersonal;

  @Required(message = "{is.required}")
  private String numberCCCD;

  @Required(message = "{is.required}")
  private LocalDate birthDay;

  @Required(message = "{is.required}")
  private String address;

  @Required(message = "{is.required}")
  private Long positionId;

  @Required(message = "{is.required}")
  private Long partId;

  @Required(message = "{is.required}")
  private Long contactId;
}
