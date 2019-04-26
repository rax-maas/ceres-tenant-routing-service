package com.rackspacecloud.metrics.tenantroutingservice.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.util.Set;

@RedisHash("tenant-measurements")
@Data
public class TenantMeasurements {
    @Id
    private String tenantId;
    private Set<String> measurements;
}
