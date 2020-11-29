package com.diskagua.api.service;

import com.diskagua.api.dto.CustomerDTO;
import com.diskagua.api.mapper.CustomerMapper;
import com.diskagua.api.models.Customer;
import com.diskagua.api.repository.CustomerRepository;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper = CustomerMapper.INSTANCE;

    /**
     * Constrói um serviço, utilizando a injeção de dependências do Spring.
     *
     * @param customerRepository repositório que armazena os dados do cliente
     */
    @Autowired
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;

    }

    /**
     * Converte um cliente no padrão DTO e salva-o no repositório.
     *
     * @param customerDTO um cliente no padrão DTO
     * @return um objeto {@link ResponseEntity} com o status da resposta e o
     * cliente salvo em formato JSON
     */
    public ResponseEntity createCustomer(CustomerDTO customerDTO) {
        if (verifyIfCustomerExistsByEmail(customerDTO.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("O email informado já existe");
        }

        Customer customerToSave = this.customerMapper.toModel(customerDTO);
        Customer customerSaved = this.customerRepository.save(customerToSave);
        CustomerDTO customerSavedDTO = this.customerMapper.toDTO(customerSaved);
        URI location = URI.create("/api/v1/clientes" + customerSaved.getId());

        return ResponseEntity.created(location).body(customerSavedDTO);
    }

    /**
     * Busca todos os cliente salvos no repositório.
     *
     * @return um objeto {@link ResponseEntity} com o status da resposta e a
     * lista de clientes encontrados em formato JSON
     */
    public ResponseEntity listAllCustomers() {
        List<Customer> allCustomers = this.customerRepository.findAll();
        List<CustomerDTO> allCustomersDTO = allCustomers.stream().map((customer) -> {
            return this.customerMapper.toDTO(customer);
        }).collect(Collectors.toList());

        return ResponseEntity.ok(allCustomersDTO);
    }

    /**
     * Busca um cliente específico no repositório.
     *
     * @param id identificador (id) do cliente
     * @return um objeto {@link ResponseEntity} com o status da resposta e o
     * cliente encontrado em formato JSON
     */
    public ResponseEntity findCustomerById(Long id) {
        Optional<CustomerDTO> foundCustomerDTO = this.customerRepository
                .findById(id).map((customer) -> {
            return this.customerMapper.toDTO(customer);
        });

        return ResponseEntity.of(foundCustomerDTO);
    }

    /**
     * Deleta um cliente do repositório.
     *
     * @param id identificador (id) do cliente
     * @return um objeto {@link ResponseEntity} com o status da resposta
     */
    public ResponseEntity deleteCustomerById(Long id) {
        if (!verifyIfCustomerExistsById(id)) {
            return ResponseEntity.notFound().build();
        }

        this.customerRepository.deleteById(id);

        return ResponseEntity.noContent().build();
    }

    /**
     * Atualiza um cliente no repositório.
     *
     * @param id identificador (id) do cliente
     * @param customerDTO o cliente atualizado no padrão DTO
     * @return um objeto {@link ResponseEntity} com o status da resposta e o
     * cliente atualizado em formato JSON
     */
    public ResponseEntity updateCustomerById(Long id, CustomerDTO customerDTO) {
        Customer customerToUpdate = this.customerMapper.toModel(customerDTO);

        Optional<CustomerDTO> updatedCustomerDTO = this.customerRepository
                .findById(id).map((foundCustomer) -> {
            foundCustomer.setEmail(customerToUpdate.getEmail());
            foundCustomer.setPassword(customerToUpdate.getPassword());
            foundCustomer.getImage().setName(customerToUpdate.getImage().getName());
            foundCustomer.getImage().setContent(customerToUpdate.getImage().getContent());
            foundCustomer.getImage().setType(customerToUpdate.getImage().getType());
            foundCustomer.setName(customerToUpdate.getName());
            foundCustomer.setPhoneNumber(customerToUpdate.getPhoneNumber());

            Customer savedCustomer = this.customerRepository.save(foundCustomer);
            CustomerDTO savedCustomerDTO = this.customerMapper.toDTO(savedCustomer);

            return savedCustomerDTO;
        });

        return ResponseEntity.of(updatedCustomerDTO);
    }

    private boolean verifyIfCustomerExistsById(Long id) {
        return this.customerRepository.existsById(id);
    }

    private boolean verifyIfCustomerExistsByEmail(String email) {
        return this.customerRepository.existsByEmail(email);
    }
}
