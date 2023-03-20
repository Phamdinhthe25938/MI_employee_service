package com.example.Employee_Service.controller;

import com.example.Employee_Service.model.dto.request.employee.AddEmployeeRequest;
import com.example.Employee_Service.service.employee.EmployeeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
@CrossOrigin
@RequestMapping("/api/employee")
public class EmployeeController {

    @Resource
    private EmployeeService employeeService;

    @PostMapping("/save")
    public ResponseEntity<?> save (@Valid @RequestBody AddEmployeeRequest request, BindingResult result) {
        employeeService.save(request, result);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
