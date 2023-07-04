package com.example.Employee_Service.model.dto.request.employee;

import com.the.common.validator.annotation.Required;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateLogVacationDayRequest {
  @Min(1)
  @Required(message = "Id not null")
  private Long id;
  @Min(1)
  @Required(message = "{idVacationDay.is.required}")
  private Long idVacationDay;
  @Min(1)
  @Required(message = "{idAssign.is.required}")
  private Long idAssign;
  @Required(message = "{date.log.vacation.is.required}")
  private LocalDate dateLogVacation;
  @Required(message = "{reason.rest.is.required}")
  private String reasonRest;
  @Required(message = "{type.day.log.is.required}")
  @Min(1)
  @Max(3)
  private Long typeDayLog;
}
