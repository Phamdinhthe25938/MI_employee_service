package com.example.Employee_Service.model.dto.response.time_scan_manager;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetTimeScanDetailResponse {
  private Long idEmployee;
  private String accountEmployee;
  private LocalDate dateWork;
  private LocalDateTime timeScanInStart;
  private LocalDateTime timeScanOutEnd;
  private Long timeOffice;
  private Long timeReality;
  private Integer statusScanWorkDay;
  private Integer statusWorkdays;
  private Double numberWorkday;
  private Integer dayOfWeek;
}
