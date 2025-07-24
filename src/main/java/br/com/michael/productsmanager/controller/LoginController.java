package br.com.michael.productsmanager.controller;

import br.com.michael.productsmanager.dao.UserDAO;
import br.com.michael.productsmanager.model.User;
import br.com.michael.productsmanager.session.Session;
import javafx.application.Platform;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.*;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Controlador responsável pela tela de login da aplicação ProductsManager.
 * <p>
 * Valida credenciais, autentica usuários com senha criptografada via BCrypt,
 * inicializa a sessão e gerencia transições entre janelas.
 */
public class LoginController implements Initializable {

    @FXML private HBox customTitleBar;
    @FXML private Button minimizeBtn;
    @FXML private Button closeBtn;
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;
    @FXML private Button loginButton;

    private double xOffset;
    private double yOffset;

    /**
     * Inicializa comportamentos da janela (mover, minimizar e fechar) e botão de login.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        customTitleBar.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        customTitleBar.setOnMouseDragged(event -> {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });

        minimizeBtn.setOnAction(event -> {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setIconified(true);
        });

        closeBtn.setOnMouseClicked(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmação de Saída");
            alert.setHeaderText(null);
            alert.setContentText("Tem certeza que deseja sair do programa?");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                Platform.exit();
            }
        });

        loginButton.setOnMouseClicked(event -> login());
    }

    /**
     * Processa a tentativa de login com validação dos campos e autenticação do usuário.
     * Em caso de sucesso, carrega a interface principal e define a sessão.
     */
    @FXML
    private void login() {
        String username = usernameField.getText().trim().toLowerCase();
        String password = passwordField.getText().trim();

        if (username.isBlank() || password.isBlank()) {
            errorLabel.setText("Preencha todos os campos.");
            return;
        }

        UserDAO userDAO = new UserDAO();
        User user = userDAO.findByUsername(username);

        if (user != null && BCrypt.checkpw(password, user.getPassword())) {
            Session.loggedUser = user;

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainView.fxml"));
                Parent root = loader.load();

                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.initStyle(StageStyle.UNDECORATED);
                stage.show();

                Stage currentStage = (Stage) usernameField.getScene().getWindow();
                currentStage.close();

            } catch (IOException e) {
                errorLabel.setText("Erro ao carregar a tela principal.");
                e.printStackTrace();
            }

        } else {
            errorLabel.setText("Usuário ou senha inválidos.");
        }
    }

    /**
     * Abre a tela de registro de novo usuário.
     * Fecha a janela atual de login.
     */
    @FXML
    private void openRegisterView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/RegisterView.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.initStyle(StageStyle.UNDECORATED);
            stage.show();

            Stage atual = (Stage) usernameField.getScene().getWindow();
            atual.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}