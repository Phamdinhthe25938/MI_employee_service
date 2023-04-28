package com.example.Employee_Service.model.entity.employee;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.the.common.model.commom.ObjectNodeConverter;
import com.the.common.model.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "contract_detail")
public class ContractDetailEntity extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;
  @NotNull
  @Column(name = "id_employee")
  private Long idEmployee;
  @NotNull
  @Column(name = "account_employee")
  private String accountEmployee;
  @NotNull
  @Column(name = "salary_basic")
  private Long salaryBasic;
  @Column(name = "salary_subsidize", columnDefinition = "json", nullable = false)
  @Convert(converter = ObjectNodeConverter.class)
  private ObjectNode salarySubsidize;
  @Column(name = "salary_total")
  private Long salaryTotal;
  @Column(name = "work_location")
  private String workLocation;
  @Column(name = "job_description")
  private String jobDescription;
  @Column(name = "date_start_contract")
  private LocalDate dateStartContract;
  @Column(name = "date_end_contract")
  private LocalDate dateEndContract;

}



