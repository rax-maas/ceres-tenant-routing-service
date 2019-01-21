package com.rackspacecloud.metrics.tenantroutingservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;


@Data
public class IngestionRoutingInformationInput {
    @JsonProperty("path")
    @Pattern(regexp = "^https?://.+") // matches http or https URLs
    private String path;

    @JsonProperty("databaseName")
    @NotBlank
    private String databaseName;
}
