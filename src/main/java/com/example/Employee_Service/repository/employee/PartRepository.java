package com.example.Employee_Service.repository.employee;

import com.example.Employee_Service.model.entity.Part;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

public interface PartRepository extends CrudRepository<Part, Long> {


    @Modifying
//    @Transactional
    @Query(nativeQuery = true, value = "update part as p set p.name = :name where p.id = :id")
    void updateTotalMember(String name, Long id);
}
