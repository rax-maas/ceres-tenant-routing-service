package com.rackspacecloud.metrics.tenantroutingservice.exceptions;

/**
 * This exception is thrown when it fails to write route information into the repository.
 */
public class RouteWriteException extends RuntimeException {
    public RouteWriteException(String routeInformation, Throwable e) {
        super("Couldn't write route information: [" + routeInformation + "]", e);
    }
}
