package com.diskagua.api.models;

import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Representa a entidade 'cliente' do banco de dados, que extende da classe
 * abstrata {@link User}, criando uma tabela que possui os atributos dessa
 * classe e de sua classe mãe.
 *
 * Ao criar um repositório utilizando essa classe, implementa as seguintes named
 * queries:
 *
 * - existsByEmail : procura o cliente que possue um determinado email.
 *
 * @author Carlos
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "clientes")
@NamedQueries(value = {
    @NamedQuery(name = "Boolean.existsByEmail", query = "SELECT coutn(c) > 0 FROM Customer c WHERE c.email = :customerEmail")
})
public class Customer extends User implements Serializable {

    @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    @JoinTable(name = "cliente_enderecos", joinColumns = {
        @JoinColumn(name = "cliente_id")}, inverseJoinColumns = {
        @JoinColumn(name = "endereco_id")})
    private List<Address> addresses;
}
