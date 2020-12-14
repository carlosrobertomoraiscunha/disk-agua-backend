package com.diskagua.api.service;

import com.diskagua.api.dto.request.AddressRequestDTO;
import com.diskagua.api.dto.response.AddressResponseDTO;
import com.diskagua.api.mapper.AddressMapper;
import com.diskagua.api.models.Address;
import com.diskagua.api.models.User;
import com.diskagua.api.repository.AddressRepository;
import com.diskagua.api.repository.UserRepository;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class AddressService {

    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper = AddressMapper.INSTANCE;

    @Autowired
    public AddressService(UserRepository userRepository, AddressRepository addressRepository) {
        this.userRepository = userRepository;
        this.addressRepository = addressRepository;
    }

    public ResponseEntity createUserAddress(Long userId, AddressRequestDTO addressDTO) {
        Optional<User> foundUser = this.userRepository.findById(userId);

        if (foundUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Address addressToSave = this.addressMapper.toModel(addressDTO);
        Address savedAddress = foundUser.map((user) -> {
            addressToSave.setUser(user);

            return this.addressRepository.save(addressToSave);
        }).orElseThrow();

        AddressResponseDTO savedAddressDTO = this.addressMapper.toResponseDTO(savedAddress);
        URI location = URI.create("/api/v1/usuarios/"
                + savedAddress.getUser().getId()
                + "/enderecos/"
                + savedAddress.getId());

        return ResponseEntity.created(location).body(savedAddressDTO);
    }

    public ResponseEntity listAllUserAddresses(Long userId) {
        if (!verifyIfUserExistsById(userId)) {
            return ResponseEntity.notFound().build();
        }

        List<Address> allAddresses = this.addressRepository.listAllUserAddressesById(userId);
        List<AddressResponseDTO> allAddressesDTO = allAddresses.stream().map((address) -> {
            return this.addressMapper.toResponseDTO(address);
        }).collect(Collectors.toList());

        return ResponseEntity.ok(allAddressesDTO);
    }

    public ResponseEntity findUserAddressById(Long userId, Long addressId) {
        Optional<AddressResponseDTO> foundAddressDTO = this.addressRepository
                .findUserAddressById(userId, addressId).map((address) -> {
            return this.addressMapper.toResponseDTO(address);
        });

        return ResponseEntity.of(foundAddressDTO);
    }

    public ResponseEntity deleteUserAddressById(Long userId, Long addressId) {
        if (!verifyIfUserAddressExistsById(userId, addressId)) {
            return ResponseEntity.notFound().build();
        }

        this.addressRepository.deleteById(addressId);

        return ResponseEntity.noContent().build();
    }

    public ResponseEntity updateUserAddressById(Long userId, Long addressId, AddressRequestDTO addressDTO) {
        Address addressToUpdate = this.addressMapper.toModel(addressDTO);

        Optional<AddressRequestDTO> updatedAddressDTO = this.addressRepository
                .findById(addressId).map((foundAddress) -> {
            foundAddress.setCity(addressToUpdate.getCity());
            foundAddress.setComplement(addressToUpdate.getComplement());
            foundAddress.setDistrict(addressToUpdate.getDistrict());
            foundAddress.setNumber(addressToUpdate.getNumber());
            foundAddress.setPostalCode(addressToUpdate.getPostalCode());
            foundAddress.setState(addressToUpdate.getState());
            foundAddress.setStreet(addressToUpdate.getStreet());

            Address savedAddress = this.addressRepository.save(foundAddress);
            AddressRequestDTO savedAddressDTO = this.addressMapper.toRequestDTO(savedAddress);

            return savedAddressDTO;
        });

        return ResponseEntity.of(updatedAddressDTO);
    }

    private boolean verifyIfUserExistsById(Long id) {
        return this.userRepository.existsById(id);
    }

    private boolean verifyIfUserAddressExistsById(Long userId, Long addressId) {
        return this.addressRepository.findUserAddressById(userId, addressId).isPresent();
    }
}
