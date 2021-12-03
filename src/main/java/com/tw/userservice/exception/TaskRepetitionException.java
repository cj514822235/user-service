package com.tw.userservice.exception;

public class TaskRepetitionException extends RuntimeException {
    public TaskRepetitionException(String message){
        super(message);
    }

}
