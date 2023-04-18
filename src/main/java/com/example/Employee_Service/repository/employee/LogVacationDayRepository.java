package com.example.Employee_Service.repository.employee;

import com.example.Employee_Service.model.entity.employee.LogVacationDayEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository("LogVacationDayRepository")
public interface LogVacationDayRepository extends CrudRepository<LogVacationDayEntity, Long> {
}
