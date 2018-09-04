package com.jackie.enhance.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.io.Serializable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @ author: Chenglong Li
 * @ date: 01/09/2018
 */
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ApiErrorResponse implements Serializable {

  private static Logger LOG = LoggerFactory
      .getLogger(ApiErrorResponse.class);

  /**
   * http status code
   */
  public Integer status;
  /**
   * error code
   */
  public String code;
  /**
   * simple error message
   */
  public String message;
  /**
   * detail error message
   */
  public String moreInfo;

  // getters and setters
  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public String getMoreInfo() {
    return moreInfo;
  }

  public void setMoreInfo(String moreInfo) {
    this.moreInfo = moreInfo;
  }

  public ApiErrorResponse() {
  }

  public ApiErrorResponse(Integer status, String code, String message,
      String moreInfo) {
    this.status = status;
    this.code = code;
    this.message = message;
    this.moreInfo = moreInfo;
  }

  public String toString() {
    ObjectMapper mapper = new ObjectMapper();
    try {
      return mapper.writeValueAsString(this);
    } catch (JsonProcessingException e) {
      LOG.error("parse json string error: {}", e);
      return null;
    }
  }
}
