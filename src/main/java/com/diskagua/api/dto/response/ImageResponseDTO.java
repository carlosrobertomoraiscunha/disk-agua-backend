package com.diskagua.api.dto.response;

import com.diskagua.api.models.Image;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Classe que implementa o padrão de projeto DTO, utilizado para enviar objetos
 * do tipo {@link Image}, apenas com os campos necessários.
 *
 * @author Carlos
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ImageResponseDTO {

    private Long id;

    @JsonProperty("nome")
    private String name;

    @JsonProperty("conteudo")
    private byte[] content;

    @JsonProperty("tipo")
    private String type;
}
