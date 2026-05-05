package com.codewithmiki.springbootblackjack.exception;

import com.codewithmiki.springbootblackjack.dto.ErrorResponseRecord;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<@NonNull ErrorResponseRecord> handleIllegalArgument(IllegalArgumentException ex) {
        ErrorResponseRecord error = new ErrorResponseRecord(ex.getMessage(), HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<@NonNull ErrorResponseRecord> handleIllegalStateException(IllegalStateException ex) {
        ErrorResponseRecord error = new ErrorResponseRecord(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
