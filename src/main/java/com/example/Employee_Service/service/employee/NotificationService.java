package com.example.Employee_Service.service.employee;

import com.example.Employee_Service.model.dto.request.employee.AddNotificationRequest;
import com.example.Employee_Service.model.entity.employee.EmployeeEntity;
import com.example.Employee_Service.model.entity.employee.NotificationEntity;
import com.example.Employee_Service.repository.employee.NotificationRepository;
import com.example.Employee_Service.service.jwt.JWTService;
import com.example.Employee_Service.validate.employee.EmployeeValidator;
import com.the.common.constant.Constants;
import com.the.common.exception.ExceptionCommon;
import com.the.common.service.BaseService;
import com.the.common.websocket.Topic;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Service("NotificationService")
public class NotificationService extends BaseService {

  @Resource
  @Qualifier("NotificationRepository")
  private NotificationRepository notificationRepository;
  @Resource
  @Qualifier("ModelMapper")
  private ModelMapper modelMapper;
  @Resource
  private SimpMessagingTemplate simpMessagingTemplate;
  @Resource
  @Qualifier("EmployeeValidator")
  private EmployeeValidator employeeValidator;
  @Resource
  @Qualifier("JWTService")
  private JWTService jwtService;

  @Transactional(rollbackFor = Exception.class)
  public void save(AddNotificationRequest request, String authorization) {
    try {
      NotificationEntity entity = modelMapper.map(request, NotificationEntity.class);
      EmployeeEntity employeeSend = employeeValidator.accountEmployeeExist(jwtService.getSubjectFromToken(jwtService.getTokenFromAuthor(authorization)));
      EmployeeEntity employeeReceive = employeeValidator.employeeExist(request.getIdReceived());

      entity.setIdSend(employeeSend.getId());
      entity.setAccountSend(employeeSend.getAccount());
      entity.setAccountReceived(employeeReceive.getAccount());
      entity.setUuidReceived(employeeReceive.getUuid());
      List<String> titleAndContent = buildTitleAndContent(request.getTypeNotification(), employeeSend.getFullName());
      entity.setTitle(titleAndContent.get(0));
      entity.setContent(titleAndContent.get(1).replace("'",""));
      notificationRepository.save(entity);
      simpMessagingTemplate.convertAndSend(Topic.Notification.TOPIC_SEND_NOTIFICATION_LOG_VACATION + request.getIdReceived(), request);
    } catch (Exception e) {

      LOGGER.error("Exception from save notification --- > : " + e.getMessage());
      throw new ExceptionCommon(e.getMessage());
    }
  }

  private List<String> buildTitleAndContent(int type, String accountSend) {
    List<String> value = new ArrayList<>();
    switch (type) {
      case 1 :
        value = List.of(Constants.Notification.TITLE_LOG_VACATION, String.format(Constants.Notification.CONTENT_LOG_VACATION, accountSend));
        break;
      case 2 :
        break;
      default:
        LOGGER.error("Type send notification invalid !");
    }
    return value;
  }
}
