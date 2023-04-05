package com.example.Employee_Service.model.entity.employee;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.obys.common.model.entity.BaseEntity;
import lombok.*;
import org.checkerframework.common.aliasing.qual.Unique;
import org.hibernate.validator.constraints.Length;
import javax.persistence.*;
import java.time.LocalDate;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "employee")
public class EmployeeEntity extends BaseEntity {

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

  @JsonFormat(pattern = "dd-MM-yyyy")
  @Column(name = "birthDay")
  private LocalDate birthDay;

  @Column(name = "address")
  private String address;

  @Column(name = "date_start_join")
  @JsonFormat(pattern = "dd-MM-yyyy")
  private LocalDate dateStartJoin;

  @Column(name = "date_end_join")
  @JsonFormat(pattern = "dd-MM-yyyy")
  private LocalDate dateEndJoin;

  @Column(name = "status_work")
  private Integer statusWork;

  @Column(name = "position_id")
  private Long positionId;

  @Column(name = "part_id")
  private Long partId;

  @Column(name = "contact_id")
  private Long contactId;

}
