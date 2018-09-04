package com.interview.across.service.impl;

import com.interview.across.exception.ErrorCode.Internal;
import com.interview.across.exception.InternalException;
import com.interview.across.model.AdRequestModel;
import com.interview.across.service.AdRequestEnhanceService;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @ author: Chenglong Li
 * @ date: 29/08/2018
 */
@Service
public class AdRequestEnhanceServiceImpl implements AdRequestEnhanceService {


  @Value("${geo.ipstack.url}")
  private String geoUrl;

  @Value("${geo.ipstack.access_key}")
  private String accessKey;


  /**
   * inject the demographics according to the given data
   */
  @Override
  public void injectDemographics(AdRequestModel model,
      Map<String, Object> demographics) {

    Map<String, Object> site = model.getSite();
    site.put("demographics", demographics);
    model.setSite(site);
  }


  /**
   * inject the publisher details according to the given data
   */
  @Override
  public void injectPublisherDetail(AdRequestModel model,
      Map<String, Object> publisherDetail) {

    Map<String, Object> site = model.getSite();
    site.put("publisher", publisherDetail);
    model.setSite(site);
  }

  /**
   * inject geo informaiton according to ipstack api
   */
  @Override
  public void injectGeo(AdRequestModel model, Map<String, Object> geo) {
    Map<String, Object> device = model.getDevice();
    String countryCode = (String) geo.get("country_code");
    Map<String, Object> country = new HashMap<>();
    country.put("country", countryCode);
    device.put("geo", country);
    model.setDevice(device);
  }

  /**
   * request demographics information asynchronously
   * @param model
   * @param siteId
   * @return
   * @throws InternalException
   */
  @Async
  @Override
  public CompletableFuture<Map<String, Object>> requestDemographics(AdRequestModel model,
      String siteId) throws InternalException {
    RestTemplate template = new RestTemplate();
    String demographicsUrl = "http://159.89.185.155:3000/api/sites/" + siteId + "/demographics";

    try {
      Map<String, Object> demographics = template.getForObject(demographicsUrl, Map.class);
      Map<String, Object> demographicsDetail = (Map<String, Object>) demographics
          .get("demographics");
      return CompletableFuture.completedFuture(demographicsDetail);
    } catch (Exception e) {
      throw new InternalException(Internal.OPERATION_FAILED,
          "External Request Error: Sometimes, bad things happen to good requests");
    }
  }

  /**
   * request publisher info asynchronously
   * @param model
   * @param siteId
   * @return
   * @throws InternalException
   */
  @Async
  @Override
  public CompletableFuture<Map<String, Object>> requestPublisherDetail(AdRequestModel model,
      String siteId) throws InternalException {
    RestTemplate template = new RestTemplate();
    String publisherUrl = "http://159.89.185.155:3000/api/publishers/find";
    Map<String, Object> requestHead = new HashMap<>();
    Map<String, String> queryParameter = new HashMap<>();
    queryParameter.put("siteID", siteId);
    requestHead.put("q", queryParameter);

    try {
      Map<String, Object> publishers = template.postForObject(publisherUrl, requestHead, Map.class);
      Map<String, Object> publisherDetail = (Map<String, Object>) publishers.get("publisher");
      return CompletableFuture.completedFuture(publisherDetail);
    } catch (Exception e) {
      throw new InternalException(Internal.OPERATION_FAILED,
          "External Request Error: Sometimes, bad things happen to good requests");
    }

  }

  /**
   * request geo info asynchronously
   * @param model
   * @param deviceIp
   * @return
   * @throws InternalException
   */
  @Async
  @Override
  public CompletableFuture<Map<String, Object>> requestGeoInfo(AdRequestModel model,
      String deviceIp)
      throws InternalException {
    RestTemplate template = new RestTemplate();
    Map<String, Object> geo;
    String geoUrlLocal = geoUrl;

    if (geoUrl.charAt(geoUrl.length() - 1) != '/') {
      geoUrlLocal = geoUrl + "/";
    }
    geoUrlLocal = geoUrlLocal + deviceIp + "?access_key=" + accessKey;
    try {
      geo = template.getForObject(geoUrlLocal, Map.class);
    } catch (Exception e) {
      throw new InternalException(Internal.OPERATION_FAILED,
          "External Request Error: Sometimes, bad things happen to good requests");
    }
    return CompletableFuture.completedFuture(geo);
  }
}
