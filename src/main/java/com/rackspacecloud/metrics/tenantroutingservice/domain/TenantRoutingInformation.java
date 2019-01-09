package com.rackspacecloud.metrics.tenantroutingservice.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.rackspacecloud.metrics.tenantroutingservice.model.IngestionRoutingInformationInput;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.util.*;

@RedisHash("routing-metadata")
@Data
public class TenantRoutingInformation {
    @Id
    private String tenantId;

    private String path;

    @JsonProperty("retentionPolicies")
    private List<RetentionPolicy> retentionPolicies;

    public TenantRoutingInformation(IngestionRoutingInformationInput input){
        RetentionPolicyBuilder builder = new RetentionPolicyBuilder();
        retentionPolicies = new LinkedList<RetentionPolicy>();
        retentionPolicies.add(builder.buildFullRetentionPolicy(input.getDatabaseName()));
        retentionPolicies.add(builder.buildFiveMinuteRetentionPolicy(input.getDatabaseName()));
        retentionPolicies.add(builder.buildTwentyMinuteRetentionPolicy(input.getDatabaseName()));
        retentionPolicies.add(builder.buildOneHourRetentionPolicy(input.getDatabaseName()));
        retentionPolicies.add(builder.buildFourHourRetentionPolicy(input.getDatabaseName()));
        retentionPolicies.add(builder.buildOneDayRetentionPolicy(input.getDatabaseName()));
        path = input.getPath();
    }


    public String getIngestionPath() {
        return path;
    }
}
