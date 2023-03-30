package com.example.Employee_Service.enums;

public enum StatusEmployeeEnum {
  WORKING(1, "WORKING"),

  DESIST(2, "DESIST");

  private int code;

  private String value;

  StatusEmployeeEnum(int code, String value) {
    this.code = code;
    this.value = value;
  }

  public int getCode() {
    return code;
  }

  public void setCode(int code) {
    this.code = code;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }
}
