package com.diskagua.api.mapper;

import com.diskagua.api.dto.AddressDTO;
import com.diskagua.api.models.Address;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * Uma interface auxiliar para converter objetos do tipo {@link Address} para
 * {@link AddressDTO}, ou vice-versa.
 *
 * @author Carlos
 */
@Mapper
public interface AddressMapper {

    public static AddressMapper INSTANCE = Mappers.getMapper(AddressMapper.class);

    /**
     * Converte um objeto do tipo {@link Address} para {@link AddressDTO}.
     *
     * @param address o endereço para ser convertido
     * @return o endereço utilizando o padrão de projeto DTO
     */
    public AddressDTO toDTO(Address address);

    /**
     * Converte um objeto do tipo {@link AddressDTO} para {@link Address}.
     *
     * @param addressDTO o endereço utilizando o padrão de projeto DTO
     * @return o endereço em seu formato de POJO
     */
    public Address toModel(AddressDTO addressDTO);
}
