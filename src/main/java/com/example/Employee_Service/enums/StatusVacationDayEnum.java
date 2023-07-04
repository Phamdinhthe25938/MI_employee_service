package com.example.Employee_Service.enums;

public enum StatusVacationDayEnum {
  NOT_SELECT(1),
  SELECTED(2),
  APPROVED(3);
  private int code;

  StatusVacationDayEnum(int code) {
    this.code = code;
  }
  public int getCode() {
    return code;
  }

  public void setCode(int code) {
    this.code = code;
  }

}
