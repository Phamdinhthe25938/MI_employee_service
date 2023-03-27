package com.example.Employee_Service.service.time_scan_manager;


import com.example.Employee_Service.model.dto.CustomUserDetails;
import com.example.Employee_Service.model.dto.request.time_scan_manager.AddTimeScanRequest;
import com.example.Employee_Service.repository.employee.EmployeeRepository;
import com.example.Employee_Service.repository.time_scan_manager.TimeScanRepository;
import com.obys.common.service.BaseService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("TimeScanService")
public class TimeScanService extends BaseService {

    @Resource
    @Qualifier("TimeScanRepository")
    private TimeScanRepository timeScanRepository;
    @Resource
    @Qualifier("EmployeeRepository")
    private EmployeeRepository employeeRepository;


    public void save(AddTimeScanRequest request) {
        CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication();
        String uuid = getUUID();
        String account = customUserDetails.getUsername();

    }
}
