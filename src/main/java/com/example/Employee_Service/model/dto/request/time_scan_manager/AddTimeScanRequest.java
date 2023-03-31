package com.example.Employee_Service.model.dto.request.time_scan_manager;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.obys.common.validator.annotation.Required;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddTimeScanRequest {

  @Required(message = "is.required")
  private Date timeScan;

  @Required(message = "is.required")
  private Integer typeScan;

}
