package com.example.Employee_Service;

import com.example.Employee_Service.config.web.ApplicationContextProvider;
import com.example.Employee_Service.model.entity.employee.PartEntity;
import com.the.common.constant.kafka.KafkaTopic;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.opencv.core.*;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ApplicationContext;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.TimeZone;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
@EnableScheduling
@EnableAsync
public class EmployeeServiceApplication {
  private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeServiceApplication.class);

  public static void main(String[] args) throws Exception {
    SpringApplication.run(EmployeeServiceApplication.class, args);
    ApplicationContext application = ApplicationContextProvider.getApplicationContext();
    KafkaTemplate<String, String> kafkaTemplate = (KafkaTemplate<String, String>) application.getBean("KafkaTemplate");
    ProducerRecord<String, String> record = new ProducerRecord<>("my-topic-1", 2, "123", "ricky start 1");
    kafkaTemplate.send(record);
    LOGGER.info("Send message 1 success okkkkkkkkkkkkkkkkkkkkkkkkkkkk!");
    Thread.sleep(5000);
    ProducerRecord<String, String> record2 = new ProducerRecord<>("my-topic-1", "ricky start 2");
    kafkaTemplate.send("my-topic-1", "ricky start 2");
  }

  @KafkaListener(topics = "my-topic-1")
  public void ok(ConsumerRecord<String, String> record) {
//    if (record.offset() >= 21 && record.offset() <= 23) {
    System.out.println("Partition log : " + record.partition() + " " + record.key());
    System.out.println("Received message: " + record.value());
//    }
  }
}
