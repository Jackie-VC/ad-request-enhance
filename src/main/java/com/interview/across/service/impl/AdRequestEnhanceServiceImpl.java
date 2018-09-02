package com.interview.across.service.impl;

import com.interview.across.model.RequestModel;
import com.interview.across.service.AdRequestEnhanceService;
import java.util.Map;
import org.springframework.stereotype.Service;

/**
 * @ author: Chenglong Li
 * @ date: 29/08/2018
 */
@Service
public class AdRequestEnhanceServiceImpl implements AdRequestEnhanceService{


  /**
   * inject the demographics according to the given data
   * @param model
   * @param demographics
   * @return
   */
  @Override
  public void injectDemographics(RequestModel model,
      Map<String, Object> demographics) {
    Map<String, Object> site = model.getSite();
    site.put("demographics", demographics);
    model.setSite(site);
  }


  /**
   * inject the publisher details according to the given data
   * @param model
   * @param publisherDetail
   * @return
   */
  @Override
  public void injectPublisherDetail(RequestModel model,
      Map<String, Object> publisherDetail) {
    Map<String, Object> site = model.getSite();
    site.put("publisher", publisherDetail);
    model.setSite(site);
  }

  @Override
  public void injectGeo(RequestModel model, Map<String, Object> geo) {
    Map<String, Object> device = model.getDevice();
    device.put("geo", geo);
    model.setDevice(device);
  }
}
