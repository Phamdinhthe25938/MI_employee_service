package com.example.Employee_Service.batch_scheduler;

import com.example.Employee_Service.enums.StatusEmployeeEnum;
import com.example.Employee_Service.enums.StatusVacationDayEnum;
import com.example.Employee_Service.enums.TypeLeaveEnum;
import com.example.Employee_Service.model.entity.employee.EmployeeEntity;
import com.example.Employee_Service.model.entity.employee.VacationDayEntity;
import com.example.Employee_Service.repository.employee.EmployeeRepository;
import com.example.Employee_Service.repository.employee.VacationDayRepository;
import com.the.common.service.BaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.YearMonth;
import java.util.LinkedList;
import java.util.List;

@Component("VacationDayBatch")
public class VacationDayBatch extends BaseService {
  private static final Logger LOGGER = LoggerFactory.getLogger(VacationDayBatch.class);
  @Resource(name = "VacationDayRepository")
  private VacationDayRepository vacationDayRepository;
  @Resource(name = "EmployeeRepository")
  private EmployeeRepository employeeRepository;
  @Resource(name = "threadPoolExecutor")
  private ThreadPoolTaskExecutor executor;

  /**
   * batch run at month finish
   */
// @Scheduled(cron = "15 * * * * *")
 public void run () {
   LOGGER.info("Start run batch vacation day !");

   try {
     YearMonth currentMonth = YearMonth.now();
     YearMonth lastMonth = YearMonth.now().minusMonths(1);
     LOGGER.info("Batch vacation day date now : " + currentMonth);
     LOGGER.info("Batch vacation day yesterday now : " + lastMonth);
     List<EmployeeEntity> employeeList = employeeRepository.getAllByStatusWork(StatusEmployeeEnum.WORKING.getCode());
     List<VacationDayEntity> vacationDayEntities = new LinkedList<>();
     employeeList.forEach(item -> {
       executor.execute(() -> {
         VacationDayEntity vacationDayEntity = VacationDayEntity.builder()
             .idEmployee(item.getId())
             .accountEmployee(item.getAccount())
             .numberDay(1)
             .typeLeave(TypeLeaveEnum.ON_LEAVE.getCode())
             .monthVacation(lastMonth)
             .status(StatusVacationDayEnum.NOT_SELECT.getCode())
             .build();
         vacationDayEntities.add(vacationDayEntity);
       });
     });
     vacationDayRepository.saveAll(vacationDayEntities);
     LOGGER.error("Batch vacation day success ! ");
   }catch (Exception e) {
     LOGGER.error("Batch vacation day fail ===> " + e.getMessage());
   }
 }
}
