package com.rackspacecloud.metrics.tenantroutingservice.controllers;

import com.rackspacecloud.metrics.tenantroutingservice.domain.TenantRoutes;
import com.rackspacecloud.metrics.tenantroutingservice.model.IngestionRoutingInformationInput;
import com.rackspacecloud.metrics.tenantroutingservice.services.RoutingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("")
public class RoutingController {
    private static final Logger LOGGER = LoggerFactory.getLogger(RoutingController.class);

    @Autowired
    private RoutingService routingService;


    /**
     * This endpoint creates all of the default routes for this tenantId.
     * JSON needs to provide the "path" and "databaseName" for the default routes.
     * @param tenantId
     * @param routingInfo
     * @return full list of routing data
     */
    @RequestMapping(
            value = "/{tenantId}",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public TenantRoutes setTenantRoutingInformation(
            @NotNull @PathVariable final String tenantId,
            @Valid @RequestBody final IngestionRoutingInformationInput routingInfo
    ){
        LOGGER.info("setTenantRoutingInformation: Set routing request received for tenantId [{}]", tenantId);
        LOGGER.debug("Routing input is [{}]", routingInfo);

        TenantRoutes routingInformation = routingService.setIngestionRoutingInformation(tenantId, routingInfo);

        return routingInformation;
    }

    /**
     * Will return the routes for the given tenantId
     * @param tenantId
     * @return
     */
    @RequestMapping(
            value = "/{tenantId}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public TenantRoutes getTenantRoutingInformation(@NotNull @PathVariable final String tenantId){
        LOGGER.info("getTenantRoutingInformation: get routing request received for tenantId [{}]", tenantId);
        return routingService.getIngestionRoutingInformation(tenantId);
    }

    /**
     * Deletes all routing data for the given tenantId
     * @param tenantId
     */
    @RequestMapping(
            value = "/{tenantId}",
            method = RequestMethod.DELETE
    )
    public void removeTenantRoutingInformation(@NotNull @PathVariable final String tenantId){
        LOGGER.info("removeTenantRoutingInformation: delete routing request received for tenantId [{}]", tenantId);
        routingService.removeIngestionRoutingInformation(tenantId);
    }
}
