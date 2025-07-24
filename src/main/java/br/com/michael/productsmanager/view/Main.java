package br.com.michael.productsmanager.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.io.IOException;

/**
 * Classe principal da aplicação ProductsManager.
 * Responsável por iniciar a interface gráfica e carregar a tela de login.
 * <p>
 * Desenvolvido por Michael — Projeto com foco em boas práticas e documentação.
 */
public class Main extends Application {

    /**
     * Inicializa a interface gráfica do sistema, exibindo a tela de login.
     *
     * @param stage Janela principal fornecida pela API JavaFX.
     */
    @Override
    public void start(Stage stage) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/LoginView.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.show();

        } catch (IOException e) {
            System.err.println("Falha ao carregar a interface de login.");
            e.printStackTrace();
        }
    }

    /**
     * Ponto de entrada da aplicação.
     *
     * @param args Argumentos da linha de comando (opcional)
     */
    public static void main(String[] args) {
        launch(args);
    }
}