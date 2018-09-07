package com.rackspacecloud.metrics.tenantroutingservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class IngestionRoutingInformationInput {
    @NotNull
    @JsonProperty("port")
    private int port;

    @NotNull
    @JsonProperty("maxSeriesCount")
    private int maxSeriesCount;
}
