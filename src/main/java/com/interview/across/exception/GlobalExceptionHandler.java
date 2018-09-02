package com.interview.across.exception;

import static org.springframework.core.annotation.AnnotatedElementUtils.findMergedAnnotation;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @ author: Chenglong Li
 * @ date: 01/09/2018
 */
@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(InternalException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public @ResponseBody
  ApiErrorResponse handleInternalException(final InternalException exception,
      final HttpServletRequest request, final HttpServletResponse response) {

    ApiErrorResponse error = new ApiErrorResponse();
    error.setMessage(exception.getMessage());
    error.setCode(exception.getCode());
    error.setStatus(exception.getStatusCode());

    return error;
  }

  @ExceptionHandler(BadRequestException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public @ResponseBody
  ApiErrorResponse handleBadRequestException(final BadRequestException exception,
      final HttpServletRequest request, final HttpServletResponse response) {

    ApiErrorResponse error = new ApiErrorResponse();
    error.setMessage(exception.getMessage());
    error.setCode(exception.getCode());
    error.setStatus(exception.getStatusCode());

    return error;
  }

  HttpStatus resolveAnnotatedResponseStatus(Exception exception) {
    ResponseStatus annotation = findMergedAnnotation(Exception.class, ResponseStatus.class);
    if (annotation != null) {
      return annotation.value();
    }
    return HttpStatus.INTERNAL_SERVER_ERROR;
  }

}
