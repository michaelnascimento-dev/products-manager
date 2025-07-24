package br.com.michael.productsmanager.dao;

import br.com.michael.productsmanager.model.User;
import br.com.michael.productsmanager.util.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import java.util.List;

/**
 * DAO responsável pelas operações de persistência da entidade {@link User}.
 * Utiliza JPA e {@code EntityManager} para manipular os registros no banco de dados.
 */
public class UserDAO {

    /** Fábrica de EntityManager compartilhada por toda a aplicação. */
    private static final EntityManagerFactory emf = JPAUtil.getEntityManagerFactory();

    /**
     * Persiste um novo usuário no banco de dados.
     *
     * @param user Objeto {@code User} a ser salvo
     * @return {@code true} se salvo com sucesso, {@code false} se houver erro
     */
    public boolean save(User user) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(user);
            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
            em.getTransaction().rollback();
            return false;
        } finally {
            em.close();
        }
    }

    /**
     * Busca um usuário pelo nome de login (username).
     *
     * @param username Nome de usuário a ser buscado
     * @return {@code User} correspondente ou {@code null} se não encontrado
     */
    public User findByUsername(String username) {
        EntityManager em = emf.createEntityManager();
        try {
            List<User> results = em.createQuery(
                            "SELECT u FROM User u WHERE u.username = :username",
                            User.class
                    )
                    .setParameter("username", username)
                    .getResultList();

            return results.isEmpty() ? null : results.get(0);
        } finally {
            em.close();
        }
    }
}