package com.example.Employee_Service.exception;

import com.the.common.exception.ErrorV1Exception;
import com.the.common.exception.ErrorV2Exception;
import com.the.common.exception.ExceptionCommon;
import com.the.common.exception.HasErrorException;
import com.the.common.service.BaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * The type Handler exception.
 */
@RestControllerAdvice
public class GlobalHandleException extends BaseService {

  private static final Logger LOGGER = LoggerFactory.getLogger(GlobalHandleException.class);

  @ExceptionHandler(ErrorV1Exception.class)
  public ResponseEntity<?> handleErrorV1Exception(ErrorV1Exception e) {
    String[] messageRes = e.getMessage().split("<-->");
    String code = messageRes[0];
    String message = messageRes[1];
    return ResponseEntity.ok(responseV1(code, message, null));
  }

  @ExceptionHandler(ErrorV2Exception.class)
  public ResponseEntity<?> handleErrorV2Exception(ErrorV2Exception e) {
    String[] messageRes = e.getMessage().split("<-->");
    String code = messageRes[0];
    String field = getMessage(messageRes[1]);
    String message = getMessage(messageRes[2]);
    return ResponseEntity.ok(responseV2(code, field + " " + message, null));
  }


  @ExceptionHandler(HasErrorException.class)
  public ResponseEntity<?> handleHasErrorException(HasErrorException e) {
    String[] messageRes = e.getMessage().split("<-->");
    String code = messageRes[0];
    String message = messageRes[1];
    return ResponseEntity.ok(responseV2(code, message, null));
  }

  @ExceptionHandler(ExceptionCommon.class)
  public ResponseEntity<?> handleErrorExceptionCommon(ExceptionCommon e) {
    return ResponseEntity.ok(responseV2("Code_exception_common", e.getMessage(), null));
  }
}
