package com.example.Employee_Service.model.dto.request.employee;

import com.the.common.validator.annotation.Required;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddNotificationRequest {
  @Required(message = "{id.received.is.required}")
  private Long idReceived;

  @Required(message = "{type.send.is.required}")
  private Integer typeNotification;
}
