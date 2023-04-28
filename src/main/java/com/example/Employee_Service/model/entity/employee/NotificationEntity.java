package com.example.Employee_Service.model.entity.employee;

import com.the.common.model.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "notification")
public class NotificationEntity extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;
  @NotNull
  @Column(name = "id_send")
  private Long idSend;
  @NotNull
  @Column(name = "account_send")
  private String accountSend;
  @NotNull
  @Column(name = "id_received")
  private Long idReceived;
  @NotNull
  @Column(name = "account_received")
  private String accountReceived;
  @NotNull
  @Column(name = "uuid_received")
  private String uuidReceived;
  @NotNull
  @Column(name = "type_notification")
  private Integer typeNotification;
  @NotNull
  @Column(name = "title")
  private String title;
  @NotNull
  @Column(name = "content")
  private String content;
}
