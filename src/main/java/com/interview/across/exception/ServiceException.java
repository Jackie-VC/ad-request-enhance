package com.interview.across.exception;

/**
 * @ author: Chenglong Li
 * @ date: 01/09/2018
 */
public class ServiceException extends Exception {

  private static final long serialVersionUID = 1L;

  private int statusCode;
  private String code;
  private String message;

  public ServiceException(ApiErrorResponse error) {
    super(error.getMessage());
    this.statusCode = error.getStatus();
    this.code = error.getCode();
    this.message = error.getMessage();
  }

  public ServiceException(int statusCode, String code, String message) {
    super(message);
    this.statusCode = statusCode;
    this.code = code;
    this.message = message;
  }

  public ServiceException(int statusCode, IError errorCode, Object... object) {
    this(statusCode, errorCode.getCode(), String.format(errorCode.getMessage(), object));
  }

  public ApiErrorResponse getErrorResponse(String moreInfo) {
    return new ApiErrorResponse(null, code, message, moreInfo);
  }

  public int getStatusCode() {
    return statusCode;
  }

  public String getCode() {
    return code;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

}