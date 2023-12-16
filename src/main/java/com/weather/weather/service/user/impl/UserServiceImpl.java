package com.weather.weather.service.user.impl;

import com.weather.weather.exception.DuplicationException;
import com.weather.weather.exception.ResourceNotFoundException;
import com.weather.weather.mapper.UserMapper;
import com.weather.weather.model.user.UserEntity;
import com.weather.weather.model.user.dto.UserCreateRequest;
import com.weather.weather.model.user.dto.UserDto;
import com.weather.weather.repository.UserRepository;
import com.weather.weather.service.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Transactional
    @Override
    public UserDto create(UserCreateRequest userCreateRequest, HttpServletRequest httpServletRequest)  {
        if(userRepository.findByUsername(userCreateRequest.getUsername()) != null) {
            throw new DuplicationException(String.format("User by username: %s already exists", userCreateRequest.getUsername()));
        }
        UserEntity userEntity = userMapper.toEntity(userCreateRequest);
        userEntity.setIP(getClientIP(httpServletRequest));
        userRepository.save(userEntity);
        return userMapper.toDto(userEntity);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        userRepository.deleteById(id);
    }

    @Override
    @Transactional
    public UserDto update(UserEntity userEntity) {
        UserEntity findById = userRepository.findById(userEntity.getId()).orElseThrow(() ->
                new ResourceNotFoundException(String.format("User by id: %d does not exist", userEntity.getId())));
        findById.setLastname(userEntity.getLastname());
        findById.setName(userEntity.getName());
        findById.setAge(userEntity.getAge());
        findById.setEmail(userEntity.getEmail());
        findById.setPassword(userEntity.getPassword());
        findById.setUsername(userEntity.getUsername());
        userRepository.save(findById);
        return userMapper.toDto(findById);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto findById(Integer id) {
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(String.format("User by id: %d not found", id)));
        return userMapper.toDto(userEntity);
    }


    @Override
    @Transactional(readOnly = true)
    public UserDto findByUsername(String username) {

        UserEntity userEntity = userRepository.findByUsername(username);

        if(userEntity == null) {
            throw new ResourceNotFoundException(String.format("User by username: %s does not exist", username));
        }

        return userMapper.toDto(userEntity);
    }

    @Transactional(readOnly = true)
    @Override
    public UserDto findByEmail(String email) {

        UserEntity userEntity = userRepository.findByEmail(email);

        if(userEntity == null) {
            throw new ResourceNotFoundException(String.format("User by Email: %s does not exist", email));
        }

        return userMapper.toDto(userEntity);
    }

    @Transactional(readOnly = true)
    @Override
    public List<UserDto> findAll() {
            return userRepository.findAll().stream().map(userMapper::toDto).toList();
    }

    private String getClientIP(HttpServletRequest request)  {
        String ipAddress = request.getHeader("X-Forwarded-For");
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
        } else {
            ipAddress = ipAddress.split(",")[0].trim();
        }
        return ipAddress;
    }


}
