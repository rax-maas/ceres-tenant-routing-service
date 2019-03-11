package com.rackspacecloud.metrics.tenantroutingservice.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.rackspacecloud.metrics.tenantroutingservice.model.IngestionRoutingInformationInput;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RedisHash("routing-metadata")
@Data
public class TenantRoutes {
    @Id
    private String tenantIdAndMeasurement;
    @JsonProperty("routes")
    private Map<String, TenantRoute> routes;

    public TenantRoutes() {
        routes = new HashMap<>();
    }

    private void setTenantIdAndMeasurement(String tenantIdAndMeasurement) {
        this.tenantIdAndMeasurement = tenantIdAndMeasurement;
    }

    public TenantRoutes(
            String tenantIdAndMeasurement,
            IngestionRoutingInformationInput input,
            List<RetentionPolicyEnum> defaultRoutes) {

        this();
        this.setTenantIdAndMeasurement(tenantIdAndMeasurement);
        for(RetentionPolicyEnum value : defaultRoutes) {
            value.setDatabaseName(input.getDatabaseName()).setPath(input.getPath());
            routes.put(value.toString(), new TenantRoute(value));
        }
    }

    @Data
    @RequiredArgsConstructor
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
