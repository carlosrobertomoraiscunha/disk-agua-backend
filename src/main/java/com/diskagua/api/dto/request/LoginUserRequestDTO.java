package com.diskagua.api.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginUserRequestDTO {

    @Email
    @NotEmpty
    private String email;

    @NotEmpty
    @JsonProperty("senha")
    private String password;
}
