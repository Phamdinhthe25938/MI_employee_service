package com.example.Employee_Service.repository.employee;

import com.example.Employee_Service.model.entity.employee.LogCalculationSalaryEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository("LogCalculationSalaryRepository")
public interface LogCalculationSalaryRepository extends CrudRepository<LogCalculationSalaryEntity, Long> {


  Optional<LogCalculationSalaryEntity> findByAccountAndMonthWork(String account, LocalDate monthWork);
}
