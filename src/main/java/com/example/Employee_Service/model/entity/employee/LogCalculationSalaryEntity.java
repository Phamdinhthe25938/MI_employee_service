package com.example.Employee_Service.model.entity.employee;

import com.obys.common.model.entity.BaseEntity;
import com.obys.common.service.BaseService;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "log_calculation_salary")
public class LogCalculationSalaryEntity extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;
  @Column(name = "account")
  private String account;

  @Column(name = "month_work")
  private LocalDate month_work;

  @Column(name = "status")
  private Boolean status;
}
