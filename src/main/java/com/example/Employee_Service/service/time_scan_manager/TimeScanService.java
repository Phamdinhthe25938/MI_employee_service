package com.example.Employee_Service.service.time_scan_manager;


import com.example.Employee_Service.model.dto.CustomUserDetails;
import com.example.Employee_Service.model.dto.request.time_scan_manager.AddTimeScanRequest;
import com.example.Employee_Service.model.entity.employee.Employee;
import com.example.Employee_Service.model.entity.time_scan_manager.TimeScan;
import com.example.Employee_Service.repository.employee.EmployeeRepository;
import com.example.Employee_Service.repository.time_scan_manager.TimeScanRepository;
import com.example.Employee_Service.validate.employee.EmployeeValidator;
import com.obys.common.model.payload.response.BaseResponse;
import com.obys.common.service.BaseService;
import com.obys.common.system_message.SystemMessageCode;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import javax.annotation.Resource;
import java.util.Calendar;

@Service("TimeScanService")
public class TimeScanService extends BaseService {

  @Resource
  @Qualifier("TimeScanRepository")
  private TimeScanRepository timeScanRepository;

  @Resource
  @Qualifier("EmployeeRepository")
  private EmployeeRepository employeeRepository;

  @Resource
  @Qualifier("EmployeeValidator")
  private EmployeeValidator employeeValidator;


  public BaseResponse<?> save(AddTimeScanRequest request, BindingResult result) {
    hasError(result);
    String uuid = getUUID();
    Employee employee = employeeValidator.accountEmployeeExist(request.getAccount());
    employeeValidator.uuidIsValid(uuid, employee.getUuid());
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(request.getTimeScan());
    TimeScan timeScanEntity = TimeScan.builder()
        .codeEmployee(employee.getCode())
        .accountEmployee(employee.getAccount())
        .uuid(uuid)
        .timeScan(request.getTimeScan())
        .typeScan(request.getTypeScan())
        .dayOfWeek(calendar.get(Calendar.DAY_OF_WEEK))
        .dateScan(calendar.get(Calendar.DAY_OF_MONTH))
        .monthScan(calendar.get(Calendar.MONTH) + 1)
        .yearScan(calendar.get(Calendar.YEAR))
        .build();
    TimeScan timeScan = timeScanRepository.save(timeScanEntity);
    return responseV1(
        SystemMessageCode.CommonMessage.CODE_SUCCESS,
        SystemMessageCode.CommonMessage.SAVE_SUCCESS,
        timeScan
    );
  }
}
