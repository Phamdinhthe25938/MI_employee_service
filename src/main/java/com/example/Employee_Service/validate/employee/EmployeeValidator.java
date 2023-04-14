package com.example.Employee_Service.validate.employee;

import com.example.Employee_Service.model.dto.request.employee.AddEmployeeRequest;
import com.example.Employee_Service.model.entity.employee.EmployeeEntity;
import com.example.Employee_Service.model.entity.employee.PositionEntity;
import com.example.Employee_Service.repository.employee.EmployeeRepository;
import com.example.Employee_Service.repository.employee.PositionRepository;
import com.obys.common.exception.ErrorV1Exception;
import com.obys.common.exception.ErrorV2Exception;
import com.obys.common.service.BaseService;
import com.obys.common.system_message.SystemMessageCode;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Optional;

@Component("EmployeeValidator")
public class EmployeeValidator extends BaseService {

  @Resource
  @Qualifier("EmployeeRepository")
  private EmployeeRepository employeeRepository;

  @Resource
  @Qualifier("PositionRepository")
  private PositionRepository positionRepository;

  public void validateSaveEmployee(AddEmployeeRequest request) {
    telephoneEmployeeExist(request.getTelephone());
    emailEmployeePersonalExist(request.getEmailPersonal());
    numberCCCDEmployeeExist(request.getNumberCCCD());
  }

  public void uuidIsValid(String uuidRequest, String uuidAccount) {
    if (!uuidRequest.equals(uuidAccount)) {
      throw new ErrorV1Exception(messageV1Exception(
          SystemMessageCode.CommonMessage.CODE_UUID_IS_NOT_VALID,
          SystemMessageCode.CommonMessage.MESSAGE_UUID_IS_NOT_VALID
      ));
    }
  }
  public void accountIsValid(String accountRequest, String accountToken) {
    if (!accountRequest.equals(accountToken)) {
      throw new ErrorV1Exception(messageV1Exception(
          SystemMessageCode.EmployeeService.CODE_ACCOUNT_NOT_MATCH,
          SystemMessageCode.EmployeeService.MESSAGE_ACCOUNT_NOT_MATCH
      ));
    }
  }

  public EmployeeEntity employeeExist(Long id) {
    EmployeeEntity employee = employeeRepository.findById(id).orElse(null);
    if (ObjectUtils.isEmpty(employee)) {
      throw new ErrorV2Exception(messageV2Exception(
          SystemMessageCode.EmployeeService.CODE_ACCOUNT_NOT_EXIST,
          SystemMessageCode.EmployeeService.ACCOUNT,
          SystemMessageCode.CommonMessage.NOT_EXIST_IN_SYSTEM
      ));
    }
    return employee;
  }

  public EmployeeEntity accountEmployeeExist(String account) {
    Optional<EmployeeEntity> employee = employeeRepository.findByAccount(account);
    if (employee.isEmpty()) {
      throw new ErrorV2Exception(messageV2Exception(
          SystemMessageCode.EmployeeService.CODE_ACCOUNT_NOT_EXIST,
          SystemMessageCode.EmployeeService.ACCOUNT,
          SystemMessageCode.CommonMessage.NOT_EXIST_IN_SYSTEM
      ));
    }
    return employee.get();
  }

  public void telephoneEmployeeExist(String telephone) {
    if (employeeRepository.findByTelephone(telephone).isPresent()) {
      throw new ErrorV2Exception(messageV2Exception(
          SystemMessageCode.EmployeeService.CODE_TELEPHONE_EXIST,
          SystemMessageCode.EmployeeService.TELEPHONE,
          SystemMessageCode.CommonMessage.EXIST_IN_SYSTEM
      ));
    }
  }

  public void emailEmployeePersonalExist(String email) {
    if (employeeRepository.findByEmailPersonal(email).isPresent()) {
      throw new ErrorV2Exception(messageV2Exception(
          SystemMessageCode.EmployeeService.CODE_EMAIL_EXIST,
          SystemMessageCode.EmployeeService.EMAIL_PERSONAL,
          SystemMessageCode.CommonMessage.EXIST_IN_SYSTEM
      ));
    }
  }

  public void numberCCCDEmployeeExist(String numberCCCD) {
    if (employeeRepository.findByNumberCCCD(numberCCCD).isPresent()) {
      throw new ErrorV2Exception(messageV2Exception(
          SystemMessageCode.EmployeeService.CODE_NUMBERCCCD_EXIST,
          SystemMessageCode.EmployeeService.NUMBERCCCD,
          SystemMessageCode.CommonMessage.EXIST_IN_SYSTEM
      ));
    }
  }

  public PositionEntity checkPositionExist(Long id) {
    PositionEntity entity = positionRepository.findById(id).orElse(null);
    if (entity == null) {
      throw new ErrorV2Exception(messageV2Exception(SystemMessageCode.EmployeeService.CODE_POSITION_NOT_EXIST,
          SystemMessageCode.EmployeeService.POSITION, SystemMessageCode.CommonMessage.NOT_EXIST_IN_SYSTEM));
    }
    return entity;
  }
}
