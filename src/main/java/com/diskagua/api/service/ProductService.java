package com.diskagua.api.service;

import com.diskagua.api.dto.request.ProductRequestDTO;
import com.diskagua.api.dto.response.ProductResponseDTO;
import com.diskagua.api.mapper.ProductMapper;
import com.diskagua.api.models.Product;
import com.diskagua.api.models.User;
import com.diskagua.api.repository.ProductRepository;
import com.diskagua.api.repository.UserRepository;
import java.net.URI;
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

    public ResponseEntity createUserProduct(Long userId, ProductRequestDTO productDTO) {
        Optional<User> foundUser = this.userRepository.findById(userId);

        if (foundUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Product productToSave = this.productMapper.toModel(productDTO);
        Product savedProduct = foundUser.map((user) -> {
            productToSave.setUser(user);

            return this.productRepository.save(productToSave);
        }).orElseThrow();

        ProductResponseDTO savedProductDTO = this.productMapper.toResponseDTO(savedProduct);
        URI location = URI.create("/api/v1/usuarios/"
                + savedProduct.getUser().getId()
                + "/enderecos/"
                + savedProduct.getId());

        return ResponseEntity.created(location).body(savedProductDTO);
    }

    public ResponseEntity listAllUserProducts(Long userId) {
        if (!verifyIfUserExistsById(userId)) {
            return ResponseEntity.notFound().build();
        }

        List<Product> allProducts = this.productRepository.listAllUserProductsById(userId);
        List<ProductResponseDTO> allProductsDTO = allProducts.stream().map((product) -> {
            return this.productMapper.toResponseDTO(product);
        }).collect(Collectors.toList());

        return ResponseEntity.ok(allProductsDTO);
    }

    public ResponseEntity findUserProductById(Long userId, Long productId) {
        Optional<ProductResponseDTO> foundProductDTO = this.productRepository
                .findUserProductById(userId, productId).map((address) -> {
            return this.productMapper.toResponseDTO(address);
        });

        return ResponseEntity.of(foundProductDTO);
    }

    public ResponseEntity deleteUserProductById(Long userId, Long productId) {
        if (!verifyIfUserProductExistsById(userId, productId)) {
            return ResponseEntity.notFound().build();
        }

        this.productRepository.deleteById(productId);

        return ResponseEntity.noContent().build();
    }

    public ResponseEntity updateUserProductById(Long userId, Long productId, ProductRequestDTO productDTO) {
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
    }

    private boolean verifyIfUserExistsById(Long id) {
        return this.userRepository.existsById(id);
    }

    private boolean verifyIfUserProductExistsById(Long userId, Long productId) {
        return this.productRepository.findUserProductById(userId, productId).isPresent();
    }
}
