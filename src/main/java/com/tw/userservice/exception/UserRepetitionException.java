package com.tw.userservice.exception;

public class UserRepetitionException extends RuntimeException{
    public UserRepetitionException(String message){
        super(message);
    }
}
