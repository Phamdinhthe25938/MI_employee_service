package com.example.Employee_Service.model.entity.employee;

import com.the.common.model.entity.BaseEntity;
import lombok.*;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.time.YearMonth;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "vacation_day")
@TypeDef(name = "yearMonth", typeClass = YearMonth.class)
public class VacationDayEntity extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;
  @Column(name = "id_employee")
  private Long idEmployee;
  @Column(name = "account_employee")
  private String accountEmployee;
  @Column(name = "number_day")
  private Integer numberDay;
  @Column(name = "type_leave")
  private Integer typeLeave;
  @Column(name = "month_vacation")
  @Type(type = "yearMonth")
  private YearMonth monthVacation;
  @Column(name = "status")
  private Integer status;
}
