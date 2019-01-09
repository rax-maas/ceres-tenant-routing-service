package com.rackspacecloud.metrics.tenantroutingservice.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class RetentionPolicy {

    @JsonProperty("name")
    private String name;
    @JsonProperty("databaseName")
    private String databaseName;
    @JsonProperty("maxSeriesCount")
    private int maxSeriesCount;
    @JsonProperty("retentionPolicyName")
    private String retentionPolicyName;
    @JsonProperty("retentionPolicy")
    private String retentionPolicy;


    public RetentionPolicy(String databaseName, String policyName, String name) {
        this.name = name;
        retentionPolicyName = policyName;
        retentionPolicy = "rp_"+retentionPolicyName;
        this.databaseName = databaseName;

        maxSeriesCount = 10000;

    }

}
