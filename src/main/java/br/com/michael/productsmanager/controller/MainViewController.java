package br.com.michael.productsmanager.controller;

import br.com.michael.productsmanager.dao.ProductDAO;
import br.com.michael.productsmanager.model.Product;
import br.com.michael.productsmanager.session.Session;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.text.NumberFormat;
import java.util.*;

/**
 * Controlador da tela principal.
 * Gerencia produtos associados ao usuário logado: adicionar, excluir, editar e visualizar.
 */
public class MainViewController implements Initializable {

    private final ObservableList<Product> productList = FXCollections.observableArrayList();
    private double xOffset;
    private double yOffset;

    @FXML private TextField prodName;
    @FXML private TextField prodPrice;
    @FXML private TextField prodDescription;

    @FXML private TableView<Product> productsTable;
    @FXML private TableColumn<Product, String> colName;
    @FXML private TableColumn<Product, Double> colPrice;
    @FXML private TableColumn<Product, String> colDescription;

    @FXML private TextField searchField;
    @FXML private Button addButton;
    @FXML private Button deleteButton;
    @FXML private Button updateButton;
    @FXML private Button logoutButton;

    @FXML private HBox customTitleBar;

    /**
     * Inicializa a interface principal, configura tabela e filtros.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        handleWindowMovement();
        setupTableColumns();
        initializeTableContent();
        setupSelectionBehavior();
        applySearchFilter();
        applyTableStyling();
    }

    private void handleWindowMovement() {
        customTitleBar.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        customTitleBar.setOnMouseDragged(event -> {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });
    }

    /**
     * Adiciona um novo produto após validação.
     */
    @FXML
    public void addProductButton() {
        String name = prodName.getText();
        String priceText = prodPrice.getText().trim().replace(",", ".");
        String description = prodDescription.getText().trim();

        if (name.isBlank() || priceText.isBlank() || description.isBlank()) {
            showAlert("Campos obrigatórios", "Preencha todos os campos.");
            return;
        }

        double price;
        try {
            price = Double.parseDouble(priceText);
            if (price <= 0) {
                showAlert("Preço inválido", "O preço deve ser maior que zero.");
                return;
            }
        } catch (NumberFormatException e) {
            showAlert("Erro de formato", "Digite um número válido para o preço.");
            return;
        }

        Product product = new Product(name, price, description);
        product.setUser(Session.loggedUser);
        new ProductDAO().addProduct(product);
        productList.add(product);

        showAlert("Sucesso", "Produto adicionado com sucesso!");

        clearFields();
    }

    /**
     * Atualiza os dados de um produto selecionado.
     */
    @FXML
    public void updateProductButton() {
        Product selected = productsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Nenhum produto selecionado", "Selecione um produto na tabela para editar.");
            return;
        }

        String newName = prodName.getText().trim();
        String newPriceText = prodPrice.getText().trim().replace(",", ".");
        String newDescription = prodDescription.getText().trim();

        if (newName.isBlank() || newPriceText.isBlank() || newDescription.isBlank()) {
            showAlert("Campos obrigatórios", "Preencha todos os campos para editar.");
            return;
        }

        double newPrice;
        try {
            newPrice = Double.parseDouble(newPriceText);
            if (newPrice <= 0) {
                showAlert("Preço inválido", "O preço deve ser maior que zero.");
                return;
            }
        } catch (NumberFormatException e) {
            showAlert("Erro de formato", "Digite um número válido para o preço.");
            return;
        }

        new ProductDAO().updateProduct(selected.getId(), newName, newPrice, newDescription);
        selected.setName(newName);
        selected.setPrice(newPrice);
        selected.setDescription(newDescription);
        productsTable.refresh();

        showAlert("Sucesso", "Produto atualizado com sucesso!");
        clearFields();
    }

    /**
     * Exclui o produto selecionado da lista e do banco.
     */
    @FXML
    public void deleteSelectedProduct() {
        Product selected = productsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Nenhum produto selecionado", "Selecione um produto para excluir.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirmar exclusão");
        confirm.setHeaderText(null);
        confirm.setContentText("Deseja realmente excluir \"" + selected.getName() + "\"?");
        confirm.getDialogPane().getStylesheets().add(getClass().getResource("/style/style.css").toExternalForm());

        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                new ProductDAO().deleteProduct(selected.getId());
                productList.remove(selected);
            }
        });
    }

    /**
     * Aplica estilização visual à tabela, como fonte personalizada.
     */
    private void applyTableStyling() {
        Font oswald = Font.loadFont(getClass().getResourceAsStream("/fonts/Oswald-SemiBold.ttf"), 14);
        productsTable.getColumns().forEach(col -> {
            col.setReorderable(false);
            col.setSortable(false);
            col.setResizable(false);
        });
        colDescription.setMaxWidth(Double.MAX_VALUE);
        colDescription.setResizable(true);
    }

    /**
     * Aplica filtro dinâmico à tabela com base no campo de busca.
     */
    private void applySearchFilter() {
        FilteredList<Product> filteredList = new FilteredList<>(productList, p -> true);

        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            String filter = newVal.trim().toLowerCase();
            filteredList.setPredicate(product ->
                    product.getName().toLowerCase().contains(filter) ||
                            product.getDescription().toLowerCase().contains(filter)
            );
        });

        SortedList<Product> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(productsTable.comparatorProperty());
        productsTable.setItems(sortedList);
    }

    /**
     * Configura a tabela com colunas e renderização personalizada com tooltips.
     */
    private void setupTableColumns() {
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        colName.setCellFactory(tc -> createTooltipCell());
        colDescription.setCellFactory(tc -> createTooltipCell());
        colPrice.setCellFactory(tc -> new TableCell<>() {
            @Override
            protected void updateItem(Double price, boolean empty) {
                super.updateItem(price, empty);
                if (empty || price == null) {
                    setGraphic(null);
                } else {
                    String formatted = NumberFormat.getCurrencyInstance(new Locale("pt", "BR")).format(price);
                    Label label = new Label(formatted);
                    label.setStyle("-fx-padding: 5px;");
                    Tooltip tooltip = new Tooltip(formatted);
                    tooltip.setWrapText(true);
                    tooltip.setMaxWidth(300);
                    tooltip.setStyle("-fx-font-size: 13px; -fx-background-color: white; -fx-text-fill: black; -fx-padding: 10px; -fx-border-color: #ccc;");
                    Tooltip.install(label, tooltip);
                    setGraphic(label);
                }
            }
        });

        colName.setPrefWidth(200);
        colDescription.setPrefWidth(425);
        colPrice.setPrefWidth(120);
    }

    private TableCell<Product, String> createTooltipCell() {
        return new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    Label label = new Label(item);
                    label.setWrapText(true);
                    label.setMaxWidth(Double.MAX_VALUE);
                    label.setStyle("-fx-padding: 5px;");
                    Tooltip tooltip = new Tooltip(item);
                    tooltip.setWrapText(true);
                    tooltip.setMaxWidth(300);
                    tooltip.setStyle("-fx-font-size: 13px; -fx-background-color: white; -fx-text-fill: black; -fx-padding: 10px; -fx-border-color: #ccc;");
                    Tooltip.install(label, tooltip);
                    setGraphic(label);
                }
            }
        };
    }

    /**
     * Limpa os campos de entrada e desmarca a seleção na tabela.
     */
    @FXML
    public void clearFields() {
        prodName.clear();
        prodPrice.clear();
        prodDescription.clear();
        productsTable.getSelectionModel().clearSelection();
    }

    /**
     * Exibe um alerta estilizado personalizado.
     *
     * @param title   Título da janela de alerta.
     * @param message Mensagem exibida.
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.getDialogPane().getStylesheets().add(getClass().getResource("/style/style.css").toExternalForm());
        alert.showAndWait();
    }

    /**
     * Desloga o usuário atual e retorna para a tela de login.
     */
    @FXML
    private void logout() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmação de Logout");
        alert.setHeaderText(null);
        alert.setContentText("Tem certeza que deseja deslogar?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                Session.loggedUser = null;

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/LoginView.fxml"));
                Parent root = loader.load();

                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.initStyle(StageStyle.UNDECORATED);
                stage.show();

                Stage currentStage = (Stage) logoutButton.getScene().getWindow();
                currentStage.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void initializeTableContent() {
        productList.addAll(new ProductDAO().listByUser(Session.loggedUser));
        productsTable.setItems(productList);
        productsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void setupSelectionBehavior() {
        productsTable.setOnMouseClicked(event -> {
            Product selected = productsTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                prodName.setText(selected.getName());
                prodPrice.setText(String.format(Locale.US, "%.2f", selected.getPrice()).replace(".", ","));
                prodDescription.setText(selected.getDescription());
            }
        });
    }

    @FXML
    public void minimizeWindow(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setIconified(true);
    }

    @FXML
    public void closeApplication(MouseEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmação de Saída");
        alert.setHeaderText(null);
        alert.setContentText("Tem certeza que deseja sair do programa?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Platform.exit();
        }

    }
}