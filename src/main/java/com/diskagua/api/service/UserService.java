package com.diskagua.api.service;

import com.diskagua.api.dto.request.LoginUserRequestDTO;
import com.diskagua.api.repository.UserRepository;
import com.diskagua.api.dto.request.UserRequestDTO;
import com.diskagua.api.dto.response.LoginUserResponseDTO;
import com.diskagua.api.dto.response.UserResponseDTO;
import com.diskagua.api.models.Role;
import com.diskagua.api.mapper.UserMapper;
import com.diskagua.api.models.User;
import com.diskagua.api.util.TokenUtils;
import com.diskagua.api.util.UrlConstants;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserMapper userMapper = UserMapper.INSTANCE;
    private final AuthenticationManager authenticationManager;
    private final TokenUtils jwtTokenUtil;

    @Autowired
    public UserService(UserRepository userRepository,
            BCryptPasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            TokenUtils jwtTokenUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    public ResponseEntity register(Role userRole, UserRequestDTO userDTO) {
        if (verifyIfUserExistsByEmail(userDTO.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("O email informado já existe");
        }

        User userToSave = this.userMapper.toModel(userDTO);

        userToSave.setUserRole(userRole);
        userToSave.setPassword(passwordEncoder.encode(userToSave.getPassword()));

        User userSaved = this.userRepository.save(userToSave);
        UserResponseDTO userSavedDTO = this.userMapper.toResponseDTO(userSaved);
        URI location = URI.create(UrlConstants.USER_URL + "/" + userSaved.getId());

        return ResponseEntity.created(location).body(userSavedDTO);
    }

    public ResponseEntity login(LoginUserRequestDTO loginDTO) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginDTO.getEmail(),
                            loginDTO.getPassword())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String token = jwtTokenUtil.generateToken(authentication);

            return ResponseEntity.ok(new LoginUserResponseDTO(token));
        } catch (AuthenticationException ex) {
            return new ResponseEntity(ex.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = this.userRepository.findByEmail(username).orElseThrow(() -> {
            throw new UsernameNotFoundException("Email ou senha inválidos");
        });

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                getAuthority(user.getUserRole()));
    }

    public ResponseEntity listAllUsers() {
        List<User> allUsers = this.userRepository.findAll();
        List<UserResponseDTO> allUsersDTO = allUsers.stream().map((user) -> {
            return this.userMapper.toResponseDTO(user);
        }).collect(Collectors.toList());

        return ResponseEntity.ok(allUsersDTO);
    }

    public ResponseEntity findUserById(Long id) {
        Optional<UserResponseDTO> foundUserDTO = this.userRepository
                .findById(id).map((user) -> {
            return this.userMapper.toResponseDTO(user);
        });

        return ResponseEntity.of(foundUserDTO);
    }

    public ResponseEntity deleteUserById(Long id) {
        if (!verifyIfUserExistsById(id)) {
            return ResponseEntity.notFound().build();
        }

        this.userRepository.deleteById(id);

        return ResponseEntity.noContent().build();
    }

    public ResponseEntity updateUserById(Long id, UserRequestDTO userDTO) {
        User userToUpdate = this.userMapper.toModel(userDTO);

        Optional<UserResponseDTO> updatedUserDTO = this.userRepository
                .findById(id).map((foundUser) -> {
            foundUser.setEmail(userToUpdate.getEmail());
            foundUser.setPassword(userToUpdate.getPassword());
            foundUser.getImage().setName(userToUpdate.getImage().getName());
            foundUser.getImage().setContent(userToUpdate.getImage().getContent());
            foundUser.getImage().setType(userToUpdate.getImage().getType());
            foundUser.setName(userToUpdate.getName());
            foundUser.setPhoneNumber(userToUpdate.getPhoneNumber());

            User savedUser = this.userRepository.save(foundUser);
            UserResponseDTO savedUserDTO = this.userMapper.toResponseDTO(savedUser);

            return savedUserDTO;
        });

        return ResponseEntity.of(updatedUserDTO);
    }

    private boolean verifyIfUserExistsById(Long id) {
        return this.userRepository.existsById(id);
    }

    private boolean verifyIfUserExistsByEmail(String email) {
        return this.userRepository.existsUserByEmail(email);
    }

    private Collection<? extends GrantedAuthority> getAuthority(Role role) {
        List<GrantedAuthority> grantedAuthoritys = new ArrayList<>();

        grantedAuthoritys.add(new SimpleGrantedAuthority(role.name()));

        return grantedAuthoritys;
    }
}
