package com.example.Employee_Service.model.entity.time_scan_manager;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.the.common.model.entity.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "time_scan")
public class TimeScanEntity extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;
  @Column(name = "id_employee")
  private Long idEmployee;
  @Column(name = "account_employee")
  private String accountEmployee;
  @Column(name = "time_scan")
  @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
  private LocalDateTime timeScan;
  @Column(name = "type_scan")
  private Integer typeScan;

  @Column(name= "days_of_week")
  private Integer dayOfWeek;

  @Column(name = "date_scan")
  private Integer dateScan;

  @Column(name = "month_scan")
  private Integer monthScan;

  @Column(name = "year_scan")
  private Integer yearScan;

}
