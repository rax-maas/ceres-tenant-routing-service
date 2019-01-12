package com.rackspacecloud.metrics.tenantroutingservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.rackspacecloud.metrics.tenantroutingservice.domain.RetentionPolicyEnum;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.*;


@Data
public class IngestionRoutingInformationInput {
    @JsonProperty("path")
    private @NotNull String path;

    @JsonProperty("databaseName")
    private String databaseName;
}
