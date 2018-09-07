package com.rackspacecloud.metrics.tenantroutingservice.services;

import com.rackspacecloud.metrics.tenantroutingservice.domain.TenantRoutingInformation;
import com.rackspacecloud.metrics.tenantroutingservice.model.IngestionRoutingInformationInput;
import com.rackspacecloud.metrics.tenantroutingservice.model.IngestionRoutingInformationOutput;

public interface IRoutingService {
    /**
     * Set Ingestion Routing Information for given tenantId
     * @param tenantId
     * @param routingInformation
     * @return
     */
    TenantRoutingInformation setIngestionRoutingInformation(
            String tenantId, IngestionRoutingInformationInput routingInformation);

    /**
     * Get Ingestion Routing Information for given tenantId
     * @param tenantId
     * @return
     */
    IngestionRoutingInformationOutput getIngestionRoutingInformation(String tenantId);
}
