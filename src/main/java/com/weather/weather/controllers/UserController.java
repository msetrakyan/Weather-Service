package com.weather.weather.controllers;

import com.weather.weather.mapper.UserMapper;
import com.weather.weather.model.user.dto.UserCreateRequest;
import com.weather.weather.model.user.dto.UserDto;
import com.weather.weather.service.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping
    public ResponseEntity<List<UserDto>> getUsers() {
        return ResponseEntity.ok(userService.findAll());
    }

    @PostMapping
    public ResponseEntity<UserDto> create(@RequestBody UserCreateRequest userCreateRequest,
                                          HttpServletRequest httpServletRequest) {
        return ResponseEntity.ok(userService.create(userCreateRequest, httpServletRequest));
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Integer id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<UserDto> delete(@PathVariable Integer id) {
        userService.delete(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping
    public ResponseEntity<UserDto> update(@RequestBody UserCreateRequest userCreateRequest) {
        return ResponseEntity.ok(userService.update(userMapper.toEntity(userCreateRequest)));
    }



















}
