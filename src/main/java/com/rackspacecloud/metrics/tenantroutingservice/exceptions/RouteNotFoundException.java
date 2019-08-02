package com.rackspacecloud.metrics.tenantroutingservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * This exception is thrown when route is not found in a read-only request
 */
@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "route not found")
public class RouteNotFoundException extends RuntimeException {
    public RouteNotFoundException(String routeInformation) {
        super("Couldn't find route: [" + routeInformation + "]");
    }
}

