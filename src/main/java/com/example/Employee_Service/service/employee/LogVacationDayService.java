package com.example.Employee_Service.service.employee;

import com.example.Employee_Service.enums.StatusVacationDayEnum;
import com.example.Employee_Service.enums.TypeLogVacationDayEnum;
import com.example.Employee_Service.model.dto.request.employee.*;
import com.example.Employee_Service.model.dto.response.employee.GetListVacationResponse;
import com.example.Employee_Service.model.entity.employee.VacationDayEntity;
import com.example.Employee_Service.repository.employee.VacationDayRepository;
import com.example.Employee_Service.service.jwt.JWTService;
import com.example.Employee_Service.validate.vacation_day.VacationDayValidate;
import com.the.common.exception.ErrorV2Exception;
import com.example.Employee_Service.model.entity.employee.EmployeeEntity;
import com.example.Employee_Service.model.entity.employee.LogVacationDayEntity;
import com.example.Employee_Service.repository.employee.LogVacationDayRepository;
import com.example.Employee_Service.validate.employee.EmployeeValidator;
import com.the.common.constant.Constants;
import com.the.common.enums.PositionEnum;
import com.the.common.exception.ErrorV1Exception;
import com.the.common.model.payload.response.BaseResponse;
import com.the.common.model.payload.response.MetaList;
import com.the.common.service.BaseService;
import com.the.common.system_message.SystemMessageCode;
import org.apache.commons.lang3.ObjectUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service("LogVacationDayService")
public class LogVacationDayService extends BaseService {
  @Resource(name = "LogVacationDayRepository")
  private LogVacationDayRepository logVacationDayRepository;
  @Resource(name = "ModelMapper")
  private ModelMapper modelMapper;
  @Resource(name = "EmployeeValidator")
  private EmployeeValidator employeeValidator;
  @Resource(name = "VacationDayValidate")
  private VacationDayValidate vacationDayValidate;
  @Resource(name = "JWTService")
  private JWTService jwtService;
  @Resource(name = "VacationDayRepository")
  private VacationDayRepository vacationDayRepository;

  /**
   * save log vacation
   *
   * @param request : "AddLogVacationDayRequest.class"
   * @return base response
   */
  @Transactional(rollbackFor = Exception.class)
  public BaseResponse<?> save(AddLogVacationDayRequest request, BindingResult result) {
    hasError(result);
    EmployeeEntity employeeLog = employeeValidator.accountEmployeeExist(getCustomUserDetail().getUsername());
    EmployeeEntity employeeAssign = employeeValidator.employeeExist(request.getIdAssign());
    checkPositionIdAssign(employeeAssign.getPositionId());
    // check vacation day exist
    VacationDayEntity vacationDay = vacationDayValidate.checkVacationDayExist(request.getIdVacationDay());
    // check status day vacation valid
    vacationDayValidate.checkStatusVacationDayValid(vacationDay.getStatus(), Boolean.FALSE);
    // check vacation day have belong employee log
    vacationDayValidate.checkVacationDayMatch(vacationDay, employeeLog.getId());

    LogVacationDayEntity logVacationDay = modelMapper.map(request, LogVacationDayEntity.class);

    logVacationDay.setIdEmployee(employeeLog.getId());
    logVacationDay.setAccountEmployee(employeeLog.getAccount());
    logVacationDay.setUuidEmployee(employeeLog.getUuid());
    logVacationDay.setAccountAssign(employeeAssign.getAccount());
    logVacationDay.setStatusApprove(Boolean.FALSE);
    logVacationDay.setTimeLogStart(timeLog(request.getTypeDayLog(), request.getDateLogVacation()).get(0));
    logVacationDay.setTimeLogEnd(timeLog(request.getTypeDayLog(), request.getDateLogVacation()).get(1));
    logVacationDay.setIdVacationDay(vacationDay.getId());

    LogVacationDayEntity entity = logVacationDayRepository.save(logVacationDay);
    // update status vacation day to 2 : is select
    vacationDayRepository.updateStatus(StatusVacationDayEnum.SELECTED.getCode(), request.getIdVacationDay());
    return responseV1(SystemMessageCode.CommonMessage.CODE_SUCCESS,
        SystemMessageCode.CommonMessage.SAVE_SUCCESS,
        entity);
  }

  /**
   * update log vacation day
   * @param request UpdateLogVacationDayRequest
   * @return null
   */
  @Transactional(rollbackFor = Exception.class)
  public BaseResponse<?> update(UpdateLogVacationDayRequest request) {
    LogVacationDayEntity logVacationDayEntity = checkLogExist(request.getId());
    if (Boolean.TRUE.equals(logVacationDayEntity.getStatusApprove())) {
      throw new ErrorV1Exception(messageV1Exception(SystemMessageCode.EmployeeService.CODE_STATUS_APPROVE_INVALID,
          SystemMessageCode.EmployeeService.MESSAGE_DELETE_LOG_VACATION_INVALID));
    }
    EmployeeEntity employeeLog = employeeValidator.accountEmployeeExist(getCustomUserDetail().getUsername());
    EmployeeEntity employeeAssign = employeeValidator.employeeExist(request.getIdAssign());
    checkPositionIdAssign(employeeAssign.getPositionId());
    // check vacation day exist
    VacationDayEntity vacationDay = vacationDayValidate.checkVacationDayExist(request.getIdVacationDay());
    // check status day vacation valid
    if (Objects.equals(request.getIdVacationDay(), logVacationDayEntity.getIdVacationDay())) {
      vacationDayValidate.checkStatusVacationDayValid(vacationDay.getStatus(), Boolean.FALSE);
    } else {
      vacationDayValidate.checkStatusVacationDayValid(vacationDay.getStatus(), Boolean.TRUE);
    }
    // check vacation day have belong employee log
    vacationDayValidate.checkVacationDayMatch(vacationDay, employeeLog.getId());

    LogVacationDayEntity logVacationDay = modelMapper.map(request, LogVacationDayEntity.class);

    logVacationDay.setIdEmployee(employeeLog.getId());
    logVacationDay.setAccountEmployee(employeeLog.getAccount());
    logVacationDay.setUuidEmployee(employeeLog.getUuid());
    logVacationDay.setAccountAssign(employeeAssign.getAccount());
    logVacationDay.setStatusApprove(Boolean.FALSE);
    logVacationDay.setTimeLogStart(timeLog(request.getTypeDayLog(), request.getDateLogVacation()).get(0));
    logVacationDay.setTimeLogEnd(timeLog(request.getTypeDayLog(), request.getDateLogVacation()).get(1));
    logVacationDay.setIdVacationDay(request.getIdVacationDay());

    // update status vacation day
    if (!Objects.equals(request.getIdVacationDay(), logVacationDayEntity.getIdVacationDay())) {
      vacationDayRepository.updateStatus(StatusVacationDayEnum.SELECTED.getCode(), request.getIdVacationDay());
      vacationDayRepository.updateStatus(StatusVacationDayEnum.NOT_SELECT.getCode(), logVacationDayEntity.getIdVacationDay());
    }
    // update log vacation
    LogVacationDayEntity entity = logVacationDayRepository.save(logVacationDay);
    return responseV1(SystemMessageCode.CommonMessage.CODE_SUCCESS,
        SystemMessageCode.CommonMessage.UPDATE_SUCCESS,
        entity);
  }

  @Transactional(rollbackFor = Exception.class)
  public BaseResponse<?> delete(DeleteLogVacationRequest request, HttpServletRequest httpServletRequest) {
    LogVacationDayEntity entity = checkLogExist(request.getId());
    String uuid = getUUID(httpServletRequest);
    if (Boolean.FALSE.equals(entity.getStatusApprove())) {
      throw new ErrorV1Exception(messageV1Exception(SystemMessageCode.EmployeeService.CODE_STATUS_APPROVE_INVALID,
          SystemMessageCode.EmployeeService.MESSAGE_DELETE_LOG_VACATION_INVALID));
    }
    logVacationDayRepository.deleteByIdAndUuidEmployee(request.getId(), uuid);
    return responseV1(SystemMessageCode.CommonMessage.CODE_SUCCESS, SystemMessageCode.CommonMessage.DELETE_SUCCESS, null);
  }

  /**
   * @param request            : GetListLogVacationByPersonSend
   * @param httpServletRequest : HttpServletRequest
   */
  public BaseResponse<?> getByPersonSend(GetListLogVacationByPersonSend request, HttpServletRequest httpServletRequest) {
    Pageable pageable = buildPageable(request.getMeta());
    EmployeeEntity employee = employeeValidator.accountEmployeeExist(jwtService.getSubjectFromToken(jwtService.getTokenFromRequest(httpServletRequest)));
    Page<LogVacationDayEntity> values = logVacationDayRepository.getAllByIdEmployeeAndUuidEmployee(employee.getId(), getUUID(httpServletRequest), pageable);
    Long total = values.getTotalElements();
    MetaList metaList = buildMetaList(pageable, total);
    return responseV1(SystemMessageCode.CommonMessage.CODE_SUCCESS,
        SystemMessageCode.CommonMessage.GET_SUCCESS,
        new GetListVacationResponse(values.getContent(), metaList)
    );
  }

  /**
   * @param request            : GetListLogVacationByPersonSend
   * @param httpServletRequest : HttpServletRequest
   * @return list log vacation by id assign
   */
  public BaseResponse<?> getByAssign(GetListLogVacationByAssignRequest request, HttpServletRequest httpServletRequest) {
    Pageable pageable = buildPageable(request.getMeta());
    EmployeeEntity employee = employeeValidator.accountEmployeeExist(jwtService.getSubjectFromToken(jwtService.getTokenFromRequest(httpServletRequest)));
    Page<LogVacationDayEntity> values = logVacationDayRepository.getAllByIdAssign(employee.getId(), pageable);
    Long total = values.getTotalElements();
    MetaList metaList = buildMetaList(pageable, total);
    return responseV1(SystemMessageCode.CommonMessage.CODE_SUCCESS,
        SystemMessageCode.CommonMessage.GET_SUCCESS,
        new GetListVacationResponse(values.getContent(), metaList)
    );
  }

  @Transactional(rollbackFor = Exception.class)
  public BaseResponse<?> approveLog(ApproveLogVacationRequest request) {
    LogVacationDayEntity logVacationDay = checkLogExist(request.getId());
    EmployeeEntity employee = employeeValidator.accountEmployeeExist(getCustomUserDetail().getUsername());
    if (!employee.getId().equals(logVacationDay.getIdAssign())) {
      throw new ErrorV1Exception(messageV1Exception(SystemMessageCode.EmployeeService.CODE_ID_PERSON_APPROVE_NOT_MATCH,
          SystemMessageCode.EmployeeService.MESSAGE_ID_PERSON_APPROVE_NOT_MATCH));
    }
    // update status log vacation to approve
    logVacationDayRepository.updateStatusApprove(request.getId());
    // update status vacation day to approve
    vacationDayRepository.updateStatus(StatusVacationDayEnum.APPROVED.getCode(), logVacationDay.getIdVacationDay());
    return responseV1(SystemMessageCode.CommonMessage.CODE_SUCCESS,
        SystemMessageCode.CommonMessage.UPDATE_SUCCESS,
        null
    );
  }

  /**
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
  public LogVacationDayEntity checkLogExist(Long id) {
    LogVacationDayEntity entity = logVacationDayRepository.findById(id).orElse(null);
    if (ObjectUtils.isEmpty(entity)) {
      throw new ErrorV2Exception(messageV2Exception(
          SystemMessageCode.EmployeeService.CODE_LOG_VACATION_NOT_EXIST,
          SystemMessageCode.EmployeeService.LOG_VACATION,
          SystemMessageCode.CommonMessage.NOT_EXIST_IN_SYSTEM
      ));
    }
    return entity;
  }
}
