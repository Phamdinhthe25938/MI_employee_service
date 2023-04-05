package com.example.Employee_Service.repository.employee;

import com.example.Employee_Service.model.entity.employee.EmployeeEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("EmployeeRepository")
public interface EmployeeRepository extends CrudRepository<EmployeeEntity, Long> {

  Optional<EmployeeEntity> findByCode(@Param("code") String code);

  @Query(nativeQuery = true, value = "select count(*) from employee where account like concat('%', :account, '%')")
  int countByAccount(@Param("account") String account);

  Optional<EmployeeEntity> findByAccount(@Param("account") String account);

  Optional<EmployeeEntity> findByEmailPersonal(@Param("emailPersonal") String emailPersonal);

  Optional<EmployeeEntity> findByEmailCompany(@Param("emailCompany") String emailCompany);

  Optional<EmployeeEntity> findByTelephone(@Param("telephone") String telephone);

  Optional<EmployeeEntity> findByNumberCCCD(@Param("numberCCCD") String numberCCCD);

  @Query(nativeQuery = true, value = "select account from employee where status_work = :statusWork order by id asc ")
  List<String> getAllAccountName(@Param("statusWork") int status);
}
