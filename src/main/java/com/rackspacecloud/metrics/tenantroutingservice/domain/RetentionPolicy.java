package com.rackspacecloud.metrics.tenantroutingservice.domain;

import lombok.Data;

@Data
public class RetentionPolicy {

    private String name;
    private String databaseName;
    private int maxSeriesCount;
    private String retentionPolicyName;
    private String retentionPolicy;


    public RetentionPolicy(String databaseName, String policyName, String name) {
        this.name = name;
        retentionPolicyName = policyName;
        retentionPolicy = "rp_"+retentionPolicyName;
        this.databaseName = databaseName;

        maxSeriesCount = 10000;

    }

}
