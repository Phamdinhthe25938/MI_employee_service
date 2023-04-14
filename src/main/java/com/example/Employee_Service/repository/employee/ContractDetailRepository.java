package com.example.Employee_Service.repository.employee;

import com.example.Employee_Service.model.entity.employee.ContractDetailEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("ContractDetailRepository")
public interface ContractDetailRepository extends CrudRepository<ContractDetailEntity, Long> {

  Optional<ContractDetailEntity> findByIdEmployee(Long id);

}
