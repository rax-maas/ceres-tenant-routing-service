package com.rackspacecloud.metrics.tenantroutingservice.config;

import com.rackspacecloud.metrics.tenantroutingservice.repositories.ITenantMeasurementRepository;
import com.rackspacecloud.metrics.tenantroutingservice.repositories.ITenantRoutingInformationRepository;
import com.rackspacecloud.metrics.tenantroutingservice.repositories.TenantMeasurementsRepository;
import com.rackspacecloud.metrics.tenantroutingservice.services.InfluxDBHelper;
import com.rackspacecloud.metrics.tenantroutingservice.services.RoutingServiceImpl;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Configuration
@EnableConfigurationProperties(RestTemplateConfigurationProperties.class)
public class RoutingServiceImplConfiguration {
    @Value("${influxdb.scaler.url}")
    private String influxDBScalerUrl;

    @Autowired
    RestTemplateConfigurationProperties config;

    @Bean
    public PoolingHttpClientConnectionManager poolingHttpClientConnectionManager() {
        PoolingHttpClientConnectionManager poolingConnectionManager = new PoolingHttpClientConnectionManager();
        poolingConnectionManager.setMaxTotal(config.getPoolingHttpClientConnectionManager().getMaxTotal());
        return poolingConnectionManager;
    }

    @Bean
    public RequestConfig requestConfig() {
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(config.getRequestConfig().getConnectionRequestTimeout())
                .setConnectTimeout(config.getRequestConfig().getConnectTimeout())
                .setSocketTimeout(config.getRequestConfig().getSocketTimeout())
                .build();
        return requestConfig;
    }

    @Bean
    public CloseableHttpClient httpClient(
            PoolingHttpClientConnectionManager poolingHttpClientConnectionManager,
            RequestConfig requestConfig) {

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        CloseableHttpClient builder = HttpClientBuilder
                .create()
                .setConnectionManager(poolingHttpClientConnectionManager)
                .setDefaultRequestConfig(requestConfig)
                .build();
        return builder;
    }

    @Bean
    public RestTemplate restTemplate(HttpClient httpClient) {
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setHttpClient(httpClient);
        return new RestTemplate(requestFactory);
    }

    @Bean
    @Autowired
    public RoutingServiceImpl routingService(
            RestTemplate restTemplate,
            ITenantRoutingInformationRepository routingInformationRepository,
            ITenantMeasurementRepository tenantMeasurementsRepository) {
        return new RoutingServiceImpl(restTemplate, routingInformationRepository,
                influxDBScalerUrl, tenantMeasurementsRepository);
    }
}
