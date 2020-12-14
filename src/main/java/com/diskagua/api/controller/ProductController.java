package com.diskagua.api.controller;

import com.diskagua.api.dto.request.ProductRequestDTO;
import com.diskagua.api.service.ProductService;
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
@RequestMapping("/api/v1/usuarios/{userId}/produtos")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity createUserProduct(@PathVariable Long userId, @RequestBody ProductRequestDTO productDTO) {
        return this.productService.createUserProduct(userId, productDTO);
    }

    @GetMapping
    public ResponseEntity listAllUserProducts(@PathVariable Long userId) {
        return this.productService.listAllUserProducts(userId);
    }

    @GetMapping(path = "/{productId}")
    public ResponseEntity findUserProductById(@PathVariable Long userId, @PathVariable Long productId) {
        return this.productService.findUserProductById(userId, productId);
    }

    @DeleteMapping(path = "/{productId}")
    public ResponseEntity deleteUserProductById(@PathVariable Long userId, @PathVariable Long productId) {
        return this.productService.deleteUserProductById(userId, productId);
    }

    @PutMapping(path = "/{productId}")
    public ResponseEntity updateUserProductById(@PathVariable Long userId, @PathVariable Long productId, @RequestBody ProductRequestDTO productDTO) {
        return this.productService.updateUserProductById(userId, productId, productDTO);
    }
}
