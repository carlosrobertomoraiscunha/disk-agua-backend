package com.diskagua.api.controller;

import com.diskagua.api.dto.request.LoginUserRequestDTO;
import com.diskagua.api.dto.request.UserRequestDTO;
import com.diskagua.api.security.JwtAuthenticationFilter;
import com.diskagua.api.service.UserService;
import com.diskagua.api.util.UrlConstants;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(path = UrlConstants.USER_CUSTOMER_URL)
    public ResponseEntity registerCustomer(@RequestBody @Valid UserRequestDTO userDTO) {
        return this.userService.register("CLIENTE", userDTO);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(path = UrlConstants.USER_VENDOR_URL)
    public ResponseEntity registerVendor(@RequestBody @Valid UserRequestDTO userDTO) {
        return this.userService.register("VENDEDOR", userDTO);
    }

    @PostMapping(path = UrlConstants.USER_LOGIN_URL)
    public ResponseEntity login(@RequestBody LoginUserRequestDTO loginDTO) {
        return this.userService.login(loginDTO);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(path = UrlConstants.USER_ADMIN_URL)
    public ResponseEntity listAllUsers() {
        return this.userService.listAllUsers();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(path = UrlConstants.USER_ADMIN_URL + "/{id}")
    public ResponseEntity findUserById(@PathVariable Long id) {
        return this.userService.findUserById(id);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_CLIENTE', 'ROLE_VENDEDOR')")
    @GetMapping(path = UrlConstants.USER_URL)
    public ResponseEntity findUserByToken(@RequestHeader(JwtAuthenticationFilter.HEADER_STRING) String authorizationToken) {
        return this.userService.findUserByToken(authorizationToken);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_CLIENTE', 'ROLE_VENDEDOR')")
    @DeleteMapping(path = UrlConstants.USER_URL)
    public ResponseEntity deleteUserByToken(@RequestHeader(JwtAuthenticationFilter.HEADER_STRING) String authorizationToken) {
        return this.userService.deleteUserByToken(authorizationToken);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_CLIENTE', 'ROLE_VENDEDOR')")
    @PutMapping(path = UrlConstants.USER_URL)
    public ResponseEntity updateUserByToken(@RequestHeader(JwtAuthenticationFilter.HEADER_STRING) String authorizationToken, @RequestBody @Valid UserRequestDTO userDTO) {
        return this.userService.updateUserByToken(authorizationToken, userDTO);
    }
}
