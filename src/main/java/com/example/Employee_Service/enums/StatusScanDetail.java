package com.example.Employee_Service.enums;

public enum StatusScanDetail {

  VALID(1, "SCAN_VALID"),
  IN_VALID(2,"SCAN_IN_VALID");

  private Integer code;

  private String value;

  StatusScanDetail(Integer code, String value) {
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
