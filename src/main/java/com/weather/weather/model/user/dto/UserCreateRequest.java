package com.weather.weather.model.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateRequest {

    private  String name;

    private String lastname;

    private String username;

    private String password;

    private String email;

    private Integer age;

}
