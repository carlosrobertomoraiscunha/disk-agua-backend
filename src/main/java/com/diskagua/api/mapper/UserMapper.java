package com.diskagua.api.mapper;

import com.diskagua.api.models.User;
import com.diskagua.api.dto.request.UserRequestDTO;
import com.diskagua.api.dto.response.UserResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {

    public static UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    public UserRequestDTO toRequestDTO(User user);

    public UserResponseDTO toResponseDTO(User user);

    public User toModel(UserRequestDTO addressDTO);

    public User toModel(UserResponseDTO addressDTO);
}
