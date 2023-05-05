package com.example.Employee_Service.controller.role_all;

import com.example.Employee_Service.model.dto.request.employee.*;
import com.example.Employee_Service.model.dto.request.time_scan_manager.AddTimeScanRequest;
import com.example.Employee_Service.model.dto.request.time_scan_manager.GetListTimeScanDetailRequest;
import com.example.Employee_Service.service.employee.LogVacationDayService;
import com.example.Employee_Service.service.employee.NotificationService;
import com.example.Employee_Service.service.time_scan_manager.TimeScanDetailService;
import com.example.Employee_Service.service.time_scan_manager.TimeScanService;
import com.the.common.constant.Constants;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.socket.WebSocketSession;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@CrossOrigin(origins = {"*", "http://localhost:1010"})
@RequestMapping("/api/all")
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
  @Resource
  @Qualifier("NotificationService")
  private NotificationService notificationService;

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
  @DeleteMapping("/log-vacation/delete")
  private ResponseEntity<?> deleteLogVacation(@Valid @RequestBody DeleteLogVacationRequest request, HttpServletRequest httpServletRequest) {
    return new ResponseEntity<>(logVacationDayService.delete(request, httpServletRequest), HttpStatus.OK);
  }
  @GetMapping("/log-vacation/get-all-by-send")
  private ResponseEntity<?> getByPersonSend(@RequestBody GetListLogVacationByPersonSend request, HttpServletRequest httpServletRequest) {
    return new ResponseEntity<>(logVacationDayService.getByPersonSend(request, httpServletRequest), HttpStatus.OK);
  }
  @GetMapping("/log-vacation/get-all-by-assign")
  private ResponseEntity<?> getByAssign(@RequestBody GetListLogVacationByAssignRequest request, HttpServletRequest httpServletRequest) {
    return new ResponseEntity<>(logVacationDayService.getByAssign(request, httpServletRequest), HttpStatus.OK);
  }
  @GetMapping("/log-vacation/update-status-approve")
  private ResponseEntity<?> updateStatusApprove(@Valid @RequestBody ApproveLogVacationRequest request) {
    return new ResponseEntity<>(logVacationDayService.approveLog(request), HttpStatus.OK);
  }

  /**
   * Notification real time
   */
  @MessageMapping("/test")
  public ResponseEntity<?> sendNotification(@Payload AddNotificationRequest request, WebSocketSession session) {
    String authorization = session.getHandshakeHeaders().getFirst(Constants.AuthService.AUTHORIZATION);
    notificationService.save(request, authorization);
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
