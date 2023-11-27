package com.fmm.controller;

/*
@RestController
@ControllerAdvice
public class GlobalExceptionHandler {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler({ Exception.class })
    public ResponseEntity<Object> handleAll(Exception ex, WebRequest request) {
        logger.error(ex.getLocalizedMessage());

        ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, ex.getLocalizedMessage(), "error occurred");

        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }
}


 */