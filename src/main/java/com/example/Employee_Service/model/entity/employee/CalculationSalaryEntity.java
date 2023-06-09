package com.example.Employee_Service.model.entity.employee;

import com.the.common.model.entity.BaseEntity;
import lombok.*;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.YearMonth;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "calculation_salary")
@TypeDef(name = "yearMonth", typeClass = YearMonth.class)
public class CalculationSalaryEntity extends BaseEntity {
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
  @Column(name = "month_work")
  @Type(type = "yearMonth")
  private YearMonth monthWork;
  @NotNull
  @Column(name = "total_days_late")
  private Long totalDaysLate = 0L;
  @NotNull
  @Column(name = "total_days_back_soon")
  private Long totalDaysBackSoon = 0L;
  @NotNull
  @Column(name = "total_days_rest")
  private Long totalDaysRest;
  @NotNull
  @Column(name = "total_days_enough")
  private Long totalDaysEnough;
  @NotNull
  @Column(name = "work_days")
  private Double workDays;
  @NotNull
  @Column(name = "total_days_off_permission")
  private Long totalDaysOffPermission;
  @NotNull
  @Column(name = "total_days_off_not_permission")
  private Long totalDaysOffNotPermission;
  @NotNull
  @Column(name = "total_days_of_month")
  private Long totalDaysOfMonth;
  @NotNull
  @Column(name = "total_salary_of_month")
  private Double totalSalaryOfMonth;
}
