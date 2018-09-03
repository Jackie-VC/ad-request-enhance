package com.interview.across.controller;

import com.interview.across.exception.BadRequestException;
import com.interview.across.exception.ErrorCode.BadRequest;
import com.interview.across.exception.ServiceException;
import com.interview.across.model.AdRequestModel;
import com.interview.across.service.AdRequestEnhanceService;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ author: Chenglong Li
 * @ date: 29/08/2018
 */
@RequestMapping(value = "/RequestEnhance", produces = "application/json")
@RestController
public class AdRequestEnhanceServiceController {

  @Autowired
  private AdRequestEnhanceService adRequestEnhanceService;

  @RequestMapping(value = {}, method = RequestMethod.POST)
  public AdRequestModel request(HttpServletRequest req, @RequestBody AdRequestModel model)
      throws ServiceException, ExecutionException, InterruptedException {

    Map<String, Object> site = model.getSite();
    String sitePage = (String) site.get("page");
    String siteId = (String) site.get("id");
    Map<String, Object> device = model.getDevice();
    String deviceIp = (String) device.get("ip");

    // validate the required parameters
    if (StringUtils.isEmpty(siteId)) {
      throw new BadRequestException(BadRequest.MISSING_PARAMETER, "site.id");
    }
    if (StringUtils.isEmpty(sitePage)) {
      throw new BadRequestException(BadRequest.MISSING_PARAMETER, "site.page");
    }
    if (StringUtils.isEmpty(deviceIp)) {
      throw new BadRequestException(BadRequest.MISSING_PARAMETER, "device.ip");
    }

    // if it is not U.S. IP, return error message
    CompletableFuture<Map<String, Object>> geoCompletedFuture = adRequestEnhanceService
        .requestGeoInfo(model, deviceIp);
    Map<String, Object> geo = geoCompletedFuture.get();
    String countryCode = (String) geo.get("country_code");
    if (!"US".equals(countryCode)) {
      throw new BadRequestException(BadRequest.NOT_US_IP);
    }

    // request demographics
    CompletableFuture<Map<String, Object>> demographicsCompletedFuture = adRequestEnhanceService
        .requestDemographics(model, siteId);

    // request publisher
    CompletableFuture<Map<String, Object>> publisherCompletedFuture = adRequestEnhanceService
        .requestPublisherDetail(model, siteId);

    Map<String, Object> demographics = demographicsCompletedFuture.get();
    Map<String, Object> publisher = publisherCompletedFuture.get();
    adRequestEnhanceService.injectDemographics(model, demographics);
    adRequestEnhanceService.injectPublisherDetail(model, publisher);
    adRequestEnhanceService.injectGeo(model, geo);
    return model;
  }
}
