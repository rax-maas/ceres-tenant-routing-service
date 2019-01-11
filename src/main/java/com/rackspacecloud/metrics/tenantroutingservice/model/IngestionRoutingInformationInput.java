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

    public String getPath() {
        return path;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

}
