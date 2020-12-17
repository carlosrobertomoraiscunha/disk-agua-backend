package com.diskagua.api.service;

import com.diskagua.api.dto.request.AddressRequestDTO;
import com.diskagua.api.dto.response.AddressResponseDTO;
import com.diskagua.api.mapper.AddressMapper;
import com.diskagua.api.models.Address;
import com.diskagua.api.models.User;
import com.diskagua.api.repository.AddressRepository;
import com.diskagua.api.repository.UserRepository;
import com.diskagua.api.util.TokenUtils;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    public ResponseEntity createUserAddress(String authorizationToken, AddressRequestDTO addressDTO) {
        try {
            String email = TokenUtils.getEmailFromToken(authorizationToken);
            String role = TokenUtils.getRoleFromToken(authorizationToken);

            Optional<User> foundUser = this.userRepository.findUserByEmail(email);

            if (foundUser.isEmpty()) {
                return ResponseEntity.notFound().build();
            }


            switch (role) {
                case "ROLE_CLIENTE": {
                    Address addressToSave = this.addressMapper.toModel(addressDTO);
                    Address savedAddress = foundUser.map((user) -> {
                        addressToSave.setUser(user);

                        return this.addressRepository.save(addressToSave);
                    }).orElseThrow();

                    AddressResponseDTO savedAddressDTO = this.addressMapper.toResponseDTO(savedAddress);

                    return ResponseEntity.ok(savedAddressDTO);
                }

                case "ROLE_VENDEDOR": {
                    if(this.addressRepository.listAllUserAddressesById(foundUser.get().getId()).isEmpty()) {
                        Address addressToSave = this.addressMapper.toModel(addressDTO);
                        Address savedAddress = foundUser.map((user) -> {
                            addressToSave.setUser(user);

                            return this.addressRepository.save(addressToSave);
                        }).orElseThrow();

                        AddressResponseDTO savedAddressDTO = this.addressMapper.toResponseDTO(savedAddress);

                        return ResponseEntity.ok(savedAddressDTO);
                    } else {
                        return ResponseEntity.status(HttpStatus.CONFLICT).body("Esse vendedor já possui um endereço");
                    }
                }

                default: {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
                }
            }
        } catch (Exception ex) {
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity listAllUserAddresses(String authorizationToken) {
        try{
            String email = TokenUtils.getEmailFromToken(authorizationToken);

            if (!verifyIfUserExistsByEmail(email)) {
                return ResponseEntity.notFound().build();
            }

            User user = this.userRepository.findUserByEmail(email).get();

            List<Address> allAddresses = this.addressRepository.listAllUserAddressesById(user.getId());
            List<AddressResponseDTO> allAddressesDTO = allAddresses.stream().map((address) -> {
                return this.addressMapper.toResponseDTO(address);
            }).collect(Collectors.toList());

            return ResponseEntity.ok(allAddressesDTO);
        } catch (Exception ex) {
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity findUserAddressById(String authorizationToken, Long addressId) {
        try {
            String email = TokenUtils.getEmailFromToken(authorizationToken);
            
            if (!verifyIfUserExistsByEmail(email)) {
                return ResponseEntity.notFound().build();
            }

            User user = this.userRepository.findUserByEmail(email).get();
            
            Optional<AddressResponseDTO> foundAddressDTO = this.addressRepository
                    .findUserAddressById(user.getId(), addressId).map((address) -> {
                return this.addressMapper.toResponseDTO(address);
            });

            return ResponseEntity.of(foundAddressDTO);
        } catch (Exception ex) {
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity deleteUserAddressById(String authorizationToken, Long addressId) {
        try{
            String email = TokenUtils.getEmailFromToken(authorizationToken);
            
            if (!verifyIfUserExistsByEmail(email)) {
                return ResponseEntity.notFound().build();
            }

            User user = this.userRepository.findUserByEmail(email).get();

            if (!verifyIfUserAddressExistsById(user.getId(), addressId)) {
                return ResponseEntity.notFound().build();
            }

            this.addressRepository.deleteById(addressId);

            return ResponseEntity.noContent().build();
        } catch (Exception ex) {
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity updateUserAddressById(String authorizationToken, Long addressId, AddressRequestDTO addressDTO) {
        try {
            String email = TokenUtils.getEmailFromToken(authorizationToken);
            
            if (!verifyIfUserExistsByEmail(email)) {
                return ResponseEntity.notFound().build();
            }

            User user = this.userRepository.findUserByEmail(email).get();
            
            if (!verifyIfUserAddressExistsById(user.getId(), addressId)) {
                return ResponseEntity.notFound().build();
            }
            
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
        } catch (Exception ex) {
            return ResponseEntity.notFound().build();
        }
    }

    private boolean verifyIfUserExistsByEmail(String email) {
        return this.userRepository.existsUserByEmail(email);
    }

    private boolean verifyIfUserAddressExistsById(Long userId, Long addressId) {
        return this.addressRepository.findUserAddressById(userId, addressId).isPresent();
    }
}
