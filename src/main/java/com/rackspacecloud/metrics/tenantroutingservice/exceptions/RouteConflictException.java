package com.rackspacecloud.metrics.tenantroutingservice.exceptions;

/**
 * This exception is thrown when route information is already present for the given tenantId, and
 * user wants to overwrite.
 */
public class RouteConflictException extends RuntimeException {
    public RouteConflictException(String tenantId) {
        super("Route information is already present for tenantId [" + tenantId + "]." +
                " If you want to overwrite, please first delete current entry for the given tenantId.");
    }
}
