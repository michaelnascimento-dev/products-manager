package br.com.michael.productsmanager.dao;

import br.com.michael.productsmanager.model.Product;
import br.com.michael.productsmanager.model.User;
import br.com.michael.productsmanager.util.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;

import java.util.List;

/**
 * Classe DAO responsável pelas operações de persistência da entidade {@link Product}.
 * Utiliza JPA para interagir com o banco de dados.
 */
public class ProductDAO {

    /** Instância compartilhada da fábrica de EntityManagers. */
    private static final EntityManagerFactory emf = JPAUtil.getEntityManagerFactory();

    /**
     * Adiciona um novo produto ao banco de dados.
     *
     * @param product Produto a ser persistido
     */
    public void addProduct(Product product) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(product);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
    }

    /**
     * Busca um produto pelo ID.
     *
     * @param id Identificador único do produto
     * @return Produto encontrado ou {@code null} se não existir
     */
    public Product findProduct(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(Product.class, id);
        } finally {
            em.close();
        }
    }

    /**
     * Atualiza os dados de um produto existente.
     *
     * @param id ID do produto a ser modificado
     * @param newName Novo nome do produto (opcional)
     * @param newPrice Novo preço (opcional)
     * @param newDescription Nova descrição (opcional)
     */
    public void updateProduct(Long id, String newName, Double newPrice, String newDescription) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Product product = em.find(Product.class, id);
            if (product != null) {
                if (newName != null) product.setName(newName);
                if (newPrice != null) product.setPrice(newPrice);
                if (newDescription != null) product.setDescription(newDescription);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
    }

    /**
     * Remove um produto do banco com base no ID.
     *
     * @param id Identificador do produto a ser removido
     */
    public void deleteProduct(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Product product = em.find(Product.class, id);
            if (product != null) {
                em.remove(product);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
    }

    /**
     * Lista todos os produtos cadastrados por um determinado usuário.
     *
     * @param user Usuário proprietário dos produtos
     * @return Lista de produtos associados ao usuário
     */
    public List<Product> listByUser(User user) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Product> query = em.createQuery(
                    "SELECT p FROM Product p WHERE p.user = :user",
                    Product.class
            );
            query.setParameter("user", user);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
}