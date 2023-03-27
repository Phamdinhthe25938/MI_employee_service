package com.example.Employee_Service.model.entity.employee;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.obys.common.model.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.checkerframework.common.aliasing.qual.Unique;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import javax.validation.constraints.Max;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "employee")
@Entity
@Builder
public class Employee extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Length(max = 50)
    @Unique
    @Column(name = "code")
    private String code;

    @Length(max = 50)
    @Unique
    @Column(name = "uuid")
    private String uuid;

    @Length(max = 50)
    @Column(name = "account")
    @Unique
    private String account;

    @Length(max = 50)
    @Column(name = "full_name")
    private String fullName;

    @Column(name = "image_name")
    private String imageName;

    @Column(name = "telephone")
    @Unique
    private String telephone;

    @Column(name = "email_company")
    @Unique
    private String emailCompany;

    @Column(name = "email_personal")
    private String emailPersonal;

    @Column(name = "number_cccd")
    @Unique
    private String numberCCCD;

    @JsonFormat(pattern = "dd-mm-yyyy")
    @Column(name = "birthDay")
    private Date birthDay;

    @Column(name = "address")
    private String address;

    @Column(name = "date_start_join")
    @CreatedDate
    @JsonFormat(pattern = "dd-mm-yyyy")
    private Date dateStartJoin;

    @Column(name = "date_end_join")
    @JsonFormat(pattern = "dd-mm-yyyy")
    private Date dateEndJoin;

    @Column(name = "position_id")
    private Long positionId;

    @Column(name = "part_id")
    private Long partId;

    @Column(name = "contact_id")
    private Long contactId;

}
