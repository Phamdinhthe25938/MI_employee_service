package com.example.Employee_Service.repository.employee;

import com.example.Employee_Service.model.entity.employee.Part;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
@Repository
public interface PartRepository extends JpaRepository<Part, Long> {

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "update part as p set p.total_member = p.total_member + 1 where p.id = :id")
    void updateTotalMember(Long id);
}
