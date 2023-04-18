package com.example.Employee_Service.model.entity.employee;

import com.obys.common.model.entity.BaseEntity;
import com.vladmihalcea.hibernate.type.basic.YearMonthTimestampType;
import lombok.*;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.YearMonth;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "log_calculation_salary")
@TypeDef(name = "yearMonth", typeClass = YearMonthTimestampType.class)
public class LogCalculationSalaryEntity extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;
  @Column(name = "account")
  private String account;

  @Column(name = "month_work")
  @Type(type = "yearMonth")
  private YearMonth monthWork;

  @Column(name = "status")
  private Boolean status;
}
