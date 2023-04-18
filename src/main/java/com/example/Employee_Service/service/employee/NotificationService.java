package com.example.Employee_Service.service.employee;

import com.example.Employee_Service.repository.employee.NotificationRepository;
import com.obys.common.model.payload.response.BaseResponse;
import com.obys.common.service.BaseService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("NotificationService")
public class NotificationService extends BaseService {

  @Resource
  @Qualifier("NotificationRepository")
  private NotificationRepository notificationRepository;

//  public BaseResponse<?> save() {
//
//  }
}
