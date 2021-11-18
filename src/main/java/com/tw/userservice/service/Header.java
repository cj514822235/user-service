package com.tw.userservice.service;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
@Component
public class Header {


    public String getToken(String originalInput) {

        return  Base64.getEncoder().encodeToString(originalInput.getBytes());

    }

    public String parsingToken(String token){

        byte[] decodedBytes = Base64.getDecoder().decode(token);
        return new String(decodedBytes);
    }



}