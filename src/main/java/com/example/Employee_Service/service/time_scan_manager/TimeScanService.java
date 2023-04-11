package com.example.Employee_Service.service.time_scan_manager;


import com.example.Employee_Service.model.dto.request.time_scan_manager.AddTimeScanRequest;
import com.example.Employee_Service.model.entity.employee.EmployeeEntity;
import com.example.Employee_Service.model.entity.time_scan_manager.TimeScanEntity;
import com.example.Employee_Service.repository.employee.EmployeeRepository;
import com.example.Employee_Service.repository.time_scan_manager.TimeScanRepository;
import com.example.Employee_Service.service.jwt.JWTService;
import com.example.Employee_Service.validate.employee.EmployeeValidator;
import com.obys.common.model.payload.response.BaseResponse;
import com.obys.common.service.BaseService;
import com.obys.common.system_message.SystemMessageCode;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Service("TimeScanService")
public class TimeScanService extends BaseService {

  @Resource
  @Qualifier("TimeScanRepository")
  private TimeScanRepository timeScanRepository;
  @Resource
  @Qualifier("EmployeeValidator")
  private EmployeeValidator employeeValidator;
  @Resource
  @Qualifier("JWTService")
  private JWTService jwtService;

  @Resource
  @Qualifier("EmployeeRepository")
  private EmployeeRepository employeeRepository;


  public BaseResponse<?> save(AddTimeScanRequest request, BindingResult result, HttpServletRequest servletRequest) {
    hasError(result);
    String uuid = getUUID(servletRequest);
    EmployeeEntity employee = employeeValidator.accountEmployeeExist(request.getAccount());
    employeeValidator.uuidIsValid(uuid, employee.getUuid());
    employeeValidator.accountIsValid(request.getAccount(), jwtService.getSubjectFromToken(jwtService.getTokenFromRequest(servletRequest)));
    TimeScanEntity timeScanEntity = TimeScanEntity.builder()
        .codeEmployee(employee.getCode())
        .accountEmployee(employee.getAccount())
        .uuid(uuid)
        .timeScan(request.getTimeScan())
        .typeScan(request.getTypeScan())
        .dayOfWeek(request.getTimeScan().getDayOfWeek().getValue())
        .dateScan(request.getTimeScan().getDayOfMonth())
        .monthScan(request.getTimeScan().getMonth().getValue())
        .yearScan(request.getTimeScan().getYear())
        .build();
    TimeScanEntity timeScan = timeScanRepository.save(timeScanEntity);
    return responseV1(
        SystemMessageCode.CommonMessage.CODE_SUCCESS,
        SystemMessageCode.CommonMessage.SAVE_SUCCESS,
        timeScan
    );
  }
}
