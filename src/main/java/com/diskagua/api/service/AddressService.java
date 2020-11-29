package com.diskagua.api.service;

import com.diskagua.api.dto.AddressDTO;
import com.diskagua.api.mapper.AddressMapper;
import com.diskagua.api.models.Address;
import com.diskagua.api.repository.AddressRepository;
import com.diskagua.api.repository.CustomerRepository;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class AddressService {

    private final CustomerRepository customerRepository;
    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper = AddressMapper.INSTANCE;

    /**
     * Constrói um serviço, utilizando a injeção de dependências do Spring.
     *
     * @param customerRepository repositório que armazena os dados do cliente
     * @param addressRepository repositório que armazena os dados dos endereços
     */
    @Autowired
    public AddressService(CustomerRepository customerRepository, AddressRepository addressRepository) {
        this.customerRepository = customerRepository;
        this.addressRepository = addressRepository;
    }

    /**
     * Converte um endereço do padrão DTO e salva-o no repositório de um
     * determinado cliente.
     *
     * @param customerId identificador (id) do cliente
     * @param addressDTO o endereço no padrão DTO
     * @return um objeto {@link ResponseEntity} com o status da resposta e o
     * endereço salvo em formato JSON
     */
    public ResponseEntity createAddressForCustomer(Long customerId, AddressDTO addressDTO) {
        if (!verifyIfCustomerExistsById(customerId)) {
            return ResponseEntity.notFound().build();
        }

        Address addressToSave = this.addressMapper.toModel(addressDTO);

        Address savedAddress = this.customerRepository
                .findById(customerId).map((customer) -> {
            addressToSave.setCustomer(customer);

            return this.addressRepository.save(addressToSave);
        }).get();

        AddressDTO savedAddressDTO = this.addressMapper.toDTO(savedAddress);
        URI location = URI.create("/api/v1/clientes" + savedAddress.getId());

        return ResponseEntity.created(location).body(savedAddressDTO);
    }

    /**
     * Busca todos os endereços de uma determinado cliente salvos no
     * repositório.
     *
     * @param customerId identificador (id) do cliente
     * @return um objeto {@link ResponseEntity} com o status da resposta e a
     * lista de endereços encontrados em formato JSON
     */
    public ResponseEntity listAllAddressesForCustomer(Long customerId) {
        if (!verifyIfCustomerExistsById(customerId)) {
            return ResponseEntity.notFound().build();
        }

        List<Address> allAddresses = this.addressRepository.listAllByCustomerId(customerId);
        List<AddressDTO> allAddressesDTO = allAddresses.stream().map((address) -> {
            return this.addressMapper.toDTO(address);
        }).collect(Collectors.toList());

        return ResponseEntity.ok(allAddressesDTO);
    }

    /**
     * Busca um endereço específico de um determinado cliente no repositório.
     *
     * @param customerId identificador (id) do cliente
     * @param addressId identificador (id) do endereço
     * @return um objeto {@link ResponseEntity} com o status da resposta e o
     * endereço encontrado em formato JSON
     */
    public ResponseEntity findAddressForCustomerById(Long customerId, Long addressId) {
        Optional<AddressDTO> foundAddressDTO = this.addressRepository
                .findByIdAndCustomerId(addressId, customerId).map((address) -> {
            return this.addressMapper.toDTO(address);
        });

        return ResponseEntity.of(foundAddressDTO);
    }

    /**
     * Deleta um endereço do repositório.
     *
     * @param customerId identificador (id) do cliente
     * @param addressId identificador (id) do endereço
     * @return um objeto {@link ResponseEntity} com o status da resposta
     */
    public ResponseEntity deleteAddressForCustomer(Long customerId, Long addressId) {
        if (!verifyIfAddressExistsInCustomer(customerId, addressId)) {
            return ResponseEntity.notFound().build();
        }

        this.addressRepository.deleteById(addressId);

        return ResponseEntity.noContent().build();
    }

    /**
     * Atualiza um endereço no repositório.
     *
     * @param customerId identificador (id) do cliente
     * @param addressId identificador (id) do endereço
     * @param addressDTO o endereço atualizado no padrão DTO
     * @return um objeto {@link ResponseEntity} com o status da resposta e o
     * endereço atualizado em formato JSON
     */
    public ResponseEntity updateAddressForCustomer(Long customerId, Long addressId, AddressDTO addressDTO) {
        Address addressToUpdate = this.addressMapper.toModel(addressDTO);

        Optional<AddressDTO> updatedAddressDTO = this.addressRepository
                .findById(addressId).map((foundAddress) -> {
            foundAddress.setCity(addressToUpdate.getCity());
            foundAddress.setComplement(addressToUpdate.getComplement());
            foundAddress.setDistrict(addressToUpdate.getDistrict());
            foundAddress.setNumber(addressToUpdate.getNumber());
            foundAddress.setPostalCode(addressToUpdate.getPostalCode());
            foundAddress.setState(addressToUpdate.getState());
            foundAddress.setStreet(addressToUpdate.getStreet());

            Address savedAddress = this.addressRepository.save(foundAddress);
            AddressDTO savedAddressDTO = this.addressMapper.toDTO(savedAddress);

            return savedAddressDTO;
        });

        return ResponseEntity.of(updatedAddressDTO);
    }

    private boolean verifyIfCustomerExistsById(Long id) {
        return this.customerRepository.existsById(id);
    }

    private boolean verifyIfAddressExistsInCustomer(Long customerId, Long addressId) {
        if (verifyIfCustomerExistsById(customerId)) {
            return this.addressRepository.existsById(addressId);
        } else {
            return false;
        }
    }
}
