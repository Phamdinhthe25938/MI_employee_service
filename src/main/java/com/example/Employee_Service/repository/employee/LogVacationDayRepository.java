package com.example.Employee_Service.repository.employee;

import com.example.Employee_Service.model.entity.employee.LogVacationDayEntity;
import com.mysql.cj.log.Log;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("LogVacationDayRepository")
public interface LogVacationDayRepository extends CrudRepository<LogVacationDayEntity, Long> {

  void deleteByIdAndUuidEmployee(Long id, String uuid);

  Page<LogVacationDayEntity> getAllByIdEmployeeAndUuidEmployee(Long idSend, String uuid, Pageable pageable);

  Page<LogVacationDayEntity> getAllByIdAssign(Long id, Pageable pageable);
}
