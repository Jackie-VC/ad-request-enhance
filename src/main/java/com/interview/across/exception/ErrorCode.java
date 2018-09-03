package com.interview.across.exception;

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
    MISSING_PARAMETER("10400001", "Missing parameters: %s");

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

}
