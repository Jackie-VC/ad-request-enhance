package com.interview.across.controller;

import com.interview.across.exception.BadRequestException;
import com.interview.across.exception.ErrorCode.BadRequest;
import com.interview.across.exception.ErrorCode.Internal;
import com.interview.across.exception.InternalException;
import com.interview.across.exception.ServiceException;
import com.interview.across.model.RequestModel;
import com.interview.across.service.AdRequestEnhanceService;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

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
  public RequestModel request(
      HttpServletRequest req,
      @RequestBody RequestModel model) throws ServiceException {

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

    RestTemplate template = new RestTemplate();
    // inject demographics
    String demographicsUrl = "http://159.89.185.155:3000/api/sites/" + siteId + "/demographics";
    try {
      Map<String, Object> demographics = template.getForObject(demographicsUrl, Map.class);
      Map<String, Object> demographicsDetail = (Map<String, Object>) demographics
          .get("demographics");
      adRequestEnhanceService.injectDemographics(model, demographicsDetail);
    } catch (Exception e) {
      e.printStackTrace();
      System.out.println(Internal.OPERATION_FAILED);
      throw new InternalException(Internal.OPERATION_FAILED,
          "External Request Error: Sometimes, bad things happen to good requests");
    }

    //inject publisher detail
    String publisherUrl = "http://159.89.185.155:3000/api/publishers/find";
    Map<String, Object> requestHead = new HashMap<>();
    Map<String, String> queryParameter = new HashMap<>();
    queryParameter.put("siteID", siteId);
    requestHead.put("q", queryParameter);
    try {
      Map<String, Object> publishers = template.postForObject(publisherUrl, requestHead, Map.class);
      Map<String, Object> publisherDetail = (Map<String, Object>) publishers.get("publisher");
      adRequestEnhanceService.injectPublisherDetail(model, publisherDetail);
    } catch (Exception e) {
      e.printStackTrace();
      System.out.println(Internal.OPERATION_FAILED);
      throw new InternalException(Internal.OPERATION_FAILED,
          "External Request Error: Sometimes, bad things happen to good requests");
    }

    //inject geo with ipstack api
    if (geoUrl.charAt(geoUrl.length() - 1) != '/') {
      geoUrl = geoUrl + "/";
    }
    geoUrl = geoUrl + deviceIp + "?access_key=" + accessKey;
    try {
      Map<String, Object> geo = template.getForObject(geoUrl, Map.class);
      String countryCode = (String) geo.get("country_code");
      Map<String, Object> country = new HashMap<>();
      country.put("country", countryCode);
      adRequestEnhanceService.injectGeo(model, country);
    } catch (Exception e) {
      e.printStackTrace();
      System.out.println(Internal.OPERATION_FAILED);
      throw new InternalException(Internal.OPERATION_FAILED,
          "External Request Error: Sometimes, bad things happen to good requests");
    }
    return model;
  }


}
