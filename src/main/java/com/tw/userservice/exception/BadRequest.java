package com.tw.userservice.exception;

public class BadRequest extends RuntimeException {

    public  BadRequest(String message){
        super(message);
    }

}
