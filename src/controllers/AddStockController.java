package controllers;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
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
import javafx.scene.layout.StackPane;
import models.Product;
import util.NumberTextField;
import util.SearchFilter;

import java.time.LocalDate;


public class AddStockController {
    public TableView<Product> product_table;
    public TableColumn<Product, Integer> id_col;
    public TableColumn<Product, String> name_col;
    public TableColumn<Product, String> category_col;
    public TableColumn<Product, String> description_col;
    public TableColumn<Product, String> action_col;
    public ComboBox<String> filterCategory_box;
    public Label totalProducts_label;
    public JFXTextField productID_field;
    public JFXTextField productName_field;
    public JFXTextField productCategory_field;
    public JFXTextField productDescription_txt;
    public JFXDatePicker stockAdded_date;
    public JFXTextField notifyOn_field;
    public JFXTextField quantity_field;
    public JFXTextField totalPrice_field;
    public StackPane searchBar;
    private static final ObservableList<Product> allProduct_list = ProductController.allProduct_list;
    private static final ObservableList<Product> filteredProduct_list = FXCollections.observableArrayList();
    
    public void initialize() {
        createTable();
        createSearchFilter();
        
        filteredProduct_list.clear(); // Must clear otherwise it may contain duplicate value
        filteredProduct_list.addAll(allProduct_list);
        
        filterCategory_box.setItems(ProductController.filterCategory_list);
        filterCategory_box.getSelectionModel().select("All");
        
        totalProducts_label.setText(String.valueOf(allProduct_list.size()));
    
        NumberTextField.makeFieldsNumberOnly(notifyOn_field, quantity_field, totalPrice_field);
        
        // All the product related fields are view only. So, make them un-editable
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
        SearchFilter<Product> searchFilter = new SearchFilter<>(searchBar, product_table, filteredProduct_list);
        searchFilter.setCodeToAdjustColumnWidth(() -> {
                    if (searchFilter.getMatchedRecords() <= 19)
                        description_col.setPrefWidth(376);
                    else
                        description_col.setPrefWidth(363);
                }
        );
    }
    
    @FXML
    void filterProductsByCategory(ActionEvent event) {
        String categorySelected = filterCategory_box.getValue();
        filterCategory_box.selectionModelProperty().addListener((observableValue, oldValue, newValue) -> {
        
        });
        if (categorySelected == null)
            return;
        filteredProduct_list.clear();
        if (categorySelected.equals("All")) {
            filteredProduct_list.addAll(allProduct_list);
        } else allProduct_list.stream() // Convert to Stream
                .filter(p -> p.getCategory().equals(categorySelected)) // Removes/Filters products whose category doesn't match
                .forEach(filteredProduct_list::add); // Add all the remaining products to filtered product list
    }
    
    public void updateStock(ActionEvent event) {
    }
    
    public void addStock(ActionEvent event) {
        Integer productID = Integer.valueOf(productID_field.getText());
        LocalDate stockAdded = stockAdded_date.getValue();
    }
}
