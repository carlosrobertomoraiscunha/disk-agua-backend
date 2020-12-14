package com.diskagua.api.mapper;

import com.diskagua.api.dto.request.AddressRequestDTO;
import com.diskagua.api.dto.response.AddressResponseDTO;
import com.diskagua.api.models.Address;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AddressMapper {

    public static AddressMapper INSTANCE = Mappers.getMapper(AddressMapper.class);

    public AddressRequestDTO toRequestDTO(Address address);

    public AddressResponseDTO toResponseDTO(Address address);

    public Address toModel(AddressRequestDTO addressDTO);

    public Address toModel(AddressResponseDTO addressDTO);
}
