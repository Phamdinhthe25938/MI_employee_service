package com.example.Employee_Service.repository.employee;

import com.example.Employee_Service.model.entity.employee.ContractDetailEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository("ContractDetailRepository")
public interface ContractDetailRepository extends CrudRepository<ContractDetailEntity, Long> {
}
