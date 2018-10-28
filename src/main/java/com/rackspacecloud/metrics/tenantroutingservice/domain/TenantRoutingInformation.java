package com.rackspacecloud.metrics.tenantroutingservice.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.util.HashMap;
import java.util.Map;

@RedisHash("routing-metadata")
@Data
public class TenantRoutingInformation {
    @Id
    private String tenantId;

    private String ingestionPath;
    private String ingestionDatabaseName;

    // This map contains path and databaseName as key/value pairs
    private Map<String, String> queryRoutes;
    private int maxSeriesCount;

    public TenantRoutingInformation(){
        this.queryRoutes = new HashMap<>();
    }
}
