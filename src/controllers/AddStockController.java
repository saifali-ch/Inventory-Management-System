package controllers;

import com.jfoenix.controls.JFXDatePicker;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import models.Product;


public class AddStockController {
    private static final ObservableList<Product> allProduct_list = FXCollections.observableArrayList();
    private static final ObservableList<String> filterCategory_list = FXCollections.observableArrayList();
    private static final ObservableList<Product> filteredProduct_list = FXCollections.observableArrayList();
    public TableView<Product> product_table;
    public TableColumn<Product, Integer> id_col;
    public TableColumn<Product, String> name_col;
    public TableColumn<Product, String> category_col;
    public TableColumn<Product, String> description_col;
    public TableColumn<Product, String> action_col;
    public TextField searchBar_field;
    public ComboBox<String> filterCategory_box;
    public Label searchBar_label;
    public Label totalProducts_label;
    public JFXDatePicker datePicker;
    
    public void initialize() {
        createTable();
        loadData();
        addListeners();
    }
    
    private void createTable() {
        product_table.setPlaceholder(new Label("No Product Available"));
        id_col.setCellValueFactory(new PropertyValueFactory<>("id"));
        name_col.setCellValueFactory(new PropertyValueFactory<>("name"));
        category_col.setCellValueFactory(new PropertyValueFactory<>("category"));
        description_col.setCellValueFactory(new PropertyValueFactory<>("description"));
        action_col.setCellFactory(p -> new TableCell<>() {
            final HBox hbox = new HBox();
            
            {
                hbox.setOnMouseEntered(e -> hbox.setCursor(Cursor.HAND));
                hbox.setPrefWidth(50);
                hbox.setPrefHeight(30);
                hbox.setPadding(new Insets(12.5));
                ImageView view = new ImageView(getClass().getResource("/media/icons/2x/check.png").toExternalForm());
                view.setFitWidth(25);
                view.setFitHeight(25);
                hbox.getChildren().add(view);
                hbox.setOnMouseClicked(e -> System.out.println("ProductPopupController.instance initializer"));
            }
            
            @Override
            protected void updateItem(String s, boolean empty) {
                super.updateItem(s, empty);
                setGraphic(empty ? null : hbox);
            }
        });
    }
    
    private void loadData() {
        allProduct_list.addAll(ProductController.allProduct_list);
        
        filteredProduct_list.addAll(ProductController.filteredProduct_list);
        filterCategory_list.addAll(ProductController.filterCategory_list);
        
        filterCategory_box.setItems(filterCategory_list);
        filterCategory_box.getSelectionModel().select("All");
    }
    
    private void addListeners() {
    
    }
    
    @FXML
    void filterProductsByCategory(ActionEvent event) {
        String categorySelected = filterCategory_box.getValue();
        if (categorySelected == null)
            return;
        filteredProduct_list.clear();
        if (categorySelected.equals("All")) {
            filteredProduct_list.addAll(allProduct_list);
        } else allProduct_list.stream() // Convert to Stream
                .filter(p -> p.getCategory().equals(categorySelected)) // Removes/Filters products whose category doesn't match
                .forEach(filteredProduct_list::add); // Add all the remaining products to filtered product list
    }
}
