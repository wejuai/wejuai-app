package com.wejuai.app.config;

import com.endofmaster.commons.util.validate.ValidateParamIsNullException;
import com.endofmaster.commons.util.validate.ValidateStringIsBlankException;
import com.endofmaster.rest.exceptionhandler.RestExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.ResourceAccessException;

/**
 * @author YQ.Huang
 */
@RestControllerAdvice
class ErrorControllerAdvice extends RestExceptionHandler {

    private final static Logger logger = LoggerFactory.getLogger(ErrorControllerAdvice.class);

    @ExceptionHandler(ResourceAccessException.class)
    public ResponseEntity<Object> handleException(ResourceAccessException e) {
        logger.error("Handle ResourceAccessException", e);
        return new ResponseEntity<>("服务暂不可用，请稍后再试", new HttpHeaders(), HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(ValidateStringIsBlankException.class)
    public ResponseEntity<Object> handleException(ValidateStringIsBlankException e) {
        logger.error("Handle ValidateStringIsBlankException", e);
        return new ResponseEntity<>(e.getLocalizedMessage(), new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ValidateParamIsNullException.class)
    public ResponseEntity<Object> handleException(ValidateParamIsNullException e) {
        logger.error("Handle ValidateParamIsNullException", e);
        return new ResponseEntity<>(e.getLocalizedMessage(), new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

}
