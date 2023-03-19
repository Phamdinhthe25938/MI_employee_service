package com.example.Employee_Service.repository.employee;

import com.example.Employee_Service.model.entity.Employee;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends CrudRepository<Employee, Long> {

    Optional<Employee> findByCode(@Param("code") String code);


    @Query(nativeQuery = true, value = "select count(*) from employee where account = :account")
    int countByAccount(@Param("account") String account);

    Optional<Employee> findByAccount(@Param("account") String account);
    Optional<Employee> findByEmail(@Param("email") String email);

    Optional<Employee> findByTelephone(@Param("telephone") String telephone);

    Optional<Employee> findByNumberCCCD(@Param("numberCCCD") String numberCCCD);
}
