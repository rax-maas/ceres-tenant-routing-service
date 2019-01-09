package com.rackspacecloud.metrics.tenantroutingservice.model;

import com.rackspacecloud.metrics.tenantroutingservice.domain.RetentionPolicy;
import lombok.Data;

import java.util.List;

@Data
public class IngestionRoutingInformationOutput {
    private String path;
    private List<RetentionPolicy> retentionPolicies;
}
