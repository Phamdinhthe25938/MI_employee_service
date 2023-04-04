package com.example.Employee_Service.model.entity.time_scan_manager;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "log_time_scan")
public class LogTimeScan {
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
