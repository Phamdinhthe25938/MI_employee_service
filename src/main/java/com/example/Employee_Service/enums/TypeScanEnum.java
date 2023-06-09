package com.example.Employee_Service.enums;

public enum TypeScanEnum {

  SCAN_IN(1, "SCAN_IN"),

  SCAN_OUT(2, "SCAN_OUT");

  private Integer code;

  private String value;

  TypeScanEnum(Integer code, String value) {
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
