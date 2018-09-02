package com.interview.across.exception;

/**
 * @ author: Chenglong Li
 * @ date: 02/09/2018
 */
public class InternalException extends ServiceException {

  public InternalException(ApiErrorResponse error) {
    super(error);
  }

  public InternalException(String code, String message) {
    super(500, code, message);
  }

  public InternalException(IError errorCode, Object... object) {
    super(500, errorCode, object);
  }
}
