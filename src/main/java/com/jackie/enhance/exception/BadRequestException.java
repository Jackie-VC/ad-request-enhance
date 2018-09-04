package com.interview.across.exception;

/**
 * @ author: Chenglong Li
 * @ date: 02/09/2018
 */
public class BadRequestException extends ServiceException {

  public BadRequestException(ApiErrorResponse error) {
    super(error);
  }

  public BadRequestException(String code, String message) {
    super(400, code, message);
  }

  public BadRequestException(IError errorCode, Object... object) {
    super(400, errorCode, object);
  }
}
