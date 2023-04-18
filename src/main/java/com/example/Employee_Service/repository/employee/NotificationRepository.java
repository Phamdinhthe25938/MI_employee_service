package com.example.Employee_Service.repository.employee;

import com.example.Employee_Service.model.entity.employee.NotificationEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository("NotificationRepository")
public interface NotificationRepository extends CrudRepository<NotificationEntity, Long> {
}
