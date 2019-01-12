package com.rackspacecloud.metrics.tenantroutingservice.controllers;

import com.rackspacecloud.metrics.tenantroutingservice.domain.TenantRoutes;
import com.rackspacecloud.metrics.tenantroutingservice.model.IngestionRoutingInformationInput;
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
    public TenantRoutes setTenantRoutingInformation(
            @PathVariable final String tenantId,
            @RequestBody final IngestionRoutingInformationInput ingestionRoutingInformationInput
    ){
        TenantRoutes routingInformation =
                routingService.setIngestionRoutingInformation(tenantId, ingestionRoutingInformationInput);

        return routingInformation;
    }

    @RequestMapping(
            value = "/full/{tenantId}",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public TenantRoutes fullCreateTenantRoutingData(
            @PathVariable final String tenantId,
            @RequestBody final TenantRoutes ingestionRoutingInformationInput
    ){
        TenantRoutes routingInformation =
                routingService.setIngestionRoutingInformation(tenantId, ingestionRoutingInformationInput);

        return routingInformation;
    }



    @RequestMapping(
            value = "/{tenantId}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public TenantRoutes getTenantRoutingInformation(@PathVariable final String tenantId){
        return routingService.getIngestionRoutingInformation(tenantId);
    }

    @RequestMapping(
            value = "/{tenantId}",
            method = RequestMethod.DELETE
    )
    public void removeTenantRoutingInformation(@PathVariable final String tenantId){
        routingService.removeIngestionRoutingInformation(tenantId);
    }



    @RequestMapping(
            value = "/test/{tenantId}",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public TenantRoutes setTenantRoutingInformation(@PathVariable final String tenantId, @PathVariable String databasePath){
        return routingService.getIngestionRoutingInformation(tenantId);
    }
}
