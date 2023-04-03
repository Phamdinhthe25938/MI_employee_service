package com.example.Employee_Service.batch_scheduler;

import com.example.Employee_Service.enums.StatusEmployeeEnum;
import com.example.Employee_Service.enums.StatusScanDetail;
import com.example.Employee_Service.enums.StatusWorkdayEnum;
import com.example.Employee_Service.enums.TypeScanEnum;
import com.example.Employee_Service.model.entity.employee.Employee;
import com.example.Employee_Service.model.entity.time_scan_manager.TimeScan;
import com.example.Employee_Service.model.entity.time_scan_manager.TimeScanDetail;
import com.example.Employee_Service.repository.employee.EmployeeRepository;
import com.example.Employee_Service.repository.time_scan_manager.TimeScanDetailRepository;
import com.example.Employee_Service.repository.time_scan_manager.TimeScanRepository;
import com.example.Employee_Service.validate.employee.EmployeeValidator;
import com.obys.common.constant.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
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

  @Scheduled(cron = "${time.scan.scheduler}")
  public void timeScanBatch() {
    List<String> allAccount = employeeRepository.getAllAccountName(StatusEmployeeEnum.WORKING.getCode());
    LocalDate currentDate = LocalDate.now();
    LocalDate yesterday = currentDate.minusDays(1);
    int day = yesterday.getDayOfMonth();
    int month = yesterday.getMonthValue();
    int year = yesterday.getYear();
    List<TimeScan> timeScans = timeScanRepository.getAllByDateScanAndMonthScanAndYearScan(day, month, year);
    allAccount.forEach(account -> {
      List<TimeScan> timeScansByAccount = timeScans.stream().filter(item -> item.getAccountEmployee().equals(account)).collect(Collectors.toList());
      if (!CollectionUtils.isEmpty(timeScansByAccount)) {
        Employee employee = employeeValidator.accountEmployeeExist(account);
        LOGGER.info("Employee ---->  " + employee.toString());
        TimeScanDetail timeScanDetail;
        TimeScan objectScanInMin = timeScansByAccount.stream()
            .filter(item -> TypeScanEnum.SCAN_IN.getCode().equals(item.getTypeScan()))
            .min(Comparator.comparing(TimeScan::getTimeScan)).orElse(null);
        TimeScan objectScanOutMax = timeScansByAccount.stream()
            .filter(item -> TypeScanEnum.SCAN_OUT.getCode().equals(item.getTypeScan()))
            .max(Comparator.comparing(TimeScan::getTimeScan)).orElse(null);
        if (objectScanInMin != null && objectScanOutMax != null) {
          int statusWorkday;
          LocalTime localTimeMax = objectScanOutMax.getTimeScan().toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
          LocalTime localTimeMin = objectScanInMin.getTimeScan().toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
          if (localTimeMax.isBefore(Constants.TimeRegulations.WORK_END_TIME) && localTimeMin.isAfter(Constants.TimeRegulations.WORK_START_TIME)) {
            statusWorkday = StatusWorkdayEnum.LATE_BACK_SOON.getCode();
          } else if (localTimeMax.isBefore(Constants.TimeRegulations.WORK_END_TIME)) {
            statusWorkday = StatusWorkdayEnum.BACK_SOON.getCode();
          } else if (localTimeMin.isAfter(Constants.TimeRegulations.WORK_START_TIME)) {
            statusWorkday = StatusWorkdayEnum.LATE.getCode();
          } else {
            statusWorkday = StatusWorkdayEnum.ENOUGH.getCode();
          }
          if (objectScanOutMax.getTimeScan().after(objectScanInMin.getTimeScan())) {
            long durationMillis = objectScanOutMax.getTimeScan().getTime() - objectScanInMin.getTimeScan().getTime();
            long hours = TimeUnit.MILLISECONDS.toHours(durationMillis);
            long minutes = TimeUnit.MILLISECONDS.toMinutes(durationMillis) - TimeUnit.HOURS.toMinutes(hours);
            long seconds = TimeUnit.MILLISECONDS.toSeconds(durationMillis) - TimeUnit.HOURS.toSeconds(hours) - TimeUnit.MINUTES.toSeconds(minutes);
            double numberWorkday = hours >= 6 ? 1 : 0.5;
            LOGGER.info("Date nax " + objectScanOutMax.getTimeScan() + " Date min " + objectScanInMin.getTimeScan());
            LOGGER.info("Hour :" + hours + " Minutes :" + minutes + " Seconds :" + seconds);
            timeScanDetail = buildTimeScanDetailObject(employee, objectScanOutMax.getTimeScan(), hours, StatusScanDetail.VALID.getCode(), statusWorkday, numberWorkday);
          } else {
            timeScanDetail = buildTimeScanDetailObject(employee, objectScanOutMax.getTimeScan(), 0L, StatusScanDetail.IN_VALID.getCode(), statusWorkday, 0D);
          }
        } else if (objectScanOutMax == null && objectScanInMin != null) {
          timeScanDetail = buildTimeScanDetailObject(employee, objectScanInMin.getTimeScan(), 0L, StatusScanDetail.IN_VALID.getCode(), StatusWorkdayEnum.BACK_SOON.getCode(), 0D);
        } else if (objectScanOutMax != null) {
          timeScanDetail = buildTimeScanDetailObject(employee, objectScanOutMax.getTimeScan(), 0L, StatusScanDetail.IN_VALID.getCode(), StatusWorkdayEnum.LATE.getCode(), 0D);
        } else {
          timeScanDetail = buildTimeScanDetailObject(employee, null, 0L, StatusScanDetail.IN_VALID.getCode(), StatusWorkdayEnum.REST.getCode(), 0D);
        }
        timeScanDetailRepository.save(timeScanDetail);
      }
    });
  }

  private TimeScanDetail buildTimeScanDetailObject(Employee employee, Date scanTime, Long timeReality,
                                                   Integer statusScanWorkDay, Integer statusWorkDay, Double numberWorkday) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(scanTime);
    return TimeScanDetail.builder()
        .accountEmployee(employee.getAccount())
        .codeEmployee(employee.getCode())
        .uuid(employee.getUuid())
        .dateWork(scanTime)
        .timeOffice(Constants.TIME_OFFICE)
        .timeReality(timeReality)
        .dayOfWeek(calendar.get(Calendar.DAY_OF_WEEK))
        .statusScanWorkDay(statusScanWorkDay)
        .statusWorkdays(statusWorkDay)
        .numberWorkday(numberWorkday)
        .build();
  }
}
