package com.rackspacecloud.metrics.tenantroutingservice.services;

import com.rackspacecloud.metrics.tenantroutingservice.domain.TenantRoutes;
import com.rackspacecloud.metrics.tenantroutingservice.model.IngestionRoutingInformationInput;

public interface RoutingService {
    /**
     * Set Ingestion Routing Information for given tenantId
     * @param tenantId
     * @param routingInformation
     * @return
     */
    TenantRoutes setIngestionRoutingInformation(String tenantId, IngestionRoutingInformationInput routingInformation);

    /**
     * Get Ingestion Routing Information for given tenantId
     * @param tenantId
     * @param measurement
     * @return
     */
    TenantRoutes getIngestionRoutingInformation(String tenantId, String measurement);

    /**
     * Remove Ingestion Routing Information for given tenantId
     * @param tenantId
     * @return
     */
    void removeIngestionRoutingInformation(String tenantId);
}
