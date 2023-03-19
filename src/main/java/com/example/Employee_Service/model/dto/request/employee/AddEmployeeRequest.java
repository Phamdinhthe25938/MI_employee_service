package com.example.Employee_Service.model.dto.request.employee;

import com.obys.common.validator.annotation.Required;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AddEmployeeRequest {

    @Required(message = "{is.required}")
    private String fullName;

    @Required(message = "{is.required}")
    private String imageName;

    @Required(message = "{is.required}")
    private String telephone;

    @Required(message = "{is.required}")
    private String email;

    @Required(message = "{is.required}")
    private String numberCCCD;

    @Required(message = "{is.required}")
    private Date birthDay;

    @Required(message = "{is.required}")
    private String address;

    @Required(message = "{is.required}")
    private Long positionId;

    @Required(message = "{is.required}")
    private Long partId;
}
