package com.interview.across.controller;

import com.interview.across.exception.ApiErrorResponse;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ author: Chenglong Li
 * @ date: 29/08/2018
 */
@RequestMapping(produces = "application/json")
@RestController
public class CustomedErrorController implements ErrorController {


  @RequestMapping("/error")
  @ResponseBody
  public ApiErrorResponse request(HttpServletRequest req) {
    int statusCode = Integer
        .parseInt(req.getAttribute(RequestDispatcher.ERROR_STATUS_CODE).toString());
    String errorCode = "20" + statusCode + "000";
    String message = req.getAttribute(RequestDispatcher.ERROR_MESSAGE).toString();
    String exception = "";
    Object errorException = req.getAttribute(RequestDispatcher.ERROR_EXCEPTION);
    if (errorException != null) {
      exception = req.getAttribute(RequestDispatcher.ERROR_EXCEPTION).toString();
    } else {
      exception = "Page not found";
    }

    return new ApiErrorResponse(statusCode, errorCode, message, exception);
  }

  @Override
  public String getErrorPath() {
    return ("/error");
  }
}
