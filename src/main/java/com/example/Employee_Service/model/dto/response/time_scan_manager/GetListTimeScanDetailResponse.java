package com.example.Employee_Service.model.dto.response.time_scan_manager;

import com.obys.common.model.payload.response.MetaList;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.YearMonth;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetListTimeScanDetailResponse {
  private YearMonth time;
  private List<GetTimeScanDetailResponse> item;
}
