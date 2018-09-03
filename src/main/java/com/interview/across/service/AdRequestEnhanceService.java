package com.interview.across.service;

import com.interview.across.exception.InternalException;
import com.interview.across.model.AdRequest;
import java.util.concurrent.CompletableFuture;

/**
 * @ author: Chenglong Li
 * @ date: 29/08/2018
 */
public interface AdRequestEnhanceService {

  CompletableFuture<AdRequest> injectDemographics(AdRequest model, String siteId)
      throws InternalException;

  CompletableFuture<AdRequest> injectPublisherDetail(AdRequest model, String siteId)
      throws InternalException;

  CompletableFuture<AdRequest> injectGeo(AdRequest model, String deviceId) throws InternalException;

  boolean isUsIp(String deviceIp) throws InternalException;

}
