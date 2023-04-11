package com.example.Employee_Service.batch_scheduler;

import com.obys.common.service.BaseService;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class CalculationSalaryBatch extends BaseService {


  public void calculationSalary() {
    LocalDate currentDate = LocalDate.now();
    LOGGER.info("Date now :" + currentDate);
    LocalDate yesterday = currentDate.minusDays(1);
  }
}
