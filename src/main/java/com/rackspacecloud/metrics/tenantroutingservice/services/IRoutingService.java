package com.rackspacecloud.metrics.tenantroutingservice.services;

import com.rackspacecloud.metrics.tenantroutingservice.domain.TenantRoutes;
import com.rackspacecloud.metrics.tenantroutingservice.model.IngestionRoutingInformationInput;

public interface IRoutingService {
    /**
     * Set Ingestion Routing Information for given tenantId
     * @param tenantId
     * @param routingInformation
     * @return
     */
    TenantRoutes setIngestionRoutingInformation(
            String tenantId, IngestionRoutingInformationInput routingInformation);


    TenantRoutes setIngestionRoutingInformation(
            String tenantId, TenantRoutes routingInformation);

    /**
     * Get Ingestion Routing Information for given tenantId
     * @param tenantId
     * @return
     */
    TenantRoutes getIngestionRoutingInformation(String tenantId);

    /**
     * Remove Ingestion Routing Information for given tenantId
     * @param tenantId
     * @return
     */
    void removeIngestionRoutingInformation(String tenantId);
}
