package com.example.Employee_Service;

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
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.TimeZone;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
@EnableScheduling
public class EmployeeServiceApplication {
  private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeServiceApplication.class);
  public static void main(String[] args) {
    SpringApplication.run(EmployeeServiceApplication.class, args);
//    System.loadLibrary("opencv_java470");
//    // Load the training image
//    LOGGER.info("Time start training " + LocalDateTime.now());
//    Mat image = Imgcodecs.imread("image/lee_jong_suk.jpg");
//
//    // Load the pre-trained Haar Cascade classifier for detecting people
//    String cascadeFile = "image/haarcascade_frontalface_alt.xml";
//    CascadeClassifier detector = new CascadeClassifier(cascadeFile);
//    if (detector.empty()) {
//      LOGGER.info("detector.getNativeObjAddr() --->" + detector.getNativeObjAddr());
//      LOGGER.error("CascadeClassifier is empty !");
////      return;
//    }
//    // 2942873858752
//    // Detect people in the image
//    Mat gray = new Mat();
//    Imgproc.cvtColor(image, gray, Imgproc.COLOR_BGR2GRAY);
//    MatOfRect detections = new MatOfRect();
//    detector.detectMultiScale(gray, detections);
//
//    // Draw rectangles around the detected people
//    for (Rect rect : detections.toArray()) {
//      Imgproc.rectangle(image, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 255, 0), 2);
//    }
//
//    // Show the output image
//    HighGui.imshow("People Detection", image);
//    HighGui.waitKey();
  }
}
