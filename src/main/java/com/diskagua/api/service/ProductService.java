package com.diskagua.api.service;

import com.diskagua.api.dto.request.ProductRequestDTO;
import com.diskagua.api.dto.response.ProductResponseDTO;
import com.diskagua.api.mapper.ProductMapper;
import com.diskagua.api.models.Product;
import com.diskagua.api.models.User;
import com.diskagua.api.repository.ProductRepository;
import com.diskagua.api.repository.UserRepository;
import com.diskagua.api.util.TokenUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final ProductMapper productMapper = ProductMapper.INSTANCE;

    @Autowired
    public ProductService(UserRepository userRepository, ProductRepository productRepository) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    public ResponseEntity createProduct(String authorizationToken, ProductRequestDTO productDTO) {
        try {
            String email = TokenUtils.getEmailFromToken(authorizationToken);
            Optional<User> foundUser = this.userRepository.findUserByEmail(email);

            if (foundUser.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            Product productToSave = this.productMapper.toModel(productDTO);
            Product savedProduct = foundUser.map((user) -> {
                productToSave.setUser(user);

                return this.productRepository.save(productToSave);
            }).orElseThrow();

            ProductResponseDTO savedProductDTO = this.productMapper.toResponseDTO(savedProduct);

            return ResponseEntity.ok(savedProductDTO);
        } catch (Exception ex) {
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity listAllProducts() {
        List<Product> products = this.productRepository.findAll();
        List<ProductResponseDTO> productsResponseDTO = new ArrayList<>();

        products.forEach(product -> {
            productsResponseDTO.add(this.productMapper.toResponseDTO(product));
        });

        return ResponseEntity.ok(productsResponseDTO);
    }

    public ResponseEntity listAllVendorProducts(String authorizationToken) {
        try {
            String email = TokenUtils.getEmailFromToken(authorizationToken);

            if (!verifyIfUserExistsByEmail(email)) {
                return ResponseEntity.notFound().build();
            }

            User user = this.userRepository.findUserByEmail(email).get();
            List<Product> allProducts = this.productRepository.listAllUserProductsById(user.getId());
            List<ProductResponseDTO> allProductsDTO = allProducts.stream().map((product) -> {
                return this.productMapper.toResponseDTO(product);
            }).collect(Collectors.toList());

            return ResponseEntity.ok(allProductsDTO);
        } catch (Exception ex) {
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity findVendorProductById(String authorizationToken, Long productId) {
        try {
            String email = TokenUtils.getEmailFromToken(authorizationToken);

            if (!verifyIfUserExistsByEmail(email)) {
                return ResponseEntity.notFound().build();
            }

            User user = this.userRepository.findUserByEmail(email).get();
            Optional<ProductResponseDTO> foundProductDTO = this.productRepository
                    .findUserProductById(user.getId(), productId).map((address) -> {
                return this.productMapper.toResponseDTO(address);
            });

            return ResponseEntity.of(foundProductDTO);
        } catch (Exception ex) {
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity deleteVendorProductById(String authorizationToken, Long productId) {
        try {
            String email = TokenUtils.getEmailFromToken(authorizationToken);

            if (!verifyIfUserExistsByEmail(email)) {
                return ResponseEntity.notFound().build();
            }

            User user = this.userRepository.findUserByEmail(email).get();

            if (!verifyIfUserProductExistsById(user.getId(), productId)) {
                return ResponseEntity.notFound().build();
            }

            this.productRepository.deleteById(productId);

            return ResponseEntity.noContent().build();
        } catch (Exception ex) {
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity updateVendorProductById(String authorizationToken, Long productId, ProductRequestDTO productDTO) {
        try {
            String email = TokenUtils.getEmailFromToken(authorizationToken);

            if (!verifyIfUserExistsByEmail(email)) {
                return ResponseEntity.notFound().build();
            }

            User user = this.userRepository.findUserByEmail(email).get();

            if (!verifyIfUserProductExistsById(user.getId(), productId)) {
                return ResponseEntity.notFound().build();
            }

            Product productToUpdate = this.productMapper.toModel(productDTO);

            Optional<ProductRequestDTO> updatedProductDTO = this.productRepository
                    .findById(productId).map((foundProduct) -> {
                foundProduct.getImage().setName(productToUpdate.getImage().getName());
                foundProduct.getImage().setContent(productToUpdate.getImage().getContent());
                foundProduct.getImage().setType(productToUpdate.getImage().getType());
                foundProduct.setName(productToUpdate.getName());
                foundProduct.setPrice(productToUpdate.getPrice());
                foundProduct.setDescription(productToUpdate.getDescription());

                Product savedProduct = this.productRepository.save(foundProduct);
                ProductRequestDTO savedProductDTO = this.productMapper.toRequestDTO(savedProduct);

                return savedProductDTO;
            });

            return ResponseEntity.of(updatedProductDTO);
        } catch (Exception ex) {
            return ResponseEntity.notFound().build();
        }
    }

    private boolean verifyIfUserExistsByEmail(String email) {
        return this.userRepository.existsUserByEmail(email);
    }

    private boolean verifyIfUserProductExistsById(Long userId, Long productId) {
        return this.productRepository.findUserProductById(userId, productId).isPresent();
    }
}
