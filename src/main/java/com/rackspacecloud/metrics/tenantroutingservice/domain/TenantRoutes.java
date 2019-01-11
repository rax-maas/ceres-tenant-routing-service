package com.rackspacecloud.metrics.tenantroutingservice.domain;

import com.rackspacecloud.metrics.tenantroutingservice.model.IngestionRoutingInformationInput;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class TenantRoutes {
    @Id
    private String tenantId;
    private Map<String, TenantRoute> routes;

    public TenantRoutes() {
        routes = new HashMap<>();
    }

    public TenantRoutes(IngestionRoutingInformationInput input, List<RetentionPolicyEnum> defaultRoutes) {
        routes = new HashMap<>();
        for(RetentionPolicyEnum value : defaultRoutes) {
            value.setDatabaseName(input.getDatabaseName()).setPath(input.getPath());
            routes.put(value.toString(), new TenantRoute(value));
        }
    }

    @Data
    @AllArgsConstructor
    public static class TenantRoute {
        private String path;
        private String databaseName;
        private String retentionPolicyName;
        private String retentionPolicy;

        public TenantRoute(RetentionPolicyEnum enumeration) {
            retentionPolicyName = enumeration.retentionPolicyName;
            retentionPolicy = enumeration.retentionPolicy;

            if(enumeration.databaseName != null) {
                databaseName = enumeration.databaseName;
            }
            if(enumeration.path != null) {
                path = enumeration.path;
            }
        }
    }
}
