package com.example.Employee_Service.service.employee;

import com.example.Employee_Service.model.dto.request.employee.AddContractDetailRequest;
import com.example.Employee_Service.model.entity.employee.ContractDetailEntity;
import com.example.Employee_Service.repository.employee.ContractDetailRepository;
import com.example.Employee_Service.validate.employee.EmployeeValidator;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.obys.common.model.payload.response.BaseResponse;
import com.obys.common.service.BaseService;
import com.obys.common.system_message.SystemMessageCode;
import com.obys.common.validator.regex.RegexHelper;
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
    employeeValidator.employeeExist(request.getIdEmployee());
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
    contractDetailEntity.setSalaryTotal(totalSalary);

    ContractDetailEntity entity = contractDetailRepository.save(contractDetailEntity);
    return responseV1(
        SystemMessageCode.CommonMessage.CODE_SUCCESS,
        SystemMessageCode.CommonMessage.SAVE_SUCCESS,
        entity
    );
  }
}
