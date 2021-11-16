package com.tw.userservice.modle;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;




@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDetails {

    private String userId;

    private String name;

    private int age;

    private String cellphone;

    private String address;

    private String email;
}
