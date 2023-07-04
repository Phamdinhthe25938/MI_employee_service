package com.example.Employee_Service.repository.employee;

import com.example.Employee_Service.model.entity.employee.LogVacationDayEntity;
import com.mysql.cj.log.Log;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Repository("LogVacationDayRepository")
public interface LogVacationDayRepository extends CrudRepository<LogVacationDayEntity, Long> {

  void deleteByIdAndUuidEmployee(Long id, String uuid);

  Page<LogVacationDayEntity> getAllByIdEmployeeAndUuidEmployee(Long idSend, String uuid, Pageable pageable);

  Page<LogVacationDayEntity> getAllByIdAssign(Long id, Pageable pageable);

  @Transactional
  @Modifying
  @Query(nativeQuery = true, value = "update log_vacation_day set status_approve = true where id = :id")
  void updateStatusApprove(Long id);

  @Query(nativeQuery = true, value = "select count(*) from log_vacation_day " +
      "where created_date >= :dateMonthFirst " +
      "and created_date <= :dateMonthEnd " +
      "and id_employee = :idEmployee " +
      "and status_approve = :statusArrpove")
  Long countDayPermission(LocalDate dateMonthFirst, LocalDate dateMonthEnd, Long idEmployee, Boolean statusApprove);
}
