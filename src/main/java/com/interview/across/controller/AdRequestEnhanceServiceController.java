package com.interview.across.controller;

import com.interview.across.exception.BadRequestException;
import com.interview.across.exception.ErrorCode.BadRequest;
import com.interview.across.exception.ServiceException;
import com.interview.across.model.AdRequest;
import com.interview.across.service.AdRequestEnhanceService;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

  @Value("${geo.ipstack.url}")
  private String geoUrl;

  @Value("${geo.ipstack.access_key}")
  private String accessKey;

  @Autowired
  private AdRequestEnhanceService adRequestEnhanceService;

  @RequestMapping(value = {}, method = RequestMethod.POST)
  public AdRequest request(HttpServletRequest req, @RequestBody AdRequest model)
      throws ServiceException {

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

    // inject demographics
    CompletableFuture<AdRequest> demographicsCompletedFuture = adRequestEnhanceService
        .injectDemographics(model, siteId);

    // inject publisher
    CompletableFuture<AdRequest> publisherCompletedFuture = adRequestEnhanceService
        .injectPublisherDetail(model, siteId);

    //inject geo
    CompletableFuture<AdRequest> geoCompletedFuture = adRequestEnhanceService
        .injectGeo(model, deviceIp, geoUrl, accessKey);

    CompletableFuture
        .allOf(demographicsCompletedFuture, publisherCompletedFuture, geoCompletedFuture)
        .join();

    return model;

  }


}
