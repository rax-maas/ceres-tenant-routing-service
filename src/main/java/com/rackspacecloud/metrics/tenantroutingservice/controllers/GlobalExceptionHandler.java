package com.rackspacecloud.metrics.tenantroutingservice.controllers;

import com.rackspacecloud.metrics.tenantroutingservice.exceptions.RouteConflictException;
import com.rackspacecloud.metrics.tenantroutingservice.exceptions.RouteDeleteException;
import com.rackspacecloud.metrics.tenantroutingservice.exceptions.RouteNotFoundException;
import com.rackspacecloud.metrics.tenantroutingservice.exceptions.RouteWriteException;
import com.rackspacecloud.metrics.tenantroutingservice.model.ErrorInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Handle RouteNotFoundException
     * @param e
     * @return
     */
    @ExceptionHandler(RouteNotFoundException.class)
    public ResponseEntity<ErrorInfo> handle(final RouteNotFoundException e) {
        LOGGER.error(e.getMessage(), e);
        return new ResponseEntity<>(
                new ErrorInfo(e.getMessage(), getRootCause(e).getMessage()),
                HttpStatus.NOT_FOUND
        );
    }

    /**
     * Handle RouteConflictException
     * @param e
     * @return
     */
    @ExceptionHandler(RouteConflictException.class)
    public ResponseEntity<ErrorInfo> handle(final RouteConflictException e) {
        LOGGER.error(e.getMessage(), e);
        return new ResponseEntity<>(
                new ErrorInfo(e.getMessage(), getRootCause(e).getMessage()),
                HttpStatus.CONFLICT
        );
    }

    /**
     * Handle RouteWriteException
     * @param e
     * @return
     */
    @ExceptionHandler(RouteWriteException.class)
    public ResponseEntity<ErrorInfo> handle(final RouteWriteException e) {
        LOGGER.error(e.getMessage(), e);
        return new ResponseEntity<>(
                new ErrorInfo(e.getMessage(), getRootCause(e).getMessage()),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    /**
     * Handle RouteDeleteException
     * @param e
     * @return
     */
    @ExceptionHandler(RouteDeleteException.class)
    public ResponseEntity<ErrorInfo> handle(final RouteDeleteException e) {
        LOGGER.error(e.getMessage(), e);
        return new ResponseEntity<>(
                new ErrorInfo(e.getMessage(), getRootCause(e).getMessage()),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    /**
     * Handle MethodArgumentNotValidException
     * @param e
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorInfo> handle(MethodArgumentNotValidException e) {
        LOGGER.error(e.getMessage(), e);

        return new ResponseEntity<>(
                new ErrorInfo(e.getMessage(), getRootCause(e).getMessage()),
                HttpStatus.BAD_REQUEST
        );
    }

    /**
     * Handle any Exception
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorInfo> handle(final Exception e) {
        LOGGER.error(e.getMessage(), e);
        return new ResponseEntity<>(
                new ErrorInfo(e.getMessage(), getRootCause(e).getMessage()),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    /**
     * Dig root cause in the exception
     * @param e
     * @return
     */
    private Throwable getRootCause(Throwable e) {
        if(e == null) return e;

        Throwable cause = e.getCause();
        if(cause == null) {
            return e;
        }
        else {
            return getRootCause(cause);
        }
    }
}
