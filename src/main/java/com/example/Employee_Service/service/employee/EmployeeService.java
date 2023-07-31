package com.example.Employee_Service.service.employee;

import com.example.Employee_Service.enums.StatusEmployeeEnum;
import com.example.Employee_Service.model.dto.communicate_kafka.employee.RegistryEmployeeProducer;
import com.example.Employee_Service.model.dto.request.employee.AddEmployeeRequest;
import com.example.Employee_Service.model.entity.employee.EmployeeEntity;
import com.example.Employee_Service.repository.employee.EmployeeRepository;
import com.example.Employee_Service.repository.employee.PartRepository;
import com.example.Employee_Service.service.jwt.JWTService;
import com.example.Employee_Service.validate.employee.EmployeeValidator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.the.common.constant.Constants;
import com.the.common.exception.ErrorV1Exception;
import com.the.common.kafka.Topic;
import com.the.common.model.payload.response.BaseResponse;
import com.the.common.service.BaseService;
import com.the.common.system_message.SystemMessageCode;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.Random;
import java.util.UUID;

@Service("EmployeeService")
public class EmployeeService extends BaseService {

  private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeService.class);
  @Resource
  private KafkaTemplate<String, String> kafkaTemplate;
  @Resource
  @Qualifier("EmployeeRepository")
  private EmployeeRepository employeeRepository;
  @Resource
  @Qualifier("PartRepository")
  private PartRepository partRepository;
  @Resource
  @Qualifier("ModelMapper")
  private ModelMapper modelMapper;
  @Resource
  @Qualifier("ObjectMapper")
  private ObjectMapper objectMapper;
  @Resource
  @Qualifier("JWTService")
  private JWTService jwtService;
  @Resource
  @Qualifier("EmployeeValidator")
  private EmployeeValidator employeeValidator;


  @Transactional(rollbackFor = {Exception.class})
  public BaseResponse<?> save(AddEmployeeRequest request, BindingResult result, HttpServletRequest httpServlet) {
    hasError(result);
    employeeValidator.validateSaveEmployee(request);
    employeeValidator.checkPositionExist(request.getPositionId());
    EmployeeEntity employeeEntity = modelMapper.map(request, EmployeeEntity.class);
    String account = buildAccount(request.getFullName());
    String code = buildCode();
    String uuid = String.valueOf(UUID.randomUUID());
    LocalDate timeStartJoinJob = LocalDate.now();
    employeeEntity.setAccount(account);
    employeeEntity.setEmailCompany(buildEmailCompany(account));
    employeeEntity.setCode(code);
    employeeEntity.setUuid(uuid);
    employeeEntity.setDateStartJoin(timeStartJoinJob);
    employeeEntity.setStatusWork(StatusEmployeeEnum.WORKING.getCode());
    EmployeeEntity employee = employeeRepository.save(employeeEntity);
    partRepository.updateTotalMember(employee.getPartId());
//    sendInfoEmployeeAuthor(employee, httpServlet);
    return responseV1(
        SystemMessageCode.CommonMessage.CODE_SUCCESS,
        SystemMessageCode.CommonMessage.SAVE_SUCCESS,
        employee
    );
  }

  private void sendInfoEmployeeAuthor(EmployeeEntity employee, HttpServletRequest httpServlet) {
    try {
      RegistryEmployeeProducer employeeProducer =
          RegistryEmployeeProducer.builder()
              .account(employee.getAccount())
              .email(employee.getEmailCompany())
              .telephone(employee.getTelephone())
              .build();
      ProducerRecord<String, String> record = new ProducerRecord<>(Topic.TOPIC_REGISTRY_EMPLOYEE, objectMapper.writeValueAsString(employeeProducer));
      record.headers().add(new RecordHeader(Constants.AuthService.AUTHORIZATION, jwtService.getTokenFromRequest(httpServlet).getBytes()));
      record.headers().add(Constants.AuthService.UUID, employee.getUuid().getBytes());
      kafkaTemplate.send(record);
    } catch (Exception e) {
      LOGGER.error("[Send message info employee registry to author service  :] ----->" + e.getMessage());
      throw new ErrorV1Exception(
          messageV1Exception(
              SystemMessageCode.EmployeeService.CODE_SEND_INFO_EMPLOYEE,
              SystemMessageCode.EmployeeService.MESSAGE_SEND_INFO_EMPLOYEE
          )
      );
    }


  }

  protected String buildAccount(String str) {
    String account = "";
    String accountBuild = "";
    boolean check = true;
    while (check) {
      account = reverseString(removeAccent(str)).replaceFirst(" ", ".").replaceAll(" ", "");
      if (employeeRepository.countByAccount(account) == 0) {
        accountBuild = account;
      } else {
        accountBuild = account + employeeRepository.countByAccount(account);
      }
      if (employeeRepository.findByAccount(accountBuild).isEmpty()) {
        check = false;
      }
    }
    return accountBuild;
  }

  protected String buildCode() {
    Random rand = new Random();
    String yearNow = String.valueOf(LocalDate.now().getYear());
    String codeBuild = "";
    boolean check = true;
    while (check) {
      String codeRandom = String.valueOf(rand.nextInt(89999) + 10000);
      codeBuild = Constants.Common.VTI_TITLE + "_" + yearNow + codeRandom;
      if (employeeRepository.findByCode(codeBuild).isEmpty()) {
        check = false;
      }
    }
    return codeBuild;
  }

  protected String buildEmailCompany(String account) {
    return account + Constants.Common.VTI_EMAIL;
  }

}
