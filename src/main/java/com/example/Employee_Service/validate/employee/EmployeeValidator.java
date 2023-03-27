package com.example.Employee_Service.validate.employee;

import com.example.Employee_Service.model.dto.request.employee.AddEmployeeRequest;
import com.example.Employee_Service.repository.employee.EmployeeRepository;
import com.obys.common.exception.ErrorV2Exception;
import com.obys.common.service.BaseService;
import com.obys.common.system_message.SystemMessageCode;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component("EmployeeValidator")
public class EmployeeValidator extends BaseService {

    @Resource
    @Qualifier("EmployeeRepository")
    private EmployeeRepository employeeRepository;

    public void validateSaveEmployee(AddEmployeeRequest request) {
        telephoneEmployeeExist(request.getTelephone());
        emailEmployeePersonalExist(request.getEmailPersonal());
        numberCCCDEmployeeExist(request.getNumberCCCD());
    }

//    public void accountEmployeeExist(String account) {
//        if (employeeRepository.findByAccount(account).isEmpty()) {
//            throw new ErrorV2Exception(me)
//        }
//    }
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
}
