package com.jackie.enhance.exception;

/**
 * @ author: Chenglong Li
 * @ date: 02/09/2018
 */
public class NotFoundException extends ServiceException {

  public NotFoundException(ApiErrorResponse error) {
    super(error);
  }

  public NotFoundException(String code, String message) {
    super(404, code, message);
  }

  public NotFoundException(IError errorCode, Object... object) {
    super(404, errorCode, object);
  }
}
