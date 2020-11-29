package com.diskagua.api.controller;

import com.diskagua.api.dto.CustomerDTO;
import com.diskagua.api.service.CustomerService;
import javax.validation.Valid;
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

/**
 * Controlador utilizado para intermediar a comunicação entre a API e o as
 * regras de negócios. Possui todas as chamadas HTTP relacionadas aos clientes.
 *
 * @author Carlos
 */
@RestController
@RequestMapping(path = "/api/v1/clientes")
public class CustomerController {

    private final CustomerService customerService;

    /**
     * Constrói um controlador, utilizando a injeção de dependências do Spring.
     *
     * @param customerService serviço que possui as regras de negócios do
     * cliente
     */
    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    /**
     * Salva um cliente no repositório, através de uma requisição HTTP POST, com
     * um JSON, informando os campos do cliente.
     *
     * @param customerDTO o cliente no padrão DTO
     * @return o status da solicitação e um JSON com o cliente criado
     */
    @PostMapping
    public ResponseEntity createCustomer(@RequestBody @Valid CustomerDTO customerDTO) {
        return this.customerService.createCustomer(customerDTO);
    }

    /**
     * Busca todos os clientes salvos no repositório, através de uma requisição
     * HTTP GET.
     *
     * @return o status da solicitação e um JSON com todos os cliente
     * encontrados
     */
    @GetMapping
    public ResponseEntity listAllCustomers() {
        return this.customerService.listAllCustomers();
    }

    /**
     * Busca um cliente específico no repositório, através de uma requisição
     * HTTP GET, informando o identificador (id) do cliente na URI.
     *
     * @param id identificador (id) do cliente
     * @return o status da solicitação e um JSON com o cliente encontrado
     */
    @GetMapping(path = "/{id}")
    public ResponseEntity findCustomerById(@PathVariable Long id) {
        return this.customerService.findCustomerById(id);
    }

    /**
     * Deleta um cliente específico do repositório, através de uma requisição
     * HTTP DELETE, informando o identificador (id) do cliente na URI.
     *
     * @param id identificador (id) do cliente
     * @return o status da solicitação
     */
    @DeleteMapping(path = "/{id}")
    public ResponseEntity deleteCustomerById(@PathVariable Long id) {
        return this.customerService.deleteCustomerById(id);
    }

    /**
     * Atualiza um cliente específico do repositório, através de uma requisição
     * HTTP PUT, informando o identificador (id) do cliente na URI, e um JSON
     * com as novas informações do cliente.
     *
     * @param id identificador (id) do cliente
     * @param customerDTO o cliente no padrão DTO
     * @return o status da solicitação e um JSON com o cliente atualizado
     */
    @PutMapping(path = "/{id}")
    public ResponseEntity updateCustomerById(@PathVariable Long id, @RequestBody @Valid CustomerDTO customerDTO) {
        return this.customerService.updateCustomerById(id, customerDTO);
    }

}
