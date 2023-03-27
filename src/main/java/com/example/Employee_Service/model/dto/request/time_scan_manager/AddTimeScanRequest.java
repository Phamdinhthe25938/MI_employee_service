package com.example.Employee_Service.model.dto.request.time_scan_manager;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddTimeScanRequest {

    @JsonFormat(pattern = "dd-mm-yyyy")
    private Date timeScan;

    @Column(name = "type_scan")
    private Integer typeScan;

    @Column(name = "date_scan")
    private Integer dateScan;

    @Column(name = "month_scan")
    private Integer monthScan;

    @Column(name = "year_scan")
    private Integer yearScan;
}
