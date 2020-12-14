package com.diskagua.api.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDTO {

    @Email
    @NotEmpty
    @JsonProperty("email")
    private String email;

    @NotEmpty
    @JsonProperty("senha")
    private String password;

    @Valid
    @NotNull
    @JsonProperty("imagem")
    private ImageRequestDTO image;

    @NotEmpty
    @JsonProperty("nome")
    private String name;

    @NotEmpty
    @JsonProperty("telefone")
    private String phoneNumber;
}
