package com.example.Employee_Service.model.entity.time_scan_manager;

import com.the.common.model.entity.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "log_time_scan")
@EqualsAndHashCode(callSuper = true)
public class LogTimeScanEntity extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @Column(name = "account")
  private String account;

  @Column(name = "date_work")
  private LocalDate dateWork;

  @Column(name = "status")
  private Boolean status;
}
