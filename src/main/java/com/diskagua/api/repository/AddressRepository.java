package com.diskagua.api.repository;

import com.diskagua.api.models.Address;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositório padrão dos endereços, que extende de {@link JpaRepository}, e
 * implementa as named queries do endereço como métodos.
 *
 * @author Carlos
 */
public interface AddressRepository extends JpaRepository<Address, Long> {

    /**
     * Busca todos os endereços que pertecem a um determinado cliente.
     *
     * @param customerId identificador (id) do cliente
     * @return uma lista de endereços
     */
    List<Address> listAllByCustomerId(Long customerId);

    /**
     * Busca um endereço específico de um determinado cliente.
     *
     * @param addressId identificador (id) do endereço
     * @param customerId identificador (id) do cliente
     * @return um objeto {@link Optional} que pode conter, ou não, um endereço
     */
    Optional<Address> findByIdAndCustomerId(Long addressId, Long customerId);
}
