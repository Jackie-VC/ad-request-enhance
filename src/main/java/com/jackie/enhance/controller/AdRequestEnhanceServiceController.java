package com.interview.across.controller;

import com.interview.across.exception.BadRequestException;
import com.interview.across.exception.ErrorCode.BadRequest;
import com.interview.across.exception.ErrorCode.NotFound;
import com.interview.across.exception.NotFoundException;
import com.interview.across.exception.ServiceException;
import com.interview.across.model.AdRequestModel;
import com.interview.across.service.AdRequestEnhanceService;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ author: Chenglong Li
 * @ date: 29/08/2018
 */
@RequestMapping(value = "/RequestEnhance", produces = "application/json")
@RestController
public class AdRequestEnhanceServiceController {

  private final AdRequestEnhanceService adRequestEnhanceService;

  private static ConcurrentHashMap<String, Map<String, Object>> demographicsStore = new ConcurrentHashMap<>();
  private static ConcurrentHashMap<String, Map<String, Object>> publisherStore = new ConcurrentHashMap<>();
  private static ConcurrentHashMap<String, String> geoStore = new ConcurrentHashMap<>();
  private static Map<String, Long> demographicsRefreshTimeStore = new HashMap<>();

  @Autowired
  public AdRequestEnhanceServiceController(AdRequestEnhanceService adRequestEnhanceService) {
    this.adRequestEnhanceService = adRequestEnhanceService;
  }

  @RequestMapping(value = {}, method = RequestMethod.POST)
  public AdRequestModel request(HttpServletRequest req, @RequestBody AdRequestModel model,
      @RequestParam(defaultValue = "true") boolean injectDemographics,
      @RequestParam(defaultValue = "true") boolean injectPublisher,
      @RequestParam(defaultValue = "true") boolean injectGeo,
      @RequestParam(defaultValue = "false") boolean refreshPublisher)
      throws ServiceException, ExecutionException, InterruptedException {

    Map<String, Object> site = model.getSite();
    if (site == null) {
      throw new BadRequestException(BadRequest.MISSING_PARAMETER, "site");
    }
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
    CompletableFuture<Map<String, Object>> geoCompletedFuture;
    String countryCode = geoStore.get(deviceIp);
    Map<String, Object> geo = new HashMap<>();
    //check if it is existed in cache, if no cache, get API data
    if (StringUtils.isEmpty(countryCode)) {
      geoCompletedFuture = adRequestEnhanceService
          .requestGeoInfo(model, deviceIp);
      geo = geoCompletedFuture.get();
      if (geo == null) {
        throw new NotFoundException(NotFound.GEO_INFORMATION);
      }
      countryCode = (String) geo.get("country_code");
      geoStore.put(deviceIp, countryCode);
    }
    if (!"US".equals(countryCode)) {
      throw new BadRequestException(BadRequest.NOT_US_IP);
    }

    // request publisher
    CompletableFuture<Map<String, Object>> publisherCompletedFuture = adRequestEnhanceService
        .requestPublisherDetail(model, siteId);
    // request demographics
    CompletableFuture<Map<String, Object>> demographicsCompletedFuture = adRequestEnhanceService
        .requestDemographics(model, siteId);

    // if need to inject publisher, check the cache, if no cache, get API data
    if (injectPublisher) {
      Map<String, Object> publisher = publisherStore.get(siteId);
      if (StringUtils.isEmpty(publisher) || refreshPublisher) {
        publisher = publisherCompletedFuture.get();
        // if publisher id can not find, return error message
        if (publisher == null || publisher.get("id") == null) {
          throw new NotFoundException(NotFound.PUBLISHER_ID);
        }
      }
      publisherStore.put(siteId, publisher);
      adRequestEnhanceService.injectPublisherDetail(model, publisher);
    }

    // if need inject demographics, check the cache, if no cache, get API data
    if (injectDemographics) {
      Map<String, Object> demographics = demographicsStore.get(siteId);
      Long lastRefresh = demographicsRefreshTimeStore.get(siteId);
      Long now = System.currentTimeMillis();
      if (StringUtils.isEmpty(demographics) || StringUtils.isEmpty(lastRefresh)
          || (now - lastRefresh) / 3600 / 24 >= 6) {
        demographics = demographicsCompletedFuture.get();
        if (demographics == null) {
          throw new NotFoundException(NotFound.DEMOGRAPHICS);
        }
      }
      demographicsStore.put(siteId, demographics);
      demographicsRefreshTimeStore.put(siteId, now);
      adRequestEnhanceService.injectDemographics(model, demographics);
    }

    // if need inject geo
    if (injectGeo) {
      adRequestEnhanceService.injectGeo(model, geo);
    }
    return model;
  }
}
