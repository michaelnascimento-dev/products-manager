package br.com.michael.productsmanager.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

/**
 * Entidade que representa um usuário do sistema ProductsManager.
 * <p>
 * Usado para autenticação via senha (criptografada com BCrypt)
 * e para associação de produtos registrados.
 */
@Entity
public class User {

    /**
     * Identificador único do usuário, gerado automaticamente.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nome de login do usuário.
     */
    private String username;

    /**
     * Senha do usuário.
     * <p>
     * Armazenada de forma criptografada com BCrypt — nunca exposta diretamente.
     */
    private String password;

    /**
     * Construtor padrão exigido pelo JPA.
     */
    public User() {}

    /**
     * Construtor para criação direta de usuários com credenciais.
     * Recomenda-se aplicar criptografia externa com BCrypt antes de persistir.
     *
     * @param username Nome de login do usuário.
     * @param password Senha já criptografada.
     */
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * @return Identificador único do usuário.
     */
    public Long getId() {
        return id;
    }

    /**
     * Define o ID do usuário (geralmente feito pelo JPA).
     *
     * @param id Identificador único.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return Nome de login do usuário.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Define o nome de login do usuário.
     *
     * @param username Nome escolhido.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Retorna a senha criptografada.
     * Somente para fins de autenticação — nunca deve ser exibida diretamente.
     *
     * @return Senha criptografada do usuário.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Define a senha criptografada do usuário.
     *
     * @param password Hash da senha gerado externamente (ex: BCrypt).
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Retorna uma representação textual do usuário, sem expor a senha.
     *
     * @return Dados identificadores do usuário.
     */
    @Override
    public String toString() {
        return "User{id=" + id + ", username='" + username + "'}";
    }
}