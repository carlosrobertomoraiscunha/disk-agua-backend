package com.diskagua.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Representa a classe {@link User}, implementando o padrão de projeto DTO,
 * utilizado para intermediar a comunicação entre o repositório e o controlador.
 *
 * @author Carlos
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class UserDTO {

    @JsonIgnore
    private Long id;

    @Email
    @NotEmpty
    @JsonProperty("email")
    private String email;

    @JsonProperty("senha")
    private String password;

    @Valid
    @NotNull
    @JsonProperty("imagem")
    private ImageDTO image;

    @NotEmpty
    @JsonProperty("nome")
    private String name;

    @NotEmpty
    @JsonProperty("telefone")
    private String phoneNumber;
}
