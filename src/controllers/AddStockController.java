package controllers;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import javafx.beans.InvalidationListener;
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
import util.SearchFilter;


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
    public JFXTextField productName_field;
    public JFXTextField productCategory_field;
    public JFXTextField productDescription_txt;
    public JFXTextField productID_field;
    
    public void initialize() {
        createTable();
        createSearchFilter();
        loadData();
        addListeners();
        
        productID_field.setEditable(false);
        productName_field.setEditable(false);
        productCategory_field.setEditable(false);
        productDescription_txt.setEditable(false);
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
                ImageView image = new ImageView(getClass().getResource("/media/icons/2x/check.png").toExternalForm());
                image.setFitWidth(25);
                image.setFitHeight(25);
                
                hbox.setPrefWidth(50);
                hbox.setPrefHeight(30);
                hbox.setPadding(new Insets(12.5)); //(HBoxPrefWidth - ImageFitWidth)/2 => (50 - 25)/2 = 12.5
                hbox.getChildren().add(image);
                
                hbox.setOnMouseEntered(e -> hbox.setCursor(Cursor.HAND));
                hbox.setOnMouseClicked(e -> {
                    Product globalProduct_obj = getTableView().getSelectionModel().getSelectedItem();
                    productID_field.setText(String.valueOf(globalProduct_obj.getId()));
                    productName_field.setText(globalProduct_obj.getName());
                    productCategory_field.setText(globalProduct_obj.getCategory());
                    productDescription_txt.setText(globalProduct_obj.getDescription());
                });
            }
            
            @Override
            protected void updateItem(String s, boolean empty) {
                super.updateItem(s, empty);
                setGraphic(empty ? null : hbox);
            }
        });
    }
    
    private void createSearchFilter() {
        new SearchFilter<>(searchBar_field, searchBar_label, product_table, filteredProduct_list,
                () -> {
                    if (SearchFilter.matchedRecords <= 19)
                        description_col.setPrefWidth(335);
                    else
                        description_col.setPrefWidth(320);
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
        // Updates total no of products
        allProduct_list.addListener((InvalidationListener) c -> {
            String totalProducts = String.valueOf(allProduct_list.size());
            totalProducts_label.setText(totalProducts);
        });
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
