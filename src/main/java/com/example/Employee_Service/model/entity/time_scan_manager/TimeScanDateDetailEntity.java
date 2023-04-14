package com.example.Employee_Service.model.entity.time_scan_manager;

import com.obys.common.model.entity.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "time_scan_date_detail")
public class TimeScanDateDetailEntity extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;
  @Column(name = "account_employee")
  private String accountEmployee;
  @Column(name = "code_employee")
  private String codeEmployee;
  @Column(name = "uuid")
  private String uuid;
  @Column(name = "date_work")
  private LocalDate dateWork;
  @Column(name = "time_scan_in_start")
  private LocalDateTime timeScanInStart;
  @Column(name = "time_scan_out_end")
  private LocalDateTime timeScanOutEnd;
  @Column(name = "time_office")
  private Long timeOffice;
  @Column(name = "time_reality")
  private Long timeReality;
  @Column(name = "status_scan_work_day")
  private Integer statusScanWorkDay;
  @Column(name = "status_workdays")
  private Integer statusWorkdays;
  @Column(name = "number_workday")
  private Double numberWorkday;
  @Column(name= "days_of_week")
  private Integer dayOfWeek;
}
