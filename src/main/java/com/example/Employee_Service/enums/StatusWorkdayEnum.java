package com.example.Employee_Service.enums;

public enum StatusWorkdayEnum {

  ENOUGH(1, "WORKING_ENOUGH"),
  LATE(2, "LATE"),
  BACK_SOON(3, "BACK_SOON"),
  LATE_BACK_SOON(4, "LATE_BACK_SOON"),

  REST(5, "REST");
  private Integer code;

  private String value;

  StatusWorkdayEnum(Integer code, String value) {
    this.code = code;
    this.value = value;
  }

  public Integer getCode() {
    return code;
  }

  public void setCode(Integer code) {
    this.code = code;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }
}
