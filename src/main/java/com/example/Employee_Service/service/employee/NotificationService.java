package com.example.Employee_Service.service.employee;

import com.example.Employee_Service.model.dto.request.employee.AddNotificationRequest;
import com.example.Employee_Service.model.entity.employee.NotificationEntity;
import com.example.Employee_Service.repository.employee.NotificationRepository;
import com.obys.common.model.payload.response.BaseResponse;
import com.obys.common.service.BaseService;
import com.obys.common.system_message.SystemMessageCode;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

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

  public BaseResponse<?> save(AddNotificationRequest request) {
    NotificationEntity entity = modelMapper.map(request, NotificationEntity.class);
    simpMessagingTemplate.convertAndSend("/topic/notification/" + request.getIdReceived());
    notificationRepository.save(entity);
    return responseV1(SystemMessageCode.CommonMessage.CODE_SUCCESS,
        SystemMessageCode.CommonMessage.SAVE_SUCCESS,
        entity);
  }
}
