package com.diskagua.api.repository;

import com.diskagua.api.models.Product;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> listAllUserProductsById(Long userId);

    Optional<Product> findUserProductById(Long userId, Long productId);
}
