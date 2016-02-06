package com.milcomsolutions.service;

import java.util.Map;


public interface HttpService {

    <T> T postJson(String serviceURL, Map<String, String> params, Class<T> returnClass) throws Exception;


    <T> T getJson(String serviceURL, Map<String, String> params, Class<T> returnClass) throws Exception;


    <T> T postJsonAsString(String endpointURL, String content, Class<T> returnClass) throws Exception;


    String postForm(String paymentGatewayUrl, Map<String, Object> postLoad);


    <T> T postJsonStringWithBasicAuth(String remoteVendUrl, String json, Class<T> class1, String remoteVendUserName, String remoteVendPassword);


    String postString(String data, String url) throws Exception;


    <T> T postJsonHandlerRedirect(String endPoint, Map<String, String> params, Class<T> responseClas) throws Exception;
}
