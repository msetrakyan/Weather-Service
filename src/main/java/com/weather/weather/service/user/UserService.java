package com.weather.weather.service.user;

import com.weather.weather.model.user.UserEntity;
import com.weather.weather.model.user.dto.UserCreateRequest;
import com.weather.weather.model.user.dto.UserDto;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface UserService {

    UserDto create(UserCreateRequest userEntity, HttpServletRequest httpServletRequest);

    void delete(Integer id);

    UserDto update(UserEntity userEntity);

    UserDto findById(Integer id);

    UserDto findByUsername(String username);

    UserDto findByEmail(String email);

    List<UserDto> findAll();





}
