package com.diskagua.api.service;

import com.diskagua.api.dto.request.CheckOutRequestDTO;
import com.diskagua.api.dto.request.LoginUserRequestDTO;
import com.diskagua.api.repository.UserRepository;
import com.diskagua.api.dto.request.UserRequestDTO;
import com.diskagua.api.dto.response.LoginUserResponseDTO;
import com.diskagua.api.dto.response.OrderResponseDTO;
import com.diskagua.api.dto.response.UserResponseDTO;
import com.diskagua.api.mapper.OrderMapper;
import com.diskagua.api.models.Role;
import com.diskagua.api.mapper.UserMapper;
import com.diskagua.api.models.OrderEntity;
import com.diskagua.api.models.OrderStatus;
import com.diskagua.api.models.Product;
import com.diskagua.api.models.User;
import com.diskagua.api.repository.OrderRepository;
import com.diskagua.api.repository.ProductRepository;
import com.diskagua.api.repository.RoleRepository;
import com.diskagua.api.util.TokenUtils;
import java.math.BigDecimal;
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
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserMapper userMapper = UserMapper.INSTANCE;
    private final OrderMapper orderMapper = OrderMapper.INSTANCE;
    private final AuthenticationManager authenticationManager;
    private final TokenUtils jwtTokenUtil;

    @Autowired
    public UserService(UserRepository userRepository,
            RoleRepository roleRepository,
            OrderRepository orderRepository,
            ProductRepository productRepository,
            BCryptPasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            TokenUtils jwtTokenUtil) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    public ResponseEntity register(String userRole, UserRequestDTO userDTO) {
        if (verifyIfUserExistsByEmail(userDTO.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("O email informado já existe");
        }

        User userToSave = this.userMapper.toModel(userDTO);
        Optional<Role> role = this.roleRepository.findRoleByName(userRole);

        if (role.isEmpty()) {
            Role savedRole = this.roleRepository.save(new Role(null, userRole));

            userToSave.setRole(savedRole);
        } else {
            userToSave.setRole(role.get());
        }

        userToSave.setPassword(this.passwordEncoder.encode(userToSave.getPassword()));

        User userSaved = this.userRepository.save(userToSave);
        UserResponseDTO userSavedDTO = this.userMapper.toResponseDTO(userSaved);

        return ResponseEntity.ok(userSavedDTO);
    }

    public ResponseEntity login(LoginUserRequestDTO loginDTO) {
        try {
            Authentication authentication = this.authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginDTO.getEmail(),
                            loginDTO.getPassword())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String token = jwtTokenUtil.generateToken(authentication);
            Optional<UserResponseDTO> foundUserDTO = this.userRepository
                    .findUserByEmail(loginDTO.getEmail()).map((user) -> {
                return this.userMapper.toResponseDTO(user);
            });

            return ResponseEntity.ok(new LoginUserResponseDTO(token, foundUserDTO.get()));
        } catch (AuthenticationException ex) {
            return new ResponseEntity(ex.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = this.userRepository.findUserByEmail(username).orElseThrow(() -> {
            throw new UsernameNotFoundException("Usuário não encontrado");
        });

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                getAuthorities(user.getRole()));
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

    public ResponseEntity findUserByToken(String authorizationToken) {
        try {
            String email = TokenUtils.getEmailFromToken(authorizationToken);

            Optional<UserResponseDTO> foundUserDTO = this.userRepository
                    .findUserByEmail(email).map((user) -> {
                return this.userMapper.toResponseDTO(user);
            });

            return ResponseEntity.of(foundUserDTO);
        } catch (Exception ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @Transactional
    public ResponseEntity deleteUserByToken(String authorizationToken) {
        try {
            String email = TokenUtils.getEmailFromToken(authorizationToken);

            if (!verifyIfUserExistsByEmail(email)) {
                return ResponseEntity.notFound().build();
            }

            this.userRepository.deleteUserByEmail(email);

            return ResponseEntity.noContent().build();
        } catch (Exception ex) {
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity updateUserByToken(String authorizationToken, UserRequestDTO userDTO) {
        try {
            String email = TokenUtils.getEmailFromToken(authorizationToken);
            User userToUpdate = this.userMapper.toModel(userDTO);

            if (verifyIfUserExistsByEmail(userToUpdate.getEmail()) && !email.equals(userToUpdate.getEmail())) {
                return ResponseEntity.badRequest().body("Esse email já está sendo utilizado");
            }

            Optional<UserResponseDTO> updatedUserDTO = this.userRepository
                    .findUserByEmail(email).map((foundUser) -> {
                foundUser.setEmail(userToUpdate.getEmail());
                foundUser.setPassword(passwordEncoder.encode(userToUpdate.getPassword()));
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
        } catch (Exception ex) {
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity addProductToOrder(String authorizationToken, Long id) {
        String email = TokenUtils.getEmailFromToken(authorizationToken);

        if (!verifyIfUserExistsByEmail(email)) {
            return ResponseEntity.notFound().build();
        }

        User user = this.userRepository.findUserByEmail(email).get();
        Optional<Product> product = this.productRepository.findById(id);

        if (product.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        List<OrderEntity> orderList = this.orderRepository.listOrdersByCustomerId(user.getId());
        OrderEntity order;

        if (orderList.isEmpty()) {
            order = this.orderRepository.save(OrderEntity
                    .builder()
                    .customer(user)
                    .build());
        } else {
            order = orderList.stream().filter((orderItem) -> {
                return orderItem.getStatus() == OrderStatus.ABERTO;
            }).findFirst().orElse(null);
        }

        if (order == null) {
            order = this.orderRepository.save(OrderEntity
                    .builder()
                    .customer(user)
                    .build());
        }

        order.getProducts().add(product.get());
        BigDecimal totalPrice = order.getTotalPrice().add(product.get().getPrice());
        order.setTotalPrice(totalPrice);
        this.orderRepository.save(order);

        OrderResponseDTO orderDTO = this.orderMapper.toResponseDTO(order);

        return ResponseEntity.ok(orderDTO);
    }

    public ResponseEntity checkOut(String authorizationToken, CheckOutRequestDTO checkOutRequestDTO) {
        String email = TokenUtils.getEmailFromToken(authorizationToken);

        if (!verifyIfUserExistsByEmail(email)) {
            return ResponseEntity.notFound().build();
        }

        User user = this.userRepository.findUserByEmail(email).get();
        List<OrderEntity> orderList = this.orderRepository.listOrdersByCustomerId(user.getId());

        OrderEntity order;

        if (orderList.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            order = orderList.stream().filter((orderItem) -> {
                return orderItem.getStatus() == OrderStatus.ABERTO;
            }).findFirst().orElse(null);
        }

        if (order == null) {
            return ResponseEntity.notFound().build();
        }

        order.setStatus(OrderStatus.FEITO);

        this.orderRepository.save(order);

        OrderResponseDTO orderDTO = this.orderMapper.toResponseDTO(order);

        return ResponseEntity.ok(orderDTO);
    }

    private boolean verifyIfUserExistsByEmail(String email) {
        return this.userRepository.existsUserByEmail(email);
    }

    private Collection<? extends GrantedAuthority> getAuthorities(Role role) {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();

        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));

        return grantedAuthorities;
    }
}
