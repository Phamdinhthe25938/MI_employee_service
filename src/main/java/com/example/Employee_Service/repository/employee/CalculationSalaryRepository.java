package com.example.Employee_Service.repository.employee;

import com.example.Employee_Service.model.entity.employee.CalculationSalaryEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository("CalculationSalaryRepository")
public interface CalculationSalaryRepository extends CrudRepository<CalculationSalaryEntity, Long> {
}
