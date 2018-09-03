package com.interview.across.service.impl;

import com.interview.across.exception.ErrorCode.Internal;
import com.interview.across.exception.InternalException;
import com.interview.across.model.AdRequest;
import com.interview.across.service.AdRequestEnhanceService;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @ author: Chenglong Li
 * @ date: 29/08/2018
 */
@Service
public class AdRequestEnhanceServiceImpl implements AdRequestEnhanceService {


  /**
   * inject the demographics according to the given data
   */
  @Async
  @Override
  public CompletableFuture<AdRequest> injectDemographics(AdRequest model, String siteId)
      throws InternalException {

    RestTemplate template = new RestTemplate();
    Map<String, Object> site = model.getSite();
    String demographicsUrl = "http://159.89.185.155:3000/api/sites/" + siteId + "/demographics";

    try {
      Map<String, Object> demographics = template.getForObject(demographicsUrl, Map.class);
      Map<String, Object> demographicsDetail = (Map<String, Object>) demographics
          .get("demographics");
      site.put("demographics", demographicsDetail);
    } catch (Exception e) {
      throw new InternalException(Internal.OPERATION_FAILED,
          "External Request Error: Sometimes, bad things happen to good requests");
    }
    model.setSite(site);
    return CompletableFuture.completedFuture(model);
  }


  /**
   * inject the publisher details according to the given data
   */
  @Async
  @Override
  public CompletableFuture<AdRequest> injectPublisherDetail(AdRequest model, String siteId)
      throws InternalException {

    RestTemplate template = new RestTemplate();
    Map<String, Object> site = model.getSite();
    String publisherUrl = "http://159.89.185.155:3000/api/publishers/find";
    Map<String, Object> requestHead = new HashMap<>();
    Map<String, String> queryParameter = new HashMap<>();
    queryParameter.put("siteID", siteId);
    requestHead.put("q", queryParameter);

    try {
      Map<String, Object> publishers = template.postForObject(publisherUrl, requestHead, Map.class);
      Map<String, Object> publisherDetail = (Map<String, Object>) publishers.get("publisher");
      site.put("publisher", publisherDetail);
    } catch (Exception e) {
      throw new InternalException(Internal.OPERATION_FAILED,
          "External Request Error: Sometimes, bad things happen to good requests");
    }
    model.setSite(site);
    return CompletableFuture.completedFuture(model);
  }

  /**
   * inject geo informaiton according to ipstack api
   * @param model
   * @param deviceIp
   * @param geoUrl
   * @param accessKey
   * @return
   * @throws InternalException
   */
  @Async
  @Override
  public CompletableFuture<AdRequest> injectGeo(AdRequest model, String deviceIp,
      String geoUrl, String accessKey)
      throws InternalException {
    RestTemplate template = new RestTemplate();
    Map<String, Object> device = model.getDevice();
    if (geoUrl.charAt(geoUrl.length() - 1) != '/') {
      geoUrl = geoUrl + "/";
    }
    geoUrl = geoUrl + deviceIp + "?access_key=" + accessKey;

    try {
      Map<String, Object> geo = template.getForObject(geoUrl, Map.class);
      String countryCode = (String) geo.get("country_code");
      Map<String, Object> country = new HashMap<>();
      country.put("country", countryCode);
      device.put("geo", country);
      model.setDevice(device);
    } catch (Exception e) {
      throw new InternalException(Internal.OPERATION_FAILED,
          "External Request Error: Sometimes, bad things happen to good requests");
    }
    return CompletableFuture.completedFuture(model);
  }
}
