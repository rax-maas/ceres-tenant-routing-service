package com.rackspacecloud.metrics.tenantroutingservice.controllers;

import com.rackspacecloud.metrics.tenantroutingservice.domain.TenantRoutingInformation;
import com.rackspacecloud.metrics.tenantroutingservice.model.IngestionRoutingInformationInput;
import com.rackspacecloud.metrics.tenantroutingservice.model.IngestionRoutingInformationOutput;
import com.rackspacecloud.metrics.tenantroutingservice.services.IRoutingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("")
public class RoutingController {
    private static final Logger LOGGER = LoggerFactory.getLogger(RoutingController.class);

    @Autowired
    private IRoutingService routingService;

    @RequestMapping(
            value = "/{tenantId}",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public IngestionRoutingInformationOutput setTenantRoutingInformation(
            @PathVariable final String tenantId,
            @RequestBody final IngestionRoutingInformationInput ingestionRoutingInformationInput
    ){
        TenantRoutingInformation routingInformation =
                routingService.setIngestionRoutingInformation(tenantId, ingestionRoutingInformationInput);

        IngestionRoutingInformationOutput out = new IngestionRoutingInformationOutput();
        out.setPath(routingInformation.getIngestionPath());
        return out;
    }

    @RequestMapping(
            value = "/{tenantId}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public IngestionRoutingInformationOutput getTenantRoutingInformation(@PathVariable final String tenantId){
        return routingService.getIngestionRoutingInformation(tenantId);
    }
}
