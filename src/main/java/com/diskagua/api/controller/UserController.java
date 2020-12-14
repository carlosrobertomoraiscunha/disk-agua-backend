package com.diskagua.api.controller;

import com.diskagua.api.dto.request.LoginUserRequestDTO;
import com.diskagua.api.dto.request.UserRequestDTO;
import com.diskagua.api.models.Role;
import com.diskagua.api.service.UserService;
import com.diskagua.api.util.UrlConstants;
import javax.validation.Valid;
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
@RequestMapping(path = UrlConstants.USER_URL)
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(path = "/{userRole}")
    public ResponseEntity register(@PathVariable Role userRole, @RequestBody @Valid UserRequestDTO userDTO) {
        return this.userService.register(userRole, userDTO);
    }

    @PostMapping(path = "/auth")
    public ResponseEntity login(@RequestBody LoginUserRequestDTO loginDTO) {
        return this.userService.login(loginDTO);
    }

    @GetMapping
    public ResponseEntity listAllUsers() {
        return this.userService.listAllUsers();
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity findUserById(@PathVariable Long id) {
        return this.userService.findUserById(id);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity deleteUserById(@PathVariable Long id) {
        return this.userService.deleteUserById(id);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity updateUserById(@PathVariable Long id, @RequestBody @Valid UserRequestDTO userDTO) {
        return this.userService.updateUserById(id, userDTO);
    }
}
