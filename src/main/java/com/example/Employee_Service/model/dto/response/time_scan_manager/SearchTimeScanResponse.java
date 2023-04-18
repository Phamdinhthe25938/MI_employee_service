package com.example.Employee_Service.model.dto.response.time_scan_manager;

import com.example.Employee_Service.model.entity.time_scan_manager.TimeScanEntity;
import com.obys.common.model.payload.response.MetaList;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchTimeScanResponse {

  private List<TimeScanEntity> item;

  private MetaList meta;
}
