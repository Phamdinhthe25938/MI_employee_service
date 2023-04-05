package com.example.Employee_Service.controller.employee;

import com.example.Employee_Service.model.dto.request.employee.AddContractDetailRequest;
import com.example.Employee_Service.service.employee.ContractDetailService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
@CrossOrigin
@RequestMapping("/api/employee/contract-detail")
public class ContractDetailController {

  @Resource
  @Qualifier("ContractDetailService")
  private ContractDetailService contractDetailService;

  @PostMapping("/save")
  public ResponseEntity<?> save(@Valid @RequestBody AddContractDetailRequest request, BindingResult result) {
    return new ResponseEntity<>(contractDetailService.save(request, result), HttpStatus.OK);
  }
}
