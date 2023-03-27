package com.example.Employee_Service.controller;

import com.example.Employee_Service.model.dto.request.time_scan_manager.AddTimeScanRequest;
import com.example.Employee_Service.service.time_scan_manager.TimeScanService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Qualifier;


@RestController
@CrossOrigin
@RequestMapping("/api/time-scan")
public class TimeScanController {

    @Resource
    @Qualifier("TimeScanService")
    TimeScanService timeScanService;

    @PostMapping("/save")
    public ResponseEntity<?> save (@Valid @RequestBody AddTimeScanRequest request) {
        return new ResponseEntity<>(timeScanService.save(request), HttpStatus.OK);
    }
}
