package com.example.Employee_Service.batch_scheduler;

import com.example.Employee_Service.enums.StatusEmployeeEnum;
import com.example.Employee_Service.enums.TypeScanEnum;
import com.example.Employee_Service.model.entity.time_scan_manager.TimeScan;
import com.example.Employee_Service.repository.employee.EmployeeRepository;
import com.example.Employee_Service.repository.time_scan_manager.TimeScanRepository;
import com.example.Employee_Service.service.employee.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDate;
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
      if(!CollectionUtils.isEmpty(timeScansByAccount)) {
        if (timeScansByAccount.size() > 1) {
          TimeScan dateMax = timeScansByAccount.stream()
              .filter(item -> TypeScanEnum.SCAN_OUT.getCode().equals(item.getTypeScan()))
              .max(Comparator.comparing(TimeScan::getTimeScan)).orElse(null);
          TimeScan dateMin = timeScansByAccount.stream()
              .filter(item -> TypeScanEnum.SCAN_IN.getCode().equals(item.getTypeScan()))
              .min(Comparator.comparing(TimeScan::getTimeScan)).orElse(null);
          LOGGER.info("DATE MAX -----> " + dateMax);
          LOGGER.info("DATE MIN -----> " + dateMin);
        }
      }
    });
  }
}
