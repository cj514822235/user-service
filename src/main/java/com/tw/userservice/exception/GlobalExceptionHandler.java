package com.tw.userservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Set;




@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity<ErrorResult> handle(AuthorizationException ex){
        ErrorResult errorResult = new ErrorResult(401,HttpStatus.UNAUTHORIZED, ex.getMessage());

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResult);

    }
    @ExceptionHandler(BadRequest.class)
    public ResponseEntity<ErrorResult> handle(BadRequest ex) {
        ErrorResult errorResult = new ErrorResult(400, HttpStatus.BAD_REQUEST, ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResult);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResult> handle(UserNotFoundException ex) {
        ErrorResult errorResult = new ErrorResult(404, HttpStatus.NOT_FOUND, ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResult);
    }


    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<ErrorResult> handle(TaskNotFoundException ex) {
        ErrorResult errorResult = new ErrorResult(404, HttpStatus.NOT_FOUND, ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResult);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResult> handle(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldError().getDefaultMessage();
        ErrorResult errorResult = new ErrorResult(404, HttpStatus.BAD_REQUEST,message);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResult);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResult> handle(ConstraintViolationException ex) {
        Set<ConstraintViolation<?>> violations = ex.getConstraintViolations();

        String message = "";
        for (ConstraintViolation<?> constraint : ex.getConstraintViolations()) {
            message = constraint.getMessage();
            break;
        }
        ErrorResult errorResult = new ErrorResult(404, HttpStatus.BAD_REQUEST, message);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResult);
    }
}
