package com.example.Employee_Service.enums;

public enum DaysOfWeekEnum {

  SUNDAY(0, "SUNDAY"),
  MONDAY(1, "MONDAY"),
  TUESDAY(2, "TUESDAY"),
  WEDNESDAY(3, "WEDNESDAY"),
  THURSDAY(4, "THURSDAY"),
  FRIDAY(5, "FRIDAY"),
  SATURDAY(6, "SATURDAY");

  private int code;

  private String value;

  DaysOfWeekEnum(int code, String value) {
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
