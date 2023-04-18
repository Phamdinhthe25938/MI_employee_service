package com.example.Employee_Service.controller.role_all;

import com.example.Employee_Service.model.dto.request.employee.AddLogVacationDayRequest;
import com.example.Employee_Service.model.dto.request.employee.UpdateLogVacationDayRequest;
import com.example.Employee_Service.model.dto.request.time_scan_manager.AddTimeScanRequest;
import com.example.Employee_Service.model.dto.request.time_scan_manager.GetListTimeScanDetailRequest;
import com.example.Employee_Service.service.employee.LogVacationDayService;
import com.example.Employee_Service.service.time_scan_manager.TimeScanDetailService;
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

  @Resource
  @Qualifier("TimeScanDetailService")
  private TimeScanDetailService timeScanDetailService;
  @Resource
  @Qualifier("LogVacationDayService")
  private LogVacationDayService logVacationDayService;

  @GetMapping("/time-scan/getAll")
  public ResponseEntity<?> getAll() {
    return new ResponseEntity<>(timeScanService.search(), HttpStatus.OK);
  }

  @PostMapping("/time-scan/save")
  public ResponseEntity<?> save(@Valid @RequestBody AddTimeScanRequest request, BindingResult result, HttpServletRequest servletRequest) {
    return new ResponseEntity<>(timeScanService.save(request, result, servletRequest), HttpStatus.OK);
  }

  @GetMapping("/time-scan-detail/getAll")
  public ResponseEntity<?> getAllTimeScanDetail(@RequestBody GetListTimeScanDetailRequest request) {
    return new ResponseEntity<>(timeScanDetailService.search(request), HttpStatus.OK);
  }

  @PostMapping("/log-vacation/save")
  public ResponseEntity<?> saveLogVacation(@Valid @RequestBody AddLogVacationDayRequest request) {
    return new ResponseEntity<>(logVacationDayService.save(request), HttpStatus.OK);
  }
  @PutMapping("/log-vacation/update")
  public ResponseEntity<?> updateLogVacation(@Valid @RequestBody UpdateLogVacationDayRequest request) {
    return new ResponseEntity<>(logVacationDayService.update(request), HttpStatus.OK);
  }
}
