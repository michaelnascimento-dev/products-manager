package br.com.michael.productsmanager.model;

import jakarta.persistence.*;

/**
 * Entidade que representa um produto no sistema.
 * <p>
 * Cada produto contém informações básicas e está associado a um usuário responsável.
 */
@Entity
public class Product {

    /**
     * Identificador único do produto.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nome do produto.
     */
    private String name;

    /**
     * Preço do produto (deve ser positivo).
     */
    private Double price;

    /**
     * Descrição opcional do produto.
     */
    private String description;

    /**
     * Usuário responsável pelo cadastro deste produto.
     */
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    /**
     * Construtor padrão exigido pelo JPA.
     */
    public Product() {}

    /**
     * Construtor com atributos principais (sem o usuário).
     *
     * @param name Nome do produto.
     * @param price Preço do produto.
     * @param description Descrição do produto.
     */
    public Product(String name, Double price, String description) {
        this.name = name;
        this.price = price;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Retorna uma representação simplificada do produto.
     *
     * @return Informações principais do produto.
     */
    @Override
    public String toString() {
        return "Product{id=" + id + ", name='" + name + "', price=" + price + "}";
    }
}