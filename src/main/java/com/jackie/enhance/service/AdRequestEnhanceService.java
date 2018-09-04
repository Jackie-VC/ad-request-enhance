package com.jackie.enhance.service;

import com.jackie.enhance.exception.InternalException;
import com.jackie.enhance.model.AdRequestModel;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * @ author: Chenglong Li
 * @ date: 29/08/2018
 */
public interface AdRequestEnhanceService {

  void injectDemographics(AdRequestModel model, Map<String, Object> demographics)
      throws InternalException;

  void injectPublisherDetail(AdRequestModel model, Map<String, Object> publisherDetail)
      throws InternalException;

  void injectGeo(AdRequestModel model, Map<String, Object> geo) throws InternalException;

  CompletableFuture<Map<String, Object>> requestDemographics(AdRequestModel model, String siteId)
      throws InternalException;

  CompletableFuture<Map<String, Object>> requestPublisherDetail(AdRequestModel model, String siteId)
      throws InternalException;

  CompletableFuture<Map<String, Object>> requestGeoInfo(AdRequestModel model, String deviceIp)
      throws InternalException;

}
