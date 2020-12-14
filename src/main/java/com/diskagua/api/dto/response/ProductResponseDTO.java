package com.diskagua.api.dto.response;

import com.diskagua.api.dto.request.ImageRequestDTO;
import com.diskagua.api.models.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponseDTO {

    private Long id;

    @JsonProperty("imagem")
    private ImageRequestDTO image;

    @JsonProperty("nome")
    private String name;

    @JsonProperty("preco")
    private BigDecimal price;

    @JsonProperty("descricao")
    private String description;

    @JsonProperty("vendedor")
    private UserResponseDTO user;
}
