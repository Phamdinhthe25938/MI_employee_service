package com.example.Employee_Service.controller.role_all;

import com.example.Employee_Service.model.dto.request.time_scan_manager.AddTimeScanRequest;
import com.example.Employee_Service.service.time_scan_manager.TimeScanService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@CrossOrigin
@RequestMapping("/api/all/")
public class AllAccessController {
  @Resource
  @Qualifier("TimeScanService")
  TimeScanService timeScanService;

  @GetMapping("/time-scan/getAll")
  public ResponseEntity<?> getAll() {
    return new ResponseEntity<>(timeScanService.search(), HttpStatus.OK);
  }

  @PostMapping("/time-scan/save")
  public ResponseEntity<?> save(@Valid @RequestBody AddTimeScanRequest request, BindingResult result, HttpServletRequest servletRequest) {
    return new ResponseEntity<>(timeScanService.save(request, result, servletRequest), HttpStatus.OK);
  }
}
