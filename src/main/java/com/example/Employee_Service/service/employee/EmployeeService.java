package com.example.Employee_Service.service.employee;


import com.example.Employee_Service.model.dto.communicate_kafka.employee.RegistryEmployeeProducer;
import com.example.Employee_Service.model.dto.request.employee.AddEmployeeRequest;
import com.example.Employee_Service.model.entity.Employee;
import com.example.Employee_Service.repository.employee.EmployeeRepository;
import com.example.Employee_Service.repository.employee.PartRepository;
import com.example.Employee_Service.service.jwt.JWTService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.obys.common.constant.Constants;
import com.obys.common.exception.ErrorV1Exception;
import com.obys.common.exception.ErrorV2Exception;
import com.obys.common.kafka.Topic;
import com.obys.common.model.payload.response.BaseResponse;
import com.obys.common.service.BaseService;
import com.obys.common.system_message.SystemMessageCode;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.apache.kafka.common.serialization.StringSerializer;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.validation.BindingResult;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.Properties;
import java.util.Random;

@Service("EmployeeService")
public class EmployeeService extends BaseService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeService.class);
    @Resource
    private KafkaTemplate<String, String> kafkaTemplate;
    @Resource
    private EmployeeRepository employeeRepository;
    @Resource
    private PartRepository partRepository;
    @Resource
    private ModelMapper modelMapper;
    @Resource
    private ObjectMapper objectMapper;
    @Resource
    private JWTService jwtService;
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse<?> save(AddEmployeeRequest request, BindingResult result, HttpServletRequest httpServlet) {
        hasError(result);
        validateSaveEmployee(request);
        Employee employeeEntity = modelMapper.map(request, Employee.class);
        String account = buildAccount(request.getFullName());
        String code = buildCode();
        employeeEntity.setAccount(account);
        employeeEntity.setEmailCompany(buildEmailCompany(account));
        employeeEntity.setCode(code);
        Employee employee = employeeRepository.save(employeeEntity);
        partRepository.updateTotalMember(employee.getPartId());
        sendInfoEmployeeAuthor(employee, httpServlet);
        return responseV1(
                SystemMessageCode.CommonMessage.CODE_SUCCESS,
                SystemMessageCode.CommonMessage.SAVE_SUCCESS,
                employee
        );
    }
    private void sendInfoEmployeeAuthor(Employee employee, HttpServletRequest httpServlet) {
        try {
            String correlationId = "12345";
            RegistryEmployeeProducer employeeProducer =
                    RegistryEmployeeProducer.builder()
                            .account(employee.getAccount())
                            .email(employee.getEmailCompany())
                            .telephone(employee.getTelephone())
                            .build();
            ProducerRecord<String, String> record = new ProducerRecord<>(Topic.TOPIC_REGISTRY_EMPLOYEE, objectMapper.writeValueAsString(employeeProducer));
            record.headers().add(new RecordHeader(Constants.AuthService.AUTHORIZATION, jwtService.getTokenFromRequest(httpServlet).getBytes()));
            record.headers().add("correlationId", correlationId.getBytes());
//            kafkaTemplate.send(record);

            Properties props = new Properties();
            props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
            props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
            props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
            Producer<String, String> producer = new KafkaProducer<>(props);
            producer.send(record, new Callback() {
                @Override
                public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                    if (e == null) {
                        LOGGER.info("Message sent successfully");
                    } else {
                        LOGGER.info("Message sent failed");
                    }
                    LOGGER.info("Response :" + recordMetadata.hasOffset());
                }
            });
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

    private void validateSaveEmployee(AddEmployeeRequest request) {
        telephoneEmployeeExist(request.getTelephone());
        emailEmployeePersonalExist(request.getEmailPersonal());
        numberCCCDEmployeeExist(request.getNumberCCCD());
    }

    private void telephoneEmployeeExist(String telephone) {
        if (employeeRepository.findByTelephone(telephone).isPresent()) {
            throw new ErrorV2Exception(messageV2Exception(
                    SystemMessageCode.EmployeeService.CODE_TELEPHONE_EXIST,
                    SystemMessageCode.EmployeeService.TELEPHONE,
                    SystemMessageCode.CommonMessage.EXIST_IN_SYSTEM
            ));
        }
    }

    private void emailEmployeePersonalExist(String email) {
        if (employeeRepository.findByEmailPersonal(email).isPresent()) {
            throw new ErrorV2Exception(messageV2Exception(
                    SystemMessageCode.EmployeeService.CODE_EMAIL_EXIST,
                    SystemMessageCode.EmployeeService.EMAIL_PERSONAL,
                    SystemMessageCode.CommonMessage.EXIST_IN_SYSTEM
            ));
        }
    }

    private void numberCCCDEmployeeExist(String numberCCCD) {
        if (employeeRepository.findByNumberCCCD(numberCCCD).isPresent()) {
            throw new ErrorV2Exception(messageV2Exception(
                    SystemMessageCode.EmployeeService.CODE_NUMBERCCCD_EXIST,
                    SystemMessageCode.EmployeeService.NUMBERCCCD,
                    SystemMessageCode.CommonMessage.EXIST_IN_SYSTEM
            ));
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
