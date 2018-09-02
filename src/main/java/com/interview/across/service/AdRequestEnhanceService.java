package com.interview.across.service;

import com.interview.across.model.RequestModel;
import java.util.Map;

/**
 * @ author: Chenglong Li
 * @ date: 29/08/2018
 */
public interface AdRequestEnhanceService {

  void injectDemographics(RequestModel requestModel, Map<String, Object> demographics);

  void injectPublisherDetail(RequestModel requestModel, Map<String, Object> publisherDetail);

  void injectGeo(RequestModel model, Map<String, Object> geo);

}
