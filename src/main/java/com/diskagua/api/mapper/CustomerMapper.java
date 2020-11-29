package com.diskagua.api.mapper;

import com.diskagua.api.dto.CustomerDTO;
import com.diskagua.api.models.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * Uma interface auxiliar para converter objetos do tipo {@link Customer} para
 * {@link CustomerDTO}, ou vice-versa.
 *
 * @author Carlos
 */
@Mapper
public interface CustomerMapper {

    public static CustomerMapper INSTANCE = Mappers.getMapper(CustomerMapper.class);

    /**
     * Converte um objeto do tipo {@link Customer} para {@link CustomerDTO}.
     *
     * @param customer o cliente para ser convertido
     * @return o cliente utilizando o padrão de projeto DTO
     */
    public CustomerDTO toDTO(Customer customer);

    /**
     * Converte um objeto do tipo {@link CustomerDTO} para {@link Customer}.
     *
     * @param customerDTO o cliente utilizando o padrão de projeto DTO
     * @return o cliente em seu formato de POJO
     */
    public Customer toModel(CustomerDTO customerDTO);
}
