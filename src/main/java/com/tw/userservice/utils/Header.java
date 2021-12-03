package com.tw.userservice.utils;

import org.springframework.stereotype.Component;

import java.util.Base64;

@Component
public class Header { //todo


    public String getToken(String originalInput) {

        return  Base64.getEncoder().encodeToString(originalInput.getBytes());

    }

    public String parsingToken(String token){

        byte[] decodedBytes = Base64.getDecoder().decode(token);
        return new String(decodedBytes);
    }



}
