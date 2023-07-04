package com.example.Employee_Service.repository.employee;

import com.example.Employee_Service.model.entity.employee.VacationDayEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository("VacationDayRepository")
public interface VacationDayRepository extends CrudRepository<VacationDayEntity, Long> {

  @Transactional
  @Modifying
  @Query(nativeQuery = true, value = "update vacation_day set status = :status where id = :id")
  void updateStatus(Integer status, Long id);
}
