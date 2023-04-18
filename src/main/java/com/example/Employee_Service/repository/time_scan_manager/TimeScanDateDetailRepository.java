package com.example.Employee_Service.repository.time_scan_manager;

import com.example.Employee_Service.model.entity.time_scan_manager.TimeScanDateDetailEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@Repository("TimeScanDateDetailRepository")
public interface TimeScanDateDetailRepository extends CrudRepository<TimeScanDateDetailEntity, Long> {

  @Query(nativeQuery = true, value = "" +
      "select * from time_scan_date_detail as tsdd " +
      "where tsdd.date_work >= :firstDayOfMonth " +
      "and tsdd.date_work <= :lastDayOfMonth ")
  List<TimeScanDateDetailEntity> getAllByMonth(LocalDate firstDayOfMonth, LocalDate lastDayOfMonth);

  List<TimeScanDateDetailEntity> findByIdEmployee(Long id);
}
