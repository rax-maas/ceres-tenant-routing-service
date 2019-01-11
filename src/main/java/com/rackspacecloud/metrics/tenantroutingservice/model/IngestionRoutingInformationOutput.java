package com.rackspacecloud.metrics.tenantroutingservice.model;

import com.rackspacecloud.metrics.tenantroutingservice.domain.TenantRoutes;
import lombok.Data;
import java.util.Map;

@Data
public class IngestionRoutingInformationOutput {
    private Map<String, TenantRoutes.TenantRoute> routes;
}
