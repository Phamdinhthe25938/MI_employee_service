package com.example.Employee_Service.repository.time_scan_manager;

import com.example.Employee_Service.model.entity.time_scan_manager.TimeScan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("TimeScanRepository")
public interface TimeScanRepository extends JpaRepository<TimeScan, Long> {

  List<TimeScan> getAllByDateScanAndMonthScanAndYearScan(int date, int month, int year);
}
