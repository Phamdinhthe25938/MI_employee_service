package com.example.Employee_Service.enums;

public enum TypeLogVacationDayEnum {

  MORNING(1,"MORNING"),
  AFTERNOON(2,"AFTERNOON"),
  ENOUGH(3,"ENOUGH");
  private int code;

  private String value;

  TypeLogVacationDayEnum(int code, String value) {
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
