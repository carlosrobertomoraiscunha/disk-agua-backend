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

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "enderecos")
@NamedQueries(value = {
    @NamedQuery(name = "Address.listAllUserAddressesById", query = "SELECT a FROM Address a WHERE a.user.id = :userId"),
    @NamedQuery(name = "Address.findUserAddressById", query = "SELECT a FROM Address a WHERE a.user.id = :userId AND a.id = :addressId")
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
    @JoinColumn(name = "usuario_id")
    private User user;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createDateTime;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updateDateTime;
}
