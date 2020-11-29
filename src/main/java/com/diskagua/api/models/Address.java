package com.diskagua.api.models;

import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

/**
 * Representa a entidade 'enderecos' no banco de dados, e armazena todos os
 * endereços dos usuários do sistema.
 *
 * Ao criar um repositório utilizando essa classe, implementa as seguintes named
 * queries:
 *
 * - listAllByCustomerId : procura todos os endereços que estão associados a um
 * determinado cliente;
 *
 * - findByIdAndCustomerId : procura um endereço específico associado a um
 * determinado cliente.
 *
 * @author Carlos
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "enderecos")
@NamedQueries(value = {
    @NamedQuery(name = "Address.listAllByCustomerId", query = "SELECT a FROM Address a, Customer c WHERE c.id = :customerId"),
    @NamedQuery(name = "Address.findByIdAndCustomerId", query = "SELECT a FROM Address a, Customer c WHERE c.id = :customerId AND a.id = :addressId")
})
public class Address implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "cep")
    private String postalCode;

    @Column(nullable = false, name = "estado")
    private String state;

    @Column(nullable = false, name = "cidade")
    private String city;

    @Column(nullable = false, name = "bairro")
    private String district;

    @Column(nullable = false, name = "rua")
    private String street;

    @Column(nullable = false, name = "numero")
    private String number;

    @Column(name = "complemento")
    private String complement;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable(name = "cliente_enderecos", joinColumns = {
        @JoinColumn(name = "endereco_id")}, inverseJoinColumns = {
        @JoinColumn(name = "cliente_id")})
    private Customer customer;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createDateTime;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updateDateTime;
}
