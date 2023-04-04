package com.example.Employee_Service.controller.employee;

import com.example.Employee_Service.model.dto.request.time_scan_manager.AddTimeScanRequest;
import com.example.Employee_Service.service.time_scan_manager.TimeScanService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Qualifier;


@RestController
@CrossOrigin
@RequestMapping("/api/employee/time-scan")
public class TimeScanController {

  @Resource
  @Qualifier("TimeScanService")
  TimeScanService timeScanService;

  @PostMapping("/save")
  public ResponseEntity<?> save(@Valid @RequestBody AddTimeScanRequest request, BindingResult result, HttpServletRequest servletRequest) {
    return new ResponseEntity<>(timeScanService.save(request, result, servletRequest), HttpStatus.OK);
  }
}
