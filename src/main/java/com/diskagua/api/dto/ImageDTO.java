package com.diskagua.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Representa a classe {@link Image}, implementando o padrão de projeto DTO,
 * utilizado para intermediar a comunicação entre o repositório e o controlador.
 *
 * @author Carlos
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ImageDTO {

    @JsonIgnore
    private Long id;

    @NotEmpty
    @JsonProperty("nome")
    private String name;

    @NotEmpty
    @JsonProperty("conteudo")
    private byte[] content;

    @NotEmpty
    @JsonProperty("tipo")
    private String type;
}
