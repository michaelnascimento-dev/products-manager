package br.com.michael.productsmanager.util;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Utilitário responsável por criar e fornecer uma instância única de EntityManagerFactory,
 * com base nas configurações externas contidas no arquivo {@code database.properties}.
 *
 * Essa classe aplica o padrão Singleton para garantir que apenas uma fábrica seja criada
 * durante o ciclo de vida da aplicação.
 */
public class JPAUtil {

    /** Instância única da fábrica de EntityManager, carregada na inicialização da classe. */
    private static final EntityManagerFactory emf = buildEntityManagerFactory();

    /**
     * Cria e configura a {@code EntityManagerFactory} utilizando os dados do arquivo {@code database.properties}.
     *
     * @return Instância configurada de {@code EntityManagerFactory}
     * @throws RuntimeException caso haja erro ao ler o arquivo de propriedades
     */
    private static EntityManagerFactory buildEntityManagerFactory() {
        try (InputStream input = JPAUtil.class.getClassLoader().getResourceAsStream("database.properties")) {
            Properties props = new Properties();
            props.load(input);

            Map<String, String> overrides = new HashMap<>();
            overrides.put("jakarta.persistence.jdbc.url", props.getProperty("db.url"));
            overrides.put("jakarta.persistence.jdbc.user", props.getProperty("db.user"));
            overrides.put("jakarta.persistence.jdbc.password", props.getProperty("db.password"));

            return Persistence.createEntityManagerFactory("productsMG", overrides);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao carregar configurações do banco de dados", e);
        }
    }

    /**
     * Retorna a instância única da {@code EntityManagerFactory}.
     * Essa fábrica deve ser compartilhada por todos os DAOs da aplicação.
     *
     * @return {@code EntityManagerFactory} instanciada no carregamento da classe
     */
    public static EntityManagerFactory getEntityManagerFactory() {
        return emf;
    }
}