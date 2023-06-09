package com.example.Employee_Service.service.employee;

import com.example.Employee_Service.model.dto.request.employee.AddContractDetailRequest;
import com.example.Employee_Service.model.entity.employee.ContractDetailEntity;
import com.example.Employee_Service.model.entity.employee.EmployeeEntity;
import com.example.Employee_Service.repository.employee.ContractDetailRepository;
import com.example.Employee_Service.validate.employee.EmployeeValidator;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.the.common.model.payload.response.BaseResponse;
import com.the.common.service.BaseService;
import com.the.common.system_message.SystemMessageCode;
import com.the.common.validator.regex.RegexHelper;
import org.apache.commons.lang3.ObjectUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import javax.annotation.Resource;
import java.util.Iterator;

@Service("ContractDetailService")
public class ContractDetailService extends BaseService {
  @Resource
  @Qualifier("RegexHelper")
  private RegexHelper regexHelper;
  @Resource
  @Qualifier("ContractDetailRepository")
  private ContractDetailRepository contractDetailRepository;
  @Resource
  @Qualifier("EmployeeValidator")
  private EmployeeValidator employeeValidator;
  @Resource
  @Qualifier("ModelMapper")
  private ModelMapper modelMapper;
  public BaseResponse<?> save(AddContractDetailRequest request, BindingResult result) {
    hasError(result);
    EmployeeEntity employee = employeeValidator.employeeExist(request.getIdEmployee());
    Long totalSalary = 0L;
    if (!ObjectUtils.isEmpty(request.getSalarySubsidize())) {
      ObjectNode salarySubsidize = request.getSalarySubsidize();
      Iterator<String> fields = salarySubsidize.fieldNames();
      while (fields.hasNext()) {
        String value = salarySubsidize.get(fields.next()).asText();
        regexHelper.regexNumber(value, SystemMessageCode.EmployeeService.SALARY_SUBSIDIZE);
        totalSalary += Long.parseLong(value);
      }
    }
    totalSalary += request.getSalaryBasic();
    ContractDetailEntity contractDetailEntity = modelMapper.map(request, ContractDetailEntity.class);
    contractDetailEntity.setAccountEmployee(employee.getAccount());
    contractDetailEntity.setSalaryTotal(totalSalary);

    ContractDetailEntity entity = contractDetailRepository.save(contractDetailEntity);
    return responseV1(
        SystemMessageCode.CommonMessage.CODE_SUCCESS,
        SystemMessageCode.CommonMessage.SAVE_SUCCESS,
        entity
    );
  }
}
