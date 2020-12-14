package com.diskagua.api.models;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.CascadeType;
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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "produtos")
@NamedQueries(value = {
    @NamedQuery(name = "Product.listAllUserProductsById", query = "SELECT p FROM Product p WHERE p.user.id = :userId"),
    @NamedQuery(name = "Product.findUserProductById", query = "SELECT p FROM Product p WHERE p.user.id = :userId AND p.id = :productId")
})
public class Product implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, orphanRemoval = true, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    @JoinColumn(name = "imagem_id")
    private Image image;

    @Column(nullable = false, name = "nome")
    private String name;

    @Column(nullable = false, name = "preco")
    private BigDecimal price;

    @Column(nullable = false, name = "descricao")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private User user;
}
