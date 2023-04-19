package com.example.Employee_Service.batch_scheduler;

import com.example.Employee_Service.enums.StatusEmployeeEnum;
import com.example.Employee_Service.enums.StatusWorkdayEnum;
import com.example.Employee_Service.model.entity.employee.CalculationSalaryEntity;
import com.example.Employee_Service.model.entity.employee.ContractDetailEntity;
import com.example.Employee_Service.model.entity.employee.EmployeeEntity;
import com.example.Employee_Service.model.entity.employee.LogCalculationSalaryEntity;
import com.example.Employee_Service.model.entity.time_scan_manager.TimeScanDateDetailEntity;
import com.example.Employee_Service.repository.employee.CalculationSalaryRepository;
import com.example.Employee_Service.repository.employee.EmployeeRepository;
import com.example.Employee_Service.repository.employee.LogCalculationSalaryRepository;
import com.example.Employee_Service.repository.time_scan_manager.TimeScanDateDetailRepository;
import com.example.Employee_Service.validate.contract.ContractDetailValidator;
import com.example.Employee_Service.validate.employee.EmployeeValidator;
import com.obys.common.enums.DaysOfWeekEnum;
import com.obys.common.service.BaseService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CalculationSalaryBatch extends BaseService {
  @Resource
  @Qualifier("EmployeeRepository")
  private EmployeeRepository employeeRepository;
  @Resource
  @Qualifier("EmployeeValidator")
  private EmployeeValidator employeeValidator;
  @Resource
  @Qualifier("TimeScanDateDetailRepository")
  private TimeScanDateDetailRepository timeScanDateDetailRepository;
  @Resource
  @Qualifier("ContractDetailValidator")
  private ContractDetailValidator contractDetailValidator;
  @Resource
  @Qualifier("CalculationSalaryRepository")
  private CalculationSalaryRepository calculationSalaryRepository;
  @Resource
  @Qualifier("LogCalculationSalaryRepository")
  private LogCalculationSalaryRepository logCalculationSalaryRepository;

//  @Scheduled(cron = "10 * * * * *")
  public void calculationSalary() {
    try {
      YearMonth currentMonth = YearMonth.now();
      YearMonth lastMonth = currentMonth.minusMonths(1);
      LOGGER.info("Calculation salary date now :" + currentMonth);
      LOGGER.info("Calculation salary yesterday now :" + lastMonth);

      LocalDate firstDayOfMonth = lastMonth.atDay(1);
      LocalDate lastDayOfMonth = lastMonth.atEndOfMonth();
      LOGGER.info("Calculation salary first day of month:" + firstDayOfMonth);
      LOGGER.info("Calculation salary last day of month :" + lastDayOfMonth);
      List<String> allAccount = employeeRepository.getAllAccountName(StatusEmployeeEnum.WORKING.getCode());
      List<TimeScanDateDetailEntity> timeScanDateDetailByMonth = timeScanDateDetailRepository.getAllByMonth(firstDayOfMonth, lastDayOfMonth);

      allAccount.forEach(account -> {
        LogCalculationSalaryEntity logCalculationSalaryEntity = logCalculationSalaryRepository.findByAccountAndMonthWork(account, lastMonth).orElse(null);
        if (logCalculationSalaryEntity == null || Boolean.FALSE.equals(logCalculationSalaryEntity.getStatus())) {

          EmployeeEntity employee = employeeValidator.accountEmployeeExist(account);
          ContractDetailEntity contractDetailEntity = contractDetailValidator.checkEmployeeExist(employee.getId());
          Long totalDaysOfMonth = calculationDayMonth(lastMonth.getMonth().getValue(), lastMonth.getYear());

          // Get list time scan by month and year of account
          List<TimeScanDateDetailEntity> timeScanDateDetailByMonthOfAccount = timeScanDateDetailByMonth.
              stream().filter(e -> e.getAccountEmployee().equals(account)
                  && !e.getDayOfWeek().equals(DaysOfWeekEnum.SATURDAY.getCode())
                  && !e.getDayOfWeek().equals(DaysOfWeekEnum.SUNDAY.getCode())).collect(Collectors.toList());

          Long totalDaysEnough = timeScanDateDetailByMonthOfAccount.stream().filter(e -> e.getStatusWorkdays().equals(StatusWorkdayEnum.ENOUGH.getCode())).count();

          Long totalDaysLate = timeScanDateDetailByMonthOfAccount.stream().filter(e -> e.getStatusWorkdays().equals(StatusWorkdayEnum.LATE.getCode())).count();

          Long totalDaysBackSoon = timeScanDateDetailByMonthOfAccount.stream().filter(e -> e.getStatusWorkdays().equals(StatusWorkdayEnum.BACK_SOON.getCode())).count();

          Long totalDaysRest = timeScanDateDetailByMonthOfAccount.stream().filter(e -> e.getStatusWorkdays().equals(StatusWorkdayEnum.REST.getCode())).count();
          // count total day work go late and back soon
          long totalDayGoLateAndBackSoon = timeScanDateDetailByMonthOfAccount.stream().filter(e -> e.getStatusWorkdays().equals(StatusWorkdayEnum.LATE_BACK_SOON.getCode())).count();

          // total work day of account =
          Double dayWork = timeScanDateDetailByMonthOfAccount.stream().mapToDouble(TimeScanDateDetailEntity::getNumberWorkday).sum();
          for (int i = 0; i < totalDayGoLateAndBackSoon; i++) {
            totalDaysLate++;
            totalDaysBackSoon++;
          }
          CalculationSalaryEntity calculationSalaryEntity = CalculationSalaryEntity.builder()
              .idEmployee(employee.getId())
              .accountEmployee(employee.getAccount())
              .monthWork(lastMonth)
              .totalDaysLate(totalDaysLate)
              .totalDaysBackSoon(totalDaysBackSoon)
              .totalDaysRest(totalDaysRest)
              .totalDaysEnough(totalDaysEnough)
              .totalDaysOffPermission(0L)
              .totalDaysOffNotPermission(totalDaysRest)
              .totalDaysOfMonth(totalDaysOfMonth)
              .workDays(dayWork)
              .totalSalaryOfMonth(calculationSalaryOfMonth(contractDetailEntity.getSalaryTotal(), dayWork, totalDaysOfMonth))
              .build();
          calculationSalaryRepository.save(calculationSalaryEntity);
          // save log calculation salary account
          LogCalculationSalaryEntity logCalculationSalary = new LogCalculationSalaryEntity();
          if (logCalculationSalaryEntity == null) {
            logCalculationSalary = LogCalculationSalaryEntity.builder()
                .account(account)
                .status(Boolean.TRUE)
                .monthWork(lastMonth)
                .build();
          } else if (Boolean.FALSE.equals(logCalculationSalaryEntity.getStatus())) {
            logCalculationSalary = LogCalculationSalaryEntity.builder()
                .id(logCalculationSalaryEntity.getId())
                .account(account)
                .status(Boolean.TRUE)
                .monthWork(lastMonth)
                .build();
          }
          logCalculationSalaryRepository.save(logCalculationSalary);
        }
      });

      LOGGER.error("[Logger of calculation salary :] ------>   Success !" );
    } catch (Exception e) {
      LOGGER.error("[Exception of calculation salary :]" + e.getMessage());
      return;
    }
  }
}
