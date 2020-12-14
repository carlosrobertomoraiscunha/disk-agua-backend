package com.diskagua.api.mapper;

import com.diskagua.api.models.Product;
import com.diskagua.api.dto.request.ProductRequestDTO;
import com.diskagua.api.dto.response.ProductResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProductMapper {

    public static ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    public ProductRequestDTO toRequestDTO(Product product);

    public ProductResponseDTO toResponseDTO(Product product);

    public Product toModel(ProductRequestDTO addressDTO);

    public Product toModel(ProductResponseDTO addressDTO);
}
