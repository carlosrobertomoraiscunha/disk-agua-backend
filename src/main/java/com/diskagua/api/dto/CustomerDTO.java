package com.diskagua.api.dto;

import com.diskagua.api.models.Customer;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Representa a classe {@link Customer}, implementando o padrão de projeto DTO,
 * utilizado para intermediar a comunicação entre o repositório e o controlador.
 *
 * Extende a classe abstrata {@link UserDTO}, e se apropria de seus métodos.
 *
 * @author Carlos
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDTO extends UserDTO {

    @Valid
    @JsonProperty("enderecos")
    private List<AddressDTO> addresses;
}
