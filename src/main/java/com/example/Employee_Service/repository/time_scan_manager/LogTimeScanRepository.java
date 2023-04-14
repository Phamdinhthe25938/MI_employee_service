package com.example.Employee_Service.repository.time_scan_manager;

import com.example.Employee_Service.model.entity.time_scan_manager.LogTimeScanEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository("LogTimeScanRepository")
public interface LogTimeScanRepository extends CrudRepository<LogTimeScanEntity, Long> {

  @Query(nativeQuery = true, value = "select status from log_time_scan where date_work = :dateWork and account = :account")
  Boolean getStatusByDateAndAccount(@Param("dateWork") LocalDate dateWork, @Param("account") String account);

  Optional<LogTimeScanEntity> findByAccountAndDateWork(String account, LocalDate dateWork);
}
