package com.diskagua.api.controller;

import com.diskagua.api.dto.request.AddressRequestDTO;
import com.diskagua.api.security.JwtAuthenticationFilter;
import com.diskagua.api.service.AddressService;
import com.diskagua.api.util.UrlConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = UrlConstants.USER_ADDRESS_URL)
public class AddressController {

    private final AddressService addressService;

    @Autowired
    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @PostMapping
    public ResponseEntity createUserAddress(@RequestHeader(JwtAuthenticationFilter.HEADER_STRING) String authorizationToken, @RequestBody AddressRequestDTO addressDTO) {
        return this.addressService.createUserAddress(authorizationToken, addressDTO);
    }

    @GetMapping
    public ResponseEntity listAllUserAddresses(@RequestHeader(JwtAuthenticationFilter.HEADER_STRING) String authorizationToken) {
        return this.addressService.listAllUserAddresses(authorizationToken);
    }

    @GetMapping(path = "/{addressId}")
    public ResponseEntity findUserAddressById(@RequestHeader(JwtAuthenticationFilter.HEADER_STRING) String authorizationToken, @PathVariable Long addressId) {
        return this.addressService.findUserAddressById(authorizationToken, addressId);
    }

    @DeleteMapping(path = "/{addressId}")
    public ResponseEntity deleteUserAddressById(@RequestHeader(JwtAuthenticationFilter.HEADER_STRING) String authorizationToken, @PathVariable Long addressId) {
        return this.addressService.deleteUserAddressById(authorizationToken, addressId);
    }

    @PutMapping(path = "/{addressId}")
    public ResponseEntity updateUserAddressById(@RequestHeader(JwtAuthenticationFilter.HEADER_STRING) String authorizationToken, @PathVariable Long addressId, @RequestBody AddressRequestDTO addressDTO) {
        return this.addressService.updateUserAddressById(authorizationToken, addressId, addressDTO);
    }
}
