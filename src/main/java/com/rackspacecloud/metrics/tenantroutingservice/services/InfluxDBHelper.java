package com.rackspacecloud.metrics.tenantroutingservice.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class InfluxDBHelper {
    private RestTemplate restTemplate;

    private static final Logger LOGGER = LoggerFactory.getLogger(InfluxDBHelper.class);

    @Autowired
    public InfluxDBHelper(RestTemplate restTemplate){
        this.restTemplate = restTemplate;
    }


}
