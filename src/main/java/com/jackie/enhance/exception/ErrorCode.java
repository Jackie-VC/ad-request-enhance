package com.jackie.enhance.exception;

/**
 * @ author: Chenglong Li
 * @ date: 01/09/2018
 */
public enum ErrorCode {
  ;

  public enum Internal implements IError {

    OPERATION_FAILED("10500002", "Operation failed. %s"),
    OPERATION_FAILED_DEFAULT("10500001",
        "Operation failed. Please contact the service provider for help");

    private String code;
    private String message;

    Internal(String code, String message) {
      this.code = code;
      this.message = message;
    }

    @Override
    public String getCode() {
      return code;
    }

    @Override
    public String getMessage() {
      return message;
    }
  }

  public enum BadRequest implements IError {
    MISSING_PARAMETER("10400001", "Missing parameters: %s"),
    NOT_US_IP("10400002", "The ip is not in U.S.");

    private String code;
    private String message;

    BadRequest(String code, String message) {
      this.code = code;
      this.message = message;
    }

    @Override
    public String getCode() {
      return code;
    }

    @Override
    public String getMessage() {
      return message;
    }
  }

  public enum NotFound implements IError {
    PAGE_NOT_FOUND("10400000", "Page not found"),
    PUBLISHER_ID("10400001", "Publisher id is not found"),
    GEO_INFORMATION("10400002", "Geo information is not found"),
    DEMOGRAPHICS("10400003", "Demographics information is not found");

    private String code;
    private String message;

    NotFound(String code, String message) {
      this.code = code;
      this.message = message;
    }

    @Override
    public String getCode() {
      return code;
    }

    @Override
    public String getMessage() {
      return message;
    }
  }

}
