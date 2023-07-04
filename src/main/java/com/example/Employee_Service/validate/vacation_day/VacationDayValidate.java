package com.example.Employee_Service.validate.vacation_day;

import com.example.Employee_Service.enums.StatusVacationDayEnum;
import com.example.Employee_Service.model.entity.employee.VacationDayEntity;
import com.example.Employee_Service.repository.employee.VacationDayRepository;
import com.the.common.exception.ErrorV1Exception;
import com.the.common.service.BaseService;
import com.the.common.system_message.SystemMessageCode;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component("VacationDayValidate")
public class VacationDayValidate extends BaseService {

  @Resource(name = "VacationDayRepository")
  private VacationDayRepository vacationDayRepository;

  public VacationDayEntity checkVacationDayExist(Long id) {
    VacationDayEntity entity = vacationDayRepository.findById(id).orElse(null);
    if (ObjectUtils.isEmpty(entity)) {
      throw new ErrorV1Exception(
          messageV1Exception(SystemMessageCode.EmployeeService.CODE_ID_VACATION_DAY_NOT_EXIST,
              SystemMessageCode.EmployeeService.MESSAGE_ID_VACATION_DAY_NOT_EXIST));
    }
    return entity;
  }

  public void checkStatusVacationDayValid(Integer status, Boolean isUpdate) {
    if (isUpdate) {
      if (!(StatusVacationDayEnum.NOT_SELECT.getCode() == status)) {
        throw new ErrorV1Exception(messageV1Exception(SystemMessageCode.EmployeeService.CODE_STATUS_VACATION_DAY_NOT_VALID,
            SystemMessageCode.EmployeeService.MESSAGE_STATUS_VACATION_DAY_NOT_VALID));
      }
    }else {
      if (StatusVacationDayEnum.APPROVED.getCode() == status) {
        throw new ErrorV1Exception(messageV1Exception(SystemMessageCode.EmployeeService.CODE_STATUS_VACATION_DAY_NOT_VALID,
            SystemMessageCode.EmployeeService.MESSAGE_STATUS_VACATION_DAY_NOT_VALID));
      }
    }
  }
  public void checkVacationDayMatch(VacationDayEntity vacationDay, Long idEmployee) {
    if (!vacationDay.getIdEmployee().equals(idEmployee)) {
      throw new ErrorV1Exception(messageV1Exception(SystemMessageCode.EmployeeService.CODE_ID_VACATION_DAY_NOT_MATCH_PERSON,
          SystemMessageCode.EmployeeService.MESSAGE_ID_VACATION_DAY_NOT_MATCH_PERSON));
    }
  }
}
