package com.diskagua.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Representa a classe {@link Address}, implementando o padrão de projeto DTO,
 * utilizado para intermediar a comunicação entre o repositório e o controlador.
 *
 * @author Carlos
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddressDTO {

    @JsonIgnore
    private Long id;

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
