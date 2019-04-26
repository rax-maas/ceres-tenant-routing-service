package com.rackspacecloud.metrics.tenantroutingservice.controllers;

import com.rackspacecloud.metrics.tenantroutingservice.domain.TenantRoutes;
import com.rackspacecloud.metrics.tenantroutingservice.services.RoutingService;
import io.micrometer.core.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.Collection;

@RestController
@RequestMapping("")
public class RoutingController {
    private static final Logger LOGGER = LoggerFactory.getLogger(RoutingController.class);

    @Autowired
    private RoutingService routingService;

    /**
     * Will return the routes for the given tenantId and measurement
     * @param tenantId
     * @param measurement
     * @return
     */
    @RequestMapping(
            value = "/{tenantId}/{measurement}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Timed(value = "tenant.routing", extraTags = {"operation","get"})
    public TenantRoutes getTenantRoutingInformation(
            @NotNull @PathVariable final String tenantId,
            @NotNull @PathVariable final String measurement) throws Exception {
        LOGGER.debug("getTenantRoutingInformation: get routing request received for tenantId [{}]" +
                " and measurement [{}]", tenantId, measurement);
        return routingService.getIngestionRoutingInformation(tenantId, measurement);
    }

    /**
     * Get all measurements for the given tenantId
     * @param tenantId
     */
    @RequestMapping(
            value = "/{tenantId}/measurements",
            method = RequestMethod.GET
    )
    @Timed(value = "tenant.routing", extraTags = {"operation","get"})
    public Collection<String> getMeasurements(@NotNull @PathVariable final String tenantId){
        LOGGER.info("getMeasurements: get measurements request received for tenantId [{}]", tenantId);
        return routingService.getMeasurements(tenantId);
    }
}
