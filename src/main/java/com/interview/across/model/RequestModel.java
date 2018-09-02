package com.interview.across.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.util.Map;

/**
 * @ author: Chenglong Li
 * @ date: 29/08/2018
 */
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class RequestModel {

  private Map<String, Object> site;
  private Map<String, Object> device;
  private Map<String, Object> user;

  public Map<String, Object> getSite() {
    return site;
  }

  public void setSite(Map<String, Object> site) {
    this.site = site;
  }

  public Map<String, Object> getDevice() {
    return device;
  }

  public void setDevice(Map<String, Object> device) {
    this.device = device;
  }

  public Map<String, Object> getUser() {
    return user;
  }

  public void setUser(Map<String, Object> user) {
    this.user = user;
  }


}
