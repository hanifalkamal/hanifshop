package com.hanifshop.order_service.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.*;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import java.util.Map;

public final class HttpUtil {

    private static final Logger logger = LogManager.getLogger(HttpUtil.class);

    public static <T> ResponseEntity<String> callServiceGETParam(String uri, UriComponentsBuilder uriBuilder) throws Exception {
        ResponseEntity<String> response = null;
        try {
            logger.info("## Request : " + uri + "\n" + uriBuilder.toUriString());
            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.add("Accept", MediaType.APPLICATION_JSON_VALUE);
            HttpEntity<T> requestEntity = new HttpEntity<>(requestHeaders);
            response = new RestTemplate().exchange(uriBuilder.toUriString(), HttpMethod.GET, requestEntity, String.class);
            logger.info("## Response : " + response.getBody());
        } catch (HttpClientErrorException e) {
            validateHttpResponse(e);
        }
        return response;
    }

    public static <T> ResponseEntity<String> callServiceGETParam(String uri, UriComponentsBuilder uriBuilder, HttpHeaders httpHeaders) throws Exception {
        ResponseEntity<String> response = null;
        try {
            logger.info("## Request : " + uri + "\n" + uriBuilder.toUriString());
            HttpEntity<T> requestEntity = new HttpEntity<>(httpHeaders);
            response = new RestTemplate().exchange(uriBuilder.toUriString(), HttpMethod.GET, requestEntity, String.class);
            logger.info("## Response : " + response.getBody());
        } catch (HttpClientErrorException e) {
            validateHttpResponse(e);
        }
        return response;
    }

    public static <T> ResponseEntity<String> callServicePOST(String uri, T pojo) throws Exception {
        ResponseEntity<String> response = null;
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
            headers.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
            HttpEntity<T> entity = new HttpEntity<>(pojo, headers);
            logger.info("URL : " + uri);
            ObjectMapper mapper = new ObjectMapper();
            logger.info("Request Body : \n" + mapper.writerWithDefaultPrettyPrinter().writeValueAsString(pojo.toString()));
            response = new RestTemplate().exchange(uri, HttpMethod.POST, entity, String.class);
            logger.info("Response : \n" + mapper.writerWithDefaultPrettyPrinter().writeValueAsString(response.getBody()));
        } catch (HttpClientErrorException e) {
            validateHttpResponse(e);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok(response.getBody());
    }

    public static <T> ResponseEntity<String> callServicePOST(String uri, T pojo, HttpHeaders httpHeaders) throws Exception {
        ResponseEntity<String> response = null;
        HttpEntity<T> entity = new HttpEntity<>(pojo, httpHeaders);
        try {
            logger.info("URL : " + uri);
            ObjectMapper mapper = new ObjectMapper();
            logger.info("Request Body : \n" + mapper.writerWithDefaultPrettyPrinter().writeValueAsString(pojo.toString()));
            response = new RestTemplate().exchange(uri, HttpMethod.POST, entity, String.class);
            logger.info("Response : \n" + mapper.writerWithDefaultPrettyPrinter().writeValueAsString(response.getBody()));
        } catch (HttpClientErrorException e) {
            validateHttpResponse(e);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok(response.getBody());
    }

    public static ResponseEntity<String> callServicePOST(String uri, MultiValueMap<String, String> map) throws Exception {
        ResponseEntity<String> response = null;
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, headers);
            ObjectMapper mapper = new ObjectMapper();
            logger.info("Request Body : \n" + PojoJsonMapper.toJson(map));
            response = restTemplate.exchange(uri, HttpMethod.POST, entity, String.class);
            logger.info("Response : \n" + mapper.writerWithDefaultPrettyPrinter().writeValueAsString(response.getBody()));
        } catch (HttpClientErrorException e) {
            validateHttpResponse(e);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok(response.getBody());
    }

    public static ResponseEntity<String> callServiceGETParamNonLog(String uri, UriComponentsBuilder uriBuilder) {
        ResponseEntity<String> response = null;
        try {
            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.add("Accept", MediaType.APPLICATION_JSON_VALUE);
            HttpEntity<String> requestEntity = new HttpEntity<>(requestHeaders);
            response = new RestTemplate().exchange(uriBuilder.toUriString(), HttpMethod.GET, requestEntity, String.class);
        } catch (Exception ex) {
            ex.printStackTrace(System.out);
            logger.info("Error response entity : " + ex.getMessage());
        }
        return response;
    }

    public static void validateHttpResponse(ResponseEntity<String> request) throws Exception {
        if (request.getStatusCodeValue() != 202 && request.getStatusCodeValue() != 200) {
            if (request.getStatusCodeValue() == 401) {
                throw new Exception("Invalid Token");
            }
            if (request.getBody() != null) {
                Map<?, ?> body = PojoJsonMapper.fromJson(request.getBody(), Map.class);
                if (!body.containsKey("result")) {
                    throw new Exception(body.get("statusDescription").toString());
                }
                String description = ((Map<?, ?>) body.get("result")).get("description").toString();
                throw new Exception(description + "#" + request.getStatusCodeValue());
            }
            throw new Exception(Integer.toString(request.getStatusCodeValue()));
        }
        Map<?, ?> body = PojoJsonMapper.fromJson(request.getBody(), Map.class);
        if (body.isEmpty()) {
            throw new Exception("DATA EMPTY");
        }
        if (body.containsKey("error")) {
            throw new Exception(body.get("error_description").toString());
        }
    }

    public static void validateHttpResponse(HttpClientErrorException request) throws Exception {
        if (request.getRawStatusCode() != 202 && request.getRawStatusCode() != 200) {
            if (request.getRawStatusCode() == 401) {
                throw new Exception("Invalid Token");
            }

//            logger.info("REQUEST BODY = " + request.getResponseBodyAsString());
            Map<?, ?> body = PojoJsonMapper.fromJson(request.getResponseBodyAsString(), Map.class);
            if (!body.containsKey("error")) {
                throw new Exception(body.get("error").toString());
            }
        }
        Map<?, ?> body = PojoJsonMapper.fromJson(request.getResponseBodyAsString(), Map.class);
        if (body.isEmpty()) {
            throw new Exception("DATA EMPTY");
        }
        if (body.containsKey("error")) {
            throw new Exception(body.get("error").toString());
        }
    }




}
