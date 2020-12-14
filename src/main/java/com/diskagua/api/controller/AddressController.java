package com.diskagua.api.controller;

import com.diskagua.api.dto.request.AddressRequestDTO;
import com.diskagua.api.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/usuarios/{userId}/enderecos")
public class AddressController {

    private final AddressService addressService;

    @Autowired
    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @PostMapping
    public ResponseEntity createUserAddress(@PathVariable Long userId, @RequestBody AddressRequestDTO addressDTO) {
        return this.addressService.createUserAddress(userId, addressDTO);
    }

    @GetMapping
    public ResponseEntity listAllUserAddresses(@PathVariable Long userId) {
        return this.addressService.listAllUserAddresses(userId);
    }

    @GetMapping(path = "/{addressId}")
    public ResponseEntity findUserAddressById(@PathVariable Long userId, @PathVariable Long addressId) {
        return this.addressService.findUserAddressById(userId, addressId);
    }

    @DeleteMapping(path = "/{addressId}")
    public ResponseEntity deleteUserAddressById(@PathVariable Long userId, @PathVariable Long addressId) {
        return this.addressService.deleteUserAddressById(userId, addressId);
    }

    @PutMapping(path = "/{addressId}")
    public ResponseEntity updateUserAddressById(@PathVariable Long userId, @PathVariable Long addressId, @RequestBody AddressRequestDTO addressDTO) {
        return this.addressService.updateUserAddressById(userId, addressId, addressDTO);
    }
}
