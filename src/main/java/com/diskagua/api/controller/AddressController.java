package com.diskagua.api.controller;

import com.diskagua.api.dto.AddressDTO;
import com.diskagua.api.service.AddressService;
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
 * regras de negócios. Possui todas as chamadas HTTP relacionadas aos endereços
 * do cliente.
 *
 * @author Carlos
 */
@RestController
@RequestMapping(path = "/api/v1/clientes/{customerId}")
public class AddressController {

    private final AddressService addressService;

    /**
     * Constrói um controlador, utilizando a injeção de dependências do Spring.
     *
     * @param addressService serviço que possui as regras de negócios dos
     * endereços do cliente
     */
    @Autowired
    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    /**
     * Salva um endereço para determinado cliente no repositório, através de uma
     * requisição HTTP POST, com um JSON, informando os campos do endereço e o
     * identificador (id) do cliente.
     *
     * @param customerId identificador (id) do cliente
     * @param addressDTO o endereço no padrão DTO
     * @return o status da solicitação e um JSON com o endereço criado
     */
    @PostMapping(path = "/enderecos")
    public ResponseEntity createAddressForCustomer(@PathVariable Long customerId, @RequestBody AddressDTO addressDTO) {
        return this.addressService.createAddressForCustomer(customerId, addressDTO);
    }

    /**
     * Busca todos os endereços de um determinado cliente salvos no repositório,
     * através de uma requisição HTTP GET.
     *
     * @param customerId identificador (id) do cliente
     * @return o status da solicitação e um JSON com todos os endereços
     * encontrados
     */
    @GetMapping(path = "/enderecos")
    public ResponseEntity listAllAddressesForCustomer(@PathVariable Long customerId) {
        return this.addressService.listAllAddressesForCustomer(customerId);
    }

    /**
     * Busca um endereço específico de um determinado cliente no repositório,
     * através de uma requisição HTTP GET, informando o identificador (id) do
     * cliente e do endereço na URI.
     *
     * @param customerId identificador (id) do cliente
     * @param addressId identificador (id) do endereço
     * @return o status da solicitação e um JSON com o endereço encontrado
     */
    @GetMapping(path = "/enderecos/{addressId}")
    public ResponseEntity findCustomerById(@PathVariable Long customerId, @PathVariable Long addressId) {
        return this.addressService.findAddressForCustomerById(customerId, addressId);
    }

    /**
     * Deleta um endereço específico de um determinado cliente do repositório,
     * através de uma requisição HTTP DELETE, informando o identificador (id) do
     * cliente e do endereço na URI.
     *
     * @param customerId identificador (id) do cliente
     * @param addressId identificador (id) do endereço
     * @return o status da solicitação
     */
    @DeleteMapping(path = "/enderecos/{addressId}")
    public ResponseEntity deleteAddressForCustomer(@PathVariable Long customerId, @PathVariable Long addressId) {
        return this.addressService.deleteAddressForCustomer(customerId, addressId);
    }

    /**
     * Atualiza um endereço específico de um determinado cliente do repositório,
     * através de uma requisição HTTP PUT, informando o identificador (id) do
     * cliente e do endereço na URI, e um JSON com as novas informações do
     * endereço.
     *
     * @param customerId identificador (id) do cliente
     * @param addressId identificador (id) do endereço
     * @param addressDTO o endereço no padrão DTO
     * @return o status da solicitação e um JSON com o cliente atualizado
     */
    @PutMapping(path = "/enderecos/{addressId}")
    public ResponseEntity updateAddressForCustomer(@PathVariable Long customerId, @PathVariable Long addressId, @RequestBody AddressDTO addressDTO) {
        return this.addressService.updateAddressForCustomer(customerId, addressId, addressDTO);
    }
}
