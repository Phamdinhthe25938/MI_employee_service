package com.example.Employee_Service.validate.contract;

import com.example.Employee_Service.model.entity.employee.ContractDetailEntity;
import com.example.Employee_Service.repository.employee.ContractDetailRepository;
import com.the.common.exception.ErrorV1Exception;
import com.the.common.service.BaseService;
import com.the.common.system_message.SystemMessageCode;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component("ContractDetailValidator")
public class ContractDetailValidator extends BaseService {

  @Resource
  @Qualifier("ContractDetailRepository")
  private ContractDetailRepository contractDetailRepository;

  public ContractDetailEntity checkEmployeeExist(Long id) {
    ContractDetailEntity contractDetailEntity = contractDetailRepository.findByIdEmployee(id).orElse(null);
    if (contractDetailEntity == null) {
      throw new ErrorV1Exception(messageV1Exception(SystemMessageCode.EmployeeService.CODE_ACCOUNT_NOT_CREATE_CONTRACT,
          SystemMessageCode.EmployeeService.MESSAGE_ACCOUNT_NOT_CREATE_CONTRACT));
    }
    return contractDetailEntity;
  }
}
