package com.example.Employee_Service.service.employee;

import com.example.Employee_Service.enums.TypeLogVacationDayEnum;
import com.example.Employee_Service.model.dto.request.employee.UpdateLogVacationDayRequest;
import com.example.Employee_Service.repository.employee.NotificationRepository;
import com.obys.common.exception.ErrorV2Exception;
import com.obys.common.model.CustomUserDetails;
import com.example.Employee_Service.model.dto.request.employee.AddLogVacationDayRequest;
import com.example.Employee_Service.model.entity.employee.EmployeeEntity;
import com.example.Employee_Service.model.entity.employee.LogVacationDayEntity;
import com.example.Employee_Service.repository.employee.LogVacationDayRepository;
import com.example.Employee_Service.validate.employee.EmployeeValidator;
import com.obys.common.constant.Constants;
import com.obys.common.enums.PositionEnum;
import com.obys.common.exception.ErrorV1Exception;
import com.obys.common.model.payload.response.BaseResponse;
import com.obys.common.service.BaseService;
import com.obys.common.system_message.SystemMessageCode;
import org.apache.commons.lang3.ObjectUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service("LogVacationDayService")
public class LogVacationDayService extends BaseService {

  @Resource
  private SimpMessagingTemplate simpMessagingTemplate;
  @Resource
  @Qualifier("LogVacationDayRepository")
  private LogVacationDayRepository logVacationDayRepository;
  @Resource
  @Qualifier("ModelMapper")
  private ModelMapper modelMapper;
  @Resource
  @Qualifier("EmployeeValidator")
  private EmployeeValidator employeeValidator;
  @Resource
  @Qualifier("NotificationRepository")
  private NotificationRepository notificationRepository;

  /**
   * save log vacation
   * @param request : "AddLogVacationDayRequest.class"
   * @return base response
   */

  @Transactional(rollbackFor = Exception.class)
  public BaseResponse<?> save(AddLogVacationDayRequest request) {


    EmployeeEntity employeeLog = employeeValidator.accountEmployeeExist(getCustomUserDetail().getUsername());
    EmployeeEntity employeeAssign = employeeValidator.employeeExist(request.getIdAssign());
    checkPositionIdAssign(employeeAssign.getPositionId());

    LogVacationDayEntity logVacationDay = modelMapper.map(request, LogVacationDayEntity.class);

    logVacationDay.setIdEmployee(employeeLog.getId());
    logVacationDay.setAccountEmployee(employeeLog.getAccount());
    logVacationDay.setAccountAssign(employeeAssign.getAccount());
    logVacationDay.setStatusApprove(Boolean.FALSE);
    logVacationDay.setTimeLogStart(timeLog(request.getTypeDayLog(), request.getDateLogVacation()).get(0));
    logVacationDay.setTimeLogEnd(timeLog(request.getTypeDayLog(), request.getDateLogVacation()).get(1));

    LogVacationDayEntity entity = logVacationDayRepository.save(logVacationDay);
    // send notification real time;

    return responseV1(SystemMessageCode.CommonMessage.CODE_SUCCESS,
        SystemMessageCode.CommonMessage.SAVE_SUCCESS,
        entity);
  }

  public BaseResponse<?> update(UpdateLogVacationDayRequest request) {
    checkLogExist(request.getId());

    EmployeeEntity employeeLog = employeeValidator.accountEmployeeExist(getCustomUserDetail().getUsername());
    EmployeeEntity employeeAssign = employeeValidator.employeeExist(request.getIdAssign());
    checkPositionIdAssign(employeeAssign.getPositionId());
    LogVacationDayEntity logVacationDay = modelMapper.map(request, LogVacationDayEntity.class);

    logVacationDay.setIdEmployee(employeeLog.getId());
    logVacationDay.setAccountEmployee(employeeLog.getAccount());
    logVacationDay.setAccountAssign(employeeAssign.getAccount());
    logVacationDay.setStatusApprove(Boolean.FALSE);
    logVacationDay.setTimeLogStart(timeLog(request.getTypeDayLog(), request.getDateLogVacation()).get(0));
    logVacationDay.setTimeLogEnd(timeLog(request.getTypeDayLog(), request.getDateLogVacation()).get(1));

    LogVacationDayEntity entity = logVacationDayRepository.save(logVacationDay);
    return responseV1(SystemMessageCode.CommonMessage.CODE_SUCCESS,
        SystemMessageCode.CommonMessage.SAVE_SUCCESS,
        entity);
  }

  /**
   *
   * @param position : is id of post personal entrusted
   */
  private void checkPositionIdAssign(Long position) {
    if (position > PositionEnum.POSITION_EMPLOYEE.getCode()) {
      throw new ErrorV1Exception(messageV1Exception(
          SystemMessageCode.EmployeeService.CODE_ID_ASSIGN_NOT_PERMISSION,
          SystemMessageCode.EmployeeService.MESSAGE_ID_ASSIGN_NOT_PERMISSION
      ));
    }
  }

  /**
   * Build time log start and time log end
   * List log time have two element
   * 1 : time log start
   * 2 : time log end
   */
  private List<LocalDateTime> timeLog(Long typeLog, LocalDate dateLog) {
    LocalDateTime timeLogStart =
        TypeLogVacationDayEnum.MORNING.getCode() == typeLog
            || TypeLogVacationDayEnum.ENOUGH.getCode() == typeLog
            ? LocalDateTime.of(dateLog, Constants.TimeRegulations.WORK_START_TIME_DAY)
            : LocalDateTime.of(dateLog, Constants.TimeRegulations.WORK_START_TIME_AFTERNOON);

    LocalDateTime timeLogEnd =
        TypeLogVacationDayEnum.MORNING.getCode() == typeLog
            ? LocalDateTime.of(dateLog, Constants.TimeRegulations.WORK_END_TIME_MORNING)
            : LocalDateTime.of(dateLog, Constants.TimeRegulations.WORK_END_TIME_DAY);

    return new ArrayList<>(List.of(timeLogStart, timeLogEnd));
  }

  /**
   * validate element log vacation exist
   */
  public void checkLogExist(Long id) {
    LogVacationDayEntity entity = logVacationDayRepository.findById(id).orElse(null);
    if (ObjectUtils.isEmpty(entity)) {
      throw new ErrorV2Exception(messageV2Exception(
          SystemMessageCode.EmployeeService.CODE_LOG_VACATION_NOT_EXIST,
          SystemMessageCode.EmployeeService.LOG_VACATION,
          SystemMessageCode.CommonMessage.NOT_EXIST_IN_SYSTEM
      ));
    }
  }
}
