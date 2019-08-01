package com.rackspacecloud.metrics.tenantroutingservice.services;

import com.rackspacecloud.metrics.tenantroutingservice.domain.RetentionPolicyEnum;
import com.rackspacecloud.metrics.tenantroutingservice.domain.TenantMeasurements;
import com.rackspacecloud.metrics.tenantroutingservice.domain.TenantRoutes;
import com.rackspacecloud.metrics.tenantroutingservice.exceptions.MeasurementNotFoundException;
import com.rackspacecloud.metrics.tenantroutingservice.exceptions.RouteNotFoundException;
import com.rackspacecloud.metrics.tenantroutingservice.exceptions.RouteWriteException;
import com.rackspacecloud.metrics.tenantroutingservice.model.IngestionRoutingInformationInput;
import com.rackspacecloud.metrics.tenantroutingservice.repositories.ITenantMeasurementRepository;
import com.rackspacecloud.metrics.tenantroutingservice.repositories.ITenantRoutingInformationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.*;

public class RoutingServiceImpl implements RoutingService {
    private static final String DefaultInfluxDBScalerUrl = "http://localhost:8087";
    private RestTemplate restTemplate;
    private ITenantRoutingInformationRepository routingInformationRepository;
    private ITenantMeasurementRepository tenantMeasurementsRepository;
    private String influxDBScalerUrl;

    private static final Logger LOGGER = LoggerFactory.getLogger(RoutingServiceImpl.class);

    public RoutingServiceImpl(RestTemplate restTemplate,
                              ITenantRoutingInformationRepository routingInformationRepository,
                              String influxDBScalerUrl,
                              ITenantMeasurementRepository tenantMeasurementsRepository
                              ) {
        this.restTemplate = restTemplate;
        this.routingInformationRepository = routingInformationRepository;
        this.influxDBScalerUrl = influxDBScalerUrl;
        this.tenantMeasurementsRepository = tenantMeasurementsRepository;
    }

    @Override
    public TenantRoutes getIngestionRoutingInformation(String tenantId, String measurement, boolean readOnly) {

        if(StringUtils.isEmpty(tenantId) || StringUtils.isEmpty(tenantId.trim()))
            throw new IllegalArgumentException("'tenantId' is null, empty or contains all whitespaces.");

        if(StringUtils.isEmpty(measurement) || StringUtils.isEmpty(measurement.trim()))
            throw new IllegalArgumentException("'measurement' is null, empty or contains all whitespaces.");

        String tenantIdAndMeasurement = String.format("%s:%s", tenantId, measurement);

        Optional<TenantRoutes> routingInfo = routingInformationRepository.findById(tenantIdAndMeasurement);

        if(!routingInfo.isPresent() && readOnly) {
            LOGGER.info("Could not look up route for tenantId [{}] and measurement [{}]", tenantId, measurement);
            throw new RouteNotFoundException(routingInfo.toString());
        }

        if(!routingInfo.isPresent()) {
            LOGGER.info("Route not found for tenantId [{}] and measurement [{}]", tenantId, measurement);

            String influxDBUrl = getMinSeriesCountInfluxDBInstance();

            IngestionRoutingInformationInput ingestionRoutesInfo = new IngestionRoutingInformationInput();
            ingestionRoutesInfo.setPath(influxDBUrl);
            ingestionRoutesInfo.setDatabaseName(createDatabaseName(tenantId, measurement));

            TenantRoutes routingInformation =
                    new TenantRoutes(tenantIdAndMeasurement, ingestionRoutesInfo, createListOfDefaultRoutes());
            try {
                addTenantMeasurement(tenantId, measurement);

                LOGGER.info("Creating route for tenantId [{}] and measurement [{}] with routing information: [{}]",
                        tenantId, measurement, routingInformation.toString());
                return routingInformationRepository.save(routingInformation);
            }
            catch (Exception e) {
                throw new RouteWriteException(routingInformation.toString(), e);
            }
        }

        return routingInfo.get();
    }

    private void addTenantMeasurement(String tenantId, String measurement) {
        Optional tenantMeasurementsOptional = tenantMeasurementsRepository.findById(tenantId);
        TenantMeasurements tenantMeasurements;

        if(tenantMeasurementsOptional.isPresent()) {
            tenantMeasurements = (TenantMeasurements) tenantMeasurementsOptional.get();
            tenantMeasurements.getMeasurements().add(measurement);
        }
        else {
            tenantMeasurements = new TenantMeasurements();
            tenantMeasurements.setTenantId(tenantId);
            Set<String> measurements = new HashSet<>();
            measurements.add(measurement);
            tenantMeasurements.setMeasurements(measurements);
        }

        tenantMeasurementsRepository.save(tenantMeasurements);
    }

    private String createDatabaseName(String tenantId, String measurement) {
        int numberOfBuckets = 10;
        int hashCodeSum = tenantId.hashCode() ^ measurement.hashCode(); // XOR operation to avoid any overflow
        int bucketIndex = Math.abs(hashCodeSum) % numberOfBuckets;

        return "db_" + bucketIndex;
    }

    @Override
    public Collection<String> getMeasurements(String tenantId) {
        if(StringUtils.isEmpty(tenantId) || StringUtils.isEmpty(tenantId.trim()))
            throw new IllegalArgumentException("'tenantId' is null, empty or contains all whitespaces.");

        try {
            Optional tenantMeasurementsOptional = tenantMeasurementsRepository.findById(tenantId);
            if(tenantMeasurementsOptional.isPresent()) {
                TenantMeasurements tenantMeasurements = (TenantMeasurements) tenantMeasurementsOptional.get();
                return tenantMeasurements.getMeasurements();
            }
        }
        catch (Exception e) {
            throw new MeasurementNotFoundException(tenantId, e);
        }

        return new HashSet<>();
    }

    private List<RetentionPolicyEnum> createListOfDefaultRoutes(){
        List<RetentionPolicyEnum> list = new ArrayList<>();
        list.add(RetentionPolicyEnum.FULL);
        list.add(RetentionPolicyEnum.FIVE_MINUTES);
        list.add(RetentionPolicyEnum.TWENTY_MINUTES);
        list.add(RetentionPolicyEnum.ONE_HOUR);
        list.add(RetentionPolicyEnum.FOUR_HOURS);
        list.add(RetentionPolicyEnum.ONE_DAY);

        return list;
    }

    public String getMinSeriesCountInfluxDBInstance() {
        if(StringUtils.isEmpty(influxDBScalerUrl)) return DefaultInfluxDBScalerUrl;

        return restTemplate.getForObject(String.format("%s/min-series-count-url", influxDBScalerUrl), String.class);
    }
}
