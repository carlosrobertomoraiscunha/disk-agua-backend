package com.diskagua.api.repository;

import com.diskagua.api.models.OrderEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {

    List<OrderEntity> listOrdersByCustomerId(Long customerId);
}
