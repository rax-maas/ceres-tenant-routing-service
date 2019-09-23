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
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Slf4j
public class RoutingServiceImpl implements RoutingService {
    private int numberOfDatabasesInInfluxDBInstance;
    private static final String DefaultInfluxDBScalerUrl = "http://localhost:8087";
    private RestTemplate restTemplate;
    private ITenantRoutingInformationRepository routingInformationRepository;
    private ITenantMeasurementRepository tenantMeasurementsRepository;
    private String influxDBScalerUrl;
    private String influxDBEnterpriseUrl;
    private boolean isUsingInfluxdbEnterprise;

    public RoutingServiceImpl(RestTemplate restTemplate,
                              ITenantRoutingInformationRepository routingInformationRepository,
                              String influxDBScalerUrl,
                              String influxDBEnterpriseUrl,
                              boolean isUsingInfluxdbEnterprise,
                              ITenantMeasurementRepository tenantMeasurementsRepository,
                              int numberOfDatabasesInInfluxDBInstance
                              ) {
        this.numberOfDatabasesInInfluxDBInstance = numberOfDatabasesInInfluxDBInstance;
        this.restTemplate = restTemplate;
        this.routingInformationRepository = routingInformationRepository;
        this.influxDBScalerUrl = influxDBScalerUrl;
        this.influxDBEnterpriseUrl = influxDBEnterpriseUrl;
        this.isUsingInfluxdbEnterprise = isUsingInfluxdbEnterprise;
        this.tenantMeasurementsRepository = tenantMeasurementsRepository;
    }

    /**
     * Get routing information for ingestion or query.
     * @param tenantId
     * @param measurement
     * @param readOnly - if this parameter's value is 'true', it will not allow creation of new routes.
     * @return
     */
    @Override
    public TenantRoutes getIngestionRoutingInformation(String tenantId, String measurement, boolean readOnly) {

        // Input validations
        Assert.isTrue(
                (!StringUtils.isEmpty(tenantId) && !(StringUtils.isEmpty(tenantId.trim()))),
                "'tenantId' is null, empty or contains all whitespaces."
        );

        Assert.isTrue(
                (!StringUtils.isEmpty(measurement) && !StringUtils.isEmpty(measurement.trim())),
                "'measurement' is null, empty or contains all whitespaces."
        );

        // Create key to store or query paths for given tenantId AND measurement
        String tenantIdAndMeasurement = String.format("%s:%s", tenantId, measurement);

        // Get the routes from the routing repository
        Optional<TenantRoutes> routingInfo = routingInformationRepository.findById(tenantIdAndMeasurement);

        if(!routingInfo.isPresent()) { // Routing repository doesn't contain the routes.
            // In case of query, we don't want to create new paths for given tenantId AND measurement
            if(readOnly) {
                log.info("Could not find route for tenantId [{}] and measurement [{}]", tenantId, measurement);
                throw new RouteNotFoundException(
                        String.format("Not Found\nRoutingInfo:%s\nTenantID:%s\nMeasurement:%s",
                                routingInfo.toString(),tenantId, measurement));
            }

            log.info("Route not found for tenantId [{}] and measurement [{}]", tenantId, measurement);

            // Create routes for new "TenantId + Measurement" duo
            return createTenantRoutes(tenantId, measurement, tenantIdAndMeasurement);
        }

        return routingInfo.get();
    }

    /**
     * Creates new routes for given tenantId + measurement and stores them into the repository
     * @param tenantId
     * @param measurement
     * @param tenantIdAndMeasurement
     * @return
     */
    private TenantRoutes createTenantRoutes(String tenantId, String measurement, String tenantIdAndMeasurement) {
        // Get the InfluxDB url to use as the home for the data related to the key tenantId AND measurement duo
        String influxDBUrl = getInfluxDBUrlToUse();

        IngestionRoutingInformationInput ingestionRoutesInfo = new IngestionRoutingInformationInput();
        ingestionRoutesInfo.setPath(influxDBUrl);
        ingestionRoutesInfo.setDatabaseName(createDatabaseName(tenantId, measurement));

        TenantRoutes routingInformation =
                new TenantRoutes(tenantIdAndMeasurement, ingestionRoutesInfo, createListOfDefaultRoutes());
        try {
            addTenantMeasurement(tenantId, measurement);

            log.info("Creating route for tenantId [{}] and measurement [{}] with routing information: [{}]",
                    tenantId, measurement, routingInformation.toString());

            return routingInformationRepository.save(routingInformation);
        }
        catch (Exception e) {
            throw new RouteWriteException(routingInformation.toString(), e);
        }
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
        int numberOfBuckets = numberOfDatabasesInInfluxDBInstance;
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

    private String getInfluxDBUrlToUse() {

        if(isUsingInfluxdbEnterprise) return influxDBEnterpriseUrl;

        if(StringUtils.isEmpty(influxDBScalerUrl)) return DefaultInfluxDBScalerUrl;

        return restTemplate.getForObject(String.format("%s/min-series-count-url", influxDBScalerUrl), String.class);
    }
}
