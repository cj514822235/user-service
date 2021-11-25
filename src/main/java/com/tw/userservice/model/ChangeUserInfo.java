package com.tw.userservice.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChangeUserInfo extends EntityBase {

    private Integer age;

    private String cellphone;

    private String address;

    private String email;


}
