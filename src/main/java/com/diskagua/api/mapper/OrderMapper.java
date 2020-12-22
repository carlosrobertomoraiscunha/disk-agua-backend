package com.diskagua.api.mapper;

import com.diskagua.api.dto.response.OrderResponseDTO;
import com.diskagua.api.models.OrderEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OrderMapper {

    public static OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    public OrderResponseDTO toResponseDTO(OrderEntity order);

    public OrderEntity toModel(OrderResponseDTO orderDTO);
}
