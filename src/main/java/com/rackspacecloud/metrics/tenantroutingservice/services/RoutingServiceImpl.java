package com.rackspacecloud.metrics.tenantroutingservice.services;

import com.rackspacecloud.metrics.tenantroutingservice.domain.MaxAndMinSeriesInstances;
import com.rackspacecloud.metrics.tenantroutingservice.domain.RetentionPolicyEnum;
import com.rackspacecloud.metrics.tenantroutingservice.domain.TenantRoutes;
import com.rackspacecloud.metrics.tenantroutingservice.exceptions.RouteConflictException;
import com.rackspacecloud.metrics.tenantroutingservice.exceptions.RouteDeleteException;
import com.rackspacecloud.metrics.tenantroutingservice.exceptions.RouteNotFoundException;
import com.rackspacecloud.metrics.tenantroutingservice.exceptions.RouteWriteException;
import com.rackspacecloud.metrics.tenantroutingservice.model.IngestionRoutingInformationInput;
import com.rackspacecloud.metrics.tenantroutingservice.repositories.ITenantRoutingInformationRepository;
import com.rackspacecloud.metrics.tenantroutingservice.repositories.MaxMinInstancesRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RoutingServiceImpl implements RoutingService {
    private static final Logger LOGGER = LoggerFactory.getLogger(RoutingServiceImpl.class);

    @Autowired
    ITenantRoutingInformationRepository routingInformationRepository;

    @Autowired
    MaxMinInstancesRepository maxMinInstancesRepository;

    @Override
    public TenantRoutes setIngestionRoutingInformation(String tenantId, IngestionRoutingInformationInput tenantInfo) {
        if(StringUtils.isEmpty(tenantId) || StringUtils.isEmpty(tenantId.trim()))
            throw new IllegalArgumentException("'tenantId' is null, empty or contains all whitespaces.");

        if(tenantInfo == null) throw new IllegalArgumentException("'tenantInfo' is null.");

        Optional<TenantRoutes> oRoutingInformation = routingInformationRepository.findById(tenantId);

        if(oRoutingInformation.isPresent()) throw new RouteConflictException(tenantId);

        TenantRoutes routingInformation = new TenantRoutes(tenantId, tenantInfo, createListOfDefaultRoutes());

        try {
            return routingInformationRepository.save(routingInformation);
        }
        catch (Exception e) {
            throw new RouteWriteException(routingInformation.toString(), e);
        }
    }

    @Override
    public TenantRoutes getIngestionRoutingInformation(String tenantId, String measurement) {

        if(StringUtils.isEmpty(tenantId) || StringUtils.isEmpty(tenantId.trim()))
            throw new IllegalArgumentException("'tenantId' is null, empty or contains all whitespaces.");

        if(StringUtils.isEmpty(measurement) || StringUtils.isEmpty(measurement.trim()))
            throw new IllegalArgumentException("'measurement' is null, empty or contains all whitespaces.");

        String tenantIdAndMeasurement = String.format("%s:%s", tenantId, measurement);

        Optional<TenantRoutes> routingInfo = routingInformationRepository.findById(tenantIdAndMeasurement);

        if(!routingInfo.isPresent()) {
            LOGGER.info("Route not found for tenantId [{}] and measurement [{}]", tenantId, measurement);

            Optional<MaxAndMinSeriesInstances> maxAndMinSeriesInstances = maxMinInstancesRepository.findById("MIN");
            if(!maxAndMinSeriesInstances.isPresent()) throw new RouteNotFoundException(tenantIdAndMeasurement);

            String influxDBUrl = maxAndMinSeriesInstances.get().getUrl();

            IngestionRoutingInformationInput ingestionRoutesInfo = new IngestionRoutingInformationInput();
            ingestionRoutesInfo.setPath(influxDBUrl);
            ingestionRoutesInfo.setDatabaseName(createDatabaseName(tenantId, measurement));

            TenantRoutes routingInformation =
                    new TenantRoutes(tenantIdAndMeasurement, ingestionRoutesInfo, createListOfDefaultRoutes());
            try {
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

    private String createDatabaseName(String tenantId, String measurement) {
        int numberOfBuckets = 10;
        int hashCodeSum = tenantId.hashCode() + measurement.hashCode();
        int bucketIndex = Math.abs(hashCodeSum) % numberOfBuckets;

        return "db_" + bucketIndex;
    }

    @Override
    public void removeIngestionRoutingInformation(String tenantId) {
        if(StringUtils.isEmpty(tenantId) || StringUtils.isEmpty(tenantId.trim()))
            throw new IllegalArgumentException("'tenantId' is null, empty or contains all whitespaces.");

        try {
            routingInformationRepository.deleteById(tenantId);
        }
        catch (Exception e) {
            throw new RouteDeleteException(tenantId, e);
        }
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
}
