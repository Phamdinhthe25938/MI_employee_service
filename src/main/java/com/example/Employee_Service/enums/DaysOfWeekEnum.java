package com.example.Employee_Service.enums;

public enum DaysOfWeekEnum {

  SUNDAY(1, "SUNDAY"),
  MONDAY(2, "MONDAY"),
  TUESDAY(3, "TUESDAY"),
  WEDNESDAY(4, "WEDNESDAY"),
  THURSDAY(5, "THURSDAY"),
  FRIDAY(6, "FRIDAY"),
  SATURDAY(7, "SATURDAY");

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
