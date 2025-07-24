package br.com.michael.productsmanager.controller;

import br.com.michael.productsmanager.dao.UserDAO;
import br.com.michael.productsmanager.model.User;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.*;
import javafx.util.Duration;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Controlador responsável pela tela de registro de novos usuários.
 * <p>
 * Valida entradas, aplica criptografia com BCrypt e interage com o {@link UserDAO}
 * para persistência no banco. Também gerencia eventos de interface como mover, minimizar e fechar a janela.
 */
public class RegisterController implements Initializable {

    @FXML private HBox customTitleBar;
    @FXML public Button btnGoToLogin;
    @FXML private Button minimizeBtn;
    @FXML private Button closeBtn;
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private Label messageLabel;

    private double xOffset;
    private double yOffset;

    /**
     * Inicializa os controles de movimentação da janela e os botões de interface.
     *
     * @param url URL do recurso FXML.
     * @param resourceBundle Bundle de internacionalização.
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

        minimizeBtn.setOnMouseClicked(event -> {
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
    }

    /**
     * Executa o processo de registro de um novo usuário.
     * Verifica campos, valida senhas, aplica criptografia e persiste via DAO.
     */
    @FXML
    private void register() {
        String username = usernameField.getText().trim().toLowerCase();
        String password = passwordField.getText().trim();
        String confirmPassword = confirmPasswordField.getText().trim();

        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            messageLabel.setText("Preencha todos os campos.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            messageLabel.setText("As senhas não coincidem.");
            return;
        }

        UserDAO dao = new UserDAO();
        User existingUser = dao.findByUsername(username);
        if (existingUser != null) {
            messageLabel.setText("Nome de usuário já existe.");
            return;
        }

        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        User newUser = new User(username, hashedPassword);
        boolean success = dao.save(newUser);

        if (success) {
            messageLabel.setText("Usuário registrado com sucesso!");
            usernameField.clear();
            passwordField.clear();
            confirmPasswordField.clear();

            PauseTransition pause = new PauseTransition(Duration.seconds(1.5));
            pause.setOnFinished(e -> openLoginView());
            pause.play();
        } else {
            messageLabel.setText("Erro ao registrar usuário.");
        }
    }

    /**
     * Abre a tela de login e fecha a janela de cadastro.
     */
    private void openLoginView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/LoginView.fxml"));
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

    /**
     * Executa transição direta da tela de cadastro para a tela de login.
     */
    @FXML
    private void goToLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/LoginView.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.initStyle(StageStyle.UNDECORATED);
            stage.show();

            Stage currentStage = (Stage) btnGoToLogin.getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            messageLabel.setText("Erro ao abrir tela de login.");
            e.printStackTrace();
        }
    }
}