package com.example.Employee_Service.repository.employee;

import com.example.Employee_Service.model.entity.employee.PositionEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository("PositionRepository")
public interface PositionRepository extends CrudRepository<PositionEntity, Long> {
}
