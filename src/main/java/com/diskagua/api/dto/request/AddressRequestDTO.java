package com.diskagua.api.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddressRequestDTO {

    @NotEmpty
    @JsonProperty("cep")
    private String postalCode;

    @NotEmpty
    @JsonProperty("estado")
    private String state;

    @NotEmpty
    @JsonProperty("cidade")
    private String city;

    @NotEmpty
    @JsonProperty("bairro")
    private String district;

    @NotEmpty
    @JsonProperty("rua")
    private String street;

    @NotEmpty
    @JsonProperty("numero")
    private String number;

    @NotEmpty
    @JsonProperty("complemento")
    private String complement;
}
