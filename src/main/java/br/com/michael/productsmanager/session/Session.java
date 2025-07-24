package br.com.michael.productsmanager.session;

import br.com.michael.productsmanager.model.User;

/**
 * Gerencia a sessão atual da aplicação.
 * Armazena o usuário autenticado globalmente durante a execução.
 */
public class Session {

    /**
     * Usuário atualmente autenticado na aplicação.
     */
    public static User loggedUser;
}