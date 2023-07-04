package com.example.Employee_Service.model.entity.employee;

import com.the.common.model.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "log_vacation_day")
public class LogVacationDayEntity extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;
  @NotNull
  @Column(name = "id_vacation_day")
  private Long idVacationDay;
  @NotNull
  @Column(name = "id_employee")
  private Long idEmployee;
  @NotNull
  @Column(name = "account_employee")
  private String accountEmployee;
  @NotNull
  @Column(name = "uuid_employee")
  private String uuidEmployee;
  @NotNull
  @Column(name = "id_assign")
  private Long idAssign;
  @NotNull
  @Column(name = "account_assign")
  private String accountAssign;
  @NotNull
  @Column(name = "date_log_vacation")
  private LocalDate dateLogVacation;
  @NotNull
  @Column(name = "reason_rest")
  private String reasonRest;
  @NotNull
  @Column(name = "type_day_log")
  private Long typeDayLog;
  @NotNull
  @Column(name = "time_log_start")
  private LocalDateTime timeLogStart;
  @NotNull
  @Column(name = "time_log_End")
  private LocalDateTime timeLogEnd;
  @NotNull
  @Column(name = "status_approve")
  private Boolean statusApprove;
}
