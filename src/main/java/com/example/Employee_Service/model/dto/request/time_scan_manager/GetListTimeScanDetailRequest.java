package com.example.Employee_Service.model.dto.request.time_scan_manager;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetListTimeScanDetailRequest {
  private Long idEmployee;
}
