package com.weather.weather.model.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto  {

    @NotBlank
    private String name;

    @NotBlank
    private String lastname;

    private Integer age;
    @NotBlank
    private String username;

    @Email
    private String email;

}
