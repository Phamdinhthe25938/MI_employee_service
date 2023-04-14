package com.example.Employee_Service.controller.role_manager_personal;

import com.example.Employee_Service.model.dto.request.employee.AddContractDetailRequest;
import com.example.Employee_Service.model.dto.request.employee.AddEmployeeRequest;
import com.example.Employee_Service.service.employee.ContractDetailService;
import com.example.Employee_Service.service.employee.EmployeeService;
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
@RequestMapping("/api/manager-personal")
public class ManagerPersonalController {

  @Resource
  private EmployeeService employeeService;

  @Resource
  @Qualifier("ContractDetailService")
  private ContractDetailService contractDetailService;

  @PostMapping("/employee/save")
  public ResponseEntity<?> save(@Valid @RequestBody AddEmployeeRequest request, BindingResult result, HttpServletRequest httpServlet) {
    return new ResponseEntity<>(employeeService.save(request, result, httpServlet), HttpStatus.OK);
  }

  @PostMapping("/contact-detail/save")
  public ResponseEntity<?> save(@Valid @RequestBody AddContractDetailRequest request, BindingResult result) {
    return new ResponseEntity<>(contractDetailService.save(request, result), HttpStatus.OK);
  }
}
