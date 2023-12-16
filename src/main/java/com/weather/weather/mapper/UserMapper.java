package com.weather.weather.mapper;


import com.weather.weather.model.user.UserEntity;
import com.weather.weather.model.user.dto.UserCreateRequest;
import com.weather.weather.model.user.dto.UserDto;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface UserMapper {

    UserDto toDto(UserEntity userEntity);

    UserEntity toEntity(UserCreateRequest userCreateRequest);


}
