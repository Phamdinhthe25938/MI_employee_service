package com.example.Employee_Service.batch_scheduler;

import com.example.Employee_Service.enums.StatusEmployeeEnum;
import com.example.Employee_Service.enums.StatusScanDetail;
import com.example.Employee_Service.enums.StatusWorkdayEnum;
import com.example.Employee_Service.enums.TypeScanEnum;
import com.example.Employee_Service.model.entity.employee.EmployeeEntity;
import com.example.Employee_Service.model.entity.time_scan_manager.LogTimeScanEntity;
import com.example.Employee_Service.model.entity.time_scan_manager.TimeScanEntity;
import com.example.Employee_Service.model.entity.time_scan_manager.TimeScanDetailEntity;
import com.example.Employee_Service.repository.employee.EmployeeRepository;
import com.example.Employee_Service.repository.time_scan_manager.LogTimeScanRepository;
import com.example.Employee_Service.repository.time_scan_manager.TimeScanDetailRepository;
import com.example.Employee_Service.repository.time_scan_manager.TimeScanRepository;
import com.example.Employee_Service.validate.employee.EmployeeValidator;
import com.obys.common.constant.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component("TimeScanBatch")
public class TimeScanBatch {
  private static final Logger LOGGER = LoggerFactory.getLogger(TimeScanBatch.class);
  @Resource
  @Qualifier("TimeScanRepository")
  private TimeScanRepository timeScanRepository;
  @Resource
  @Qualifier("EmployeeRepository")
  private EmployeeRepository employeeRepository;
  @Resource
  @Qualifier("TimeScanDetailRepository")
  private TimeScanDetailRepository timeScanDetailRepository;
  @Resource
  @Qualifier("EmployeeValidator")
  private EmployeeValidator employeeValidator;
  @Resource
  @Qualifier("LogTimeScanRepository")
  private LogTimeScanRepository logTimeScanRepository;

  @Scheduled(cron = "${time.scan.scheduler}")
  @Transactional(rollbackFor = Exception.class)
  public void timeScanBatch() {
    List<String> allAccount = employeeRepository.getAllAccountName(StatusEmployeeEnum.WORKING.getCode());
    LocalDate currentDate = LocalDate.now();
    LOGGER.info("Date now :" + currentDate);
    LocalDate yesterday = currentDate.minusDays(1);
    LOGGER.info("Date yesterday :" + yesterday);
    int day = yesterday.getDayOfMonth();
    int month = yesterday.getMonthValue();
    int year = yesterday.getYear();
    List<TimeScanEntity> timeScans = timeScanRepository.getAllByDateScanAndMonthScanAndYearScan(day, month, year);
    allAccount.forEach(account -> {
      Boolean checkLogTime = logTimeScanRepository.getStatusByDateAndAccount(yesterday, account);
      if (ObjectUtils.isEmpty(checkLogTime) || Boolean.FALSE.equals(checkLogTime)) {
        TimeScanDetailEntity timeScanDetail = null;
        List<TimeScanEntity> timeScansByAccount = timeScans.stream().filter(item -> item.getAccountEmployee().equals(account)).collect(Collectors.toList());
        EmployeeEntity employee = employeeValidator.accountEmployeeExist(account);
        if (!CollectionUtils.isEmpty(timeScansByAccount)) {
          LOGGER.info("Employee ---->  " + employee.toString());
          TimeScanEntity objectScanInMin = timeScansByAccount.stream()
              .filter(item -> TypeScanEnum.SCAN_IN.getCode().equals(item.getTypeScan()))
              .min(Comparator.comparing(TimeScanEntity::getTimeScan)).orElse(null);
          TimeScanEntity objectScanOutMax = timeScansByAccount.stream()
              .filter(item -> TypeScanEnum.SCAN_OUT.getCode().equals(item.getTypeScan()))
              .max(Comparator.comparing(TimeScanEntity::getTimeScan)).orElse(null);
          if (objectScanInMin != null && objectScanOutMax != null) {
            int statusWorkday;
            LocalTime localTimeMax = objectScanOutMax.getTimeScan().toLocalTime();
            LocalTime localTimeMin = objectScanInMin.getTimeScan().toLocalTime();
            if (localTimeMax.isBefore(Constants.TimeRegulations.WORK_END_TIME) && localTimeMin.isAfter(Constants.TimeRegulations.WORK_START_TIME)) {
              statusWorkday = StatusWorkdayEnum.LATE_BACK_SOON.getCode();
            } else if (localTimeMax.isBefore(Constants.TimeRegulations.WORK_END_TIME)) {
              statusWorkday = StatusWorkdayEnum.BACK_SOON.getCode();
            } else if (localTimeMin.isAfter(Constants.TimeRegulations.WORK_START_TIME)) {
              statusWorkday = StatusWorkdayEnum.LATE.getCode();
            } else {
              statusWorkday = StatusWorkdayEnum.ENOUGH.getCode();
            }
            if (objectScanOutMax.getTimeScan().isAfter(objectScanInMin.getTimeScan())) {
              Duration duration = Duration.between(objectScanInMin.getTimeScan(), objectScanOutMax.getTimeScan());
              long seconds = duration.getSeconds();
              long minutes = ChronoUnit.MINUTES.between(objectScanInMin.getTimeScan(), objectScanOutMax.getTimeScan());
              long hours = ChronoUnit.HOURS.between(objectScanInMin.getTimeScan(), objectScanOutMax.getTimeScan());
              long days = ChronoUnit.DAYS.between(objectScanInMin.getTimeScan(), objectScanOutMax.getTimeScan());
              double numberWorkday = hours >= 6 ? 1 : 0.5;
              LOGGER.info("Date nax " + objectScanOutMax.getTimeScan() + " Date min " + objectScanInMin.getTimeScan());
              LOGGER.info("Hour :" + hours + " Minutes :" + minutes + " Seconds :" + seconds + " Days :" + days);
              timeScanDetail = buildTimeScanDetailObject(employee, yesterday, hours, StatusScanDetail.VALID.getCode(), statusWorkday, numberWorkday);
            } else {
              timeScanDetail = buildTimeScanDetailObject(employee, yesterday, 0L, StatusScanDetail.IN_VALID.getCode(), statusWorkday, 0D);
            }
          } else if (objectScanOutMax == null && objectScanInMin != null) {
            timeScanDetail = buildTimeScanDetailObject(employee, yesterday, 0L, StatusScanDetail.IN_VALID.getCode(), StatusWorkdayEnum.BACK_SOON.getCode(), 0D);
          } else if (objectScanOutMax != null) {
            timeScanDetail = buildTimeScanDetailObject(employee, yesterday, 0L, StatusScanDetail.IN_VALID.getCode(), StatusWorkdayEnum.LATE.getCode(), 0D);
          }
        } else {
          timeScanDetail = buildTimeScanDetailObject(employee, yesterday, 0L, StatusScanDetail.IN_VALID.getCode(), StatusWorkdayEnum.REST.getCode(), 0D);
        }
        if (!ObjectUtils.isEmpty(timeScanDetail)) {
          timeScanDetailRepository.save(timeScanDetail);
        }
        LogTimeScanEntity logTimeScan = LogTimeScanEntity.builder().account(account).dateWork(yesterday).status(true).build();
        logTimeScanRepository.save(logTimeScan);
      }
    });
  }

  private TimeScanDetailEntity buildTimeScanDetailObject(EmployeeEntity employee, LocalDate dateWork, Long timeReality,
                                                         Integer statusScanWorkDay, Integer statusWorkDay, Double numberWorkday) {
    return TimeScanDetailEntity.builder()
        .accountEmployee(employee.getAccount())
        .codeEmployee(employee.getCode())
        .uuid(employee.getUuid())
        .dateWork(dateWork)
        .timeOffice(Constants.TIME_OFFICE)
        .timeReality(timeReality)
        .dayOfWeek(dateWork.getDayOfWeek().getValue())
        .statusScanWorkDay(statusScanWorkDay)
        .statusWorkdays(statusWorkDay)
        .numberWorkday(numberWorkday)
        .build();
  }
}
