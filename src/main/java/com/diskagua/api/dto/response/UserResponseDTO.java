package com.diskagua.api.dto.response;

import com.diskagua.api.models.Role;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDTO {

    private Long id;

    @JsonProperty("email")
    private String email;

    @JsonProperty("imagem")
    private ImageResponseDTO image;

    @JsonProperty("nome")
    private String name;

    @JsonProperty("telefone")
    private String phoneNumber;

    @JsonProperty("cargo")
    private Role userRole;
}
