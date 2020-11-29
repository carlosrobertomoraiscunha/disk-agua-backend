package com.diskagua.api.repository;

import com.diskagua.api.models.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositório padrão dos clientes, que extende de {@link JpaRepository}, e
 * implementa as named queries do cliente como métodos.
 *
 * @author Carlos
 */
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    /**
     * Verifica se existe um cliente com um determinado email.
     *
     * @param customerEmail email do cliente
     * @return true se o cliente já existe no repositório, false caso não
     */
    Boolean existsByEmail(String customerEmail);
}
