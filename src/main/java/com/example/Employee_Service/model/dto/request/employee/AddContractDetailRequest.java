package com.example.Employee_Service.model.dto.request.employee;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.the.common.validator.annotation.Required;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddContractDetailRequest {

  @Required(message = "{is.required}")
  private Long idEmployee;
  @Required(message = "{is.required}")
  private Long salaryBasic;
  private ObjectNode salarySubsidize;
  @Column(name = "work_location")
  private String workLocation;
  @Required(message = "{is.required}")
  private String jobDescription;
  @Required(message = "{is.required}")
  private LocalDate dateStartContract;
  private LocalDate dateEndContract;
}
