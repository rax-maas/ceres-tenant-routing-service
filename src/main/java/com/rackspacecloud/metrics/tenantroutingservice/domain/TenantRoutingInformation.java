package com.rackspacecloud.metrics.tenantroutingservice.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.util.HashSet;
import java.util.Set;

@RedisHash("routing-metadata")
@Data
public class TenantRoutingInformation {
    @Id
    private String tenantId;

    private int ingestionPort;
    private Set<Integer> queryPorts;
    private int maxSeriesCount;

    public TenantRoutingInformation(){
        this.queryPorts = new HashSet<>();
    }
}
