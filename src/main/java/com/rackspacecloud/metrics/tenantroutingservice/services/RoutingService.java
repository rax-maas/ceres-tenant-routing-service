package com.rackspacecloud.metrics.tenantroutingservice.services;

import com.rackspacecloud.metrics.tenantroutingservice.domain.TenantRoutes;
import com.rackspacecloud.metrics.tenantroutingservice.model.IngestionRoutingInformationInput;

import java.util.Collection;
import java.util.List;

public interface RoutingService {
    /**
     * Get Ingestion Routing Information for given tenantId
     * @param tenantId
     * @param measurement
     * @return
     */
    TenantRoutes getIngestionRoutingInformation(String tenantId, String measurement) throws Exception;

    /**
     * Get measurements for given tenantId
     * @param tenantId
     * @return
     */
    Collection<String> getMeasurements(String tenantId);
}
