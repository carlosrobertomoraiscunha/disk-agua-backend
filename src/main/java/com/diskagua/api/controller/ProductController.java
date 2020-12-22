package com.diskagua.api.controller;

import com.diskagua.api.dto.request.ProductRequestDTO;
import com.diskagua.api.security.JwtAuthenticationFilter;
import com.diskagua.api.service.ProductService;
import com.diskagua.api.util.UrlConstants;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(UrlConstants.VENDOR_PRODUCT_URL)
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PreAuthorize("hasAnyRole('ROLE_VENDEDOR', 'ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity createProduct(@RequestHeader(JwtAuthenticationFilter.HEADER_STRING) String authorizationToken, @RequestBody ProductRequestDTO productDTO) {
        return this.productService.createProduct(authorizationToken, productDTO);
    }

    @PreAuthorize("hasAnyRole('ROLE_CLIENTE','ROLE_VENDEDOR' ,'ROLE_ADMIN')")
    @GetMapping(path = "/todos")
    public ResponseEntity listAllProducts() {
        return this.productService.listAllProducts();
    }

    @PreAuthorize("hasAnyRole('ROLE_VENDEDOR', 'ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity listAllVendorProducts(@RequestHeader(JwtAuthenticationFilter.HEADER_STRING) String authorizationToken) {
        return this.productService.listAllVendorProducts(authorizationToken);
    }

    @PreAuthorize("hasAnyRole('ROLE_VENDEDOR', 'ROLE_ADMIN')")
    @GetMapping(path = "/{productId}")
    public ResponseEntity findVendorProductById(@RequestHeader(JwtAuthenticationFilter.HEADER_STRING) String authorizationToken, @PathVariable Long productId) {
        return this.productService.findVendorProductById(authorizationToken, productId);
    }

    @PreAuthorize("hasAnyRole('ROLE_VENDEDOR', 'ROLE_ADMIN')")
    @DeleteMapping(path = "/{productId}")
    public ResponseEntity deleteVendorProductById(@RequestHeader(JwtAuthenticationFilter.HEADER_STRING) String authorizationToken, @PathVariable Long productId) {
        return this.productService.deleteVendorProductById(authorizationToken, productId);
    }

    @PreAuthorize("hasAnyRole('ROLE_VENDEDOR', 'ROLE_ADMIN')")
    @PutMapping(path = "/{productId}")
    public ResponseEntity updateVendorProductById(@RequestHeader(JwtAuthenticationFilter.HEADER_STRING) String authorizationToken, @PathVariable Long productId, @RequestBody ProductRequestDTO productDTO) {
        return this.productService.updateVendorProductById(authorizationToken, productId, productDTO);
    }
}
