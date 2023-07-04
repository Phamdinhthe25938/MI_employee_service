package com.example.Employee_Service.enums;

public enum TypeLeaveEnum {
  ON_LEAVE(1, "ON_LEAVE"),
  COMPENSATE_LEAVE(2,"COMPENSATE_LEAVE");

  private Integer code;

  private String value;

  TypeLeaveEnum(Integer code, String value) {
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
