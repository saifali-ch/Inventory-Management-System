package controllers;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import database.DBConnection;
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
import util.GMSAlert;
import util.NumberTextField;
import util.SearchFilter;

import java.sql.SQLException;
import java.time.LocalDate;


public class AddStockController {
    public TableView<Product> product_table;
    public TableColumn<Product, Integer> id_col;
    public TableColumn<Product, String> name_col;
    public TableColumn<Product, String> category_col;
    public TableColumn<Product, String> description_col;
    public TableColumn<Product, HBox> action_col;
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
            protected void updateItem(HBox hBox, boolean empty) {
                super.updateItem(hBox, empty);
                setGraphic(empty ? null : hbox);
            }
        });
    }
    
    private void createSearchFilter() {
        SearchFilter<Product> searchFilter = new SearchFilter<>(searchBar, product_table, filteredProduct_list);
        searchFilter.setCodeToAdjustColumnWidth(() -> {
                    if (searchFilter.getMatchedRecords() > 16)
                        description_col.setPrefWidth(363);
                    else description_col.setPrefWidth(376);
                }
        );
    }
    
    @FXML
    void filterProductsByCategory(ActionEvent event) {
        filteredProduct_list.clear();
        String categorySelected = filterCategory_box.getValue();
        allProduct_list.stream()
                .filter(p -> p.getCategory().equals(categorySelected) || categorySelected.equals("All"))
                .forEach(filteredProduct_list::add);
    }
    
    public void updateStock(ActionEvent event) {
    }
    
    public void addStock(ActionEvent event) {
        String stock = "Are you sure to Add the selected product Stock";
        GMSAlert alert = new GMSAlert(GMSAlert.AlertType.ADD_PRODUCT, stock);
        alert.setFxmlPath("/views/alerts/AddProductAlert.fxml");
        alert.show();
        alert.onYes(() -> {
            Integer productID = Integer.valueOf(productID_field.getText());
            String productName = productName_field.getText();
            String productCategory = productCategory_field.getText();
            String productDescription = productDescription_txt.getText();
    
            LocalDate stockAdded = stockAdded_date.getValue();
            Integer notifyOn = Integer.valueOf(notifyOn_field.getText());
            Integer quantity = Integer.valueOf(quantity_field.getText());
            Integer totalPrice = Integer.valueOf(totalPrice_field.getText());
            Integer pricePerProduct = totalPrice / quantity;
            int lastSid = -1;
            try {
                // AutoIncrement via Sequence, Inserting stock into database
                String query = String.format("Insert into stock values(stock_seq.nextval, %d, to_date('%s', 'yyyy-mm-dd hh24:mi:ss'),'%d', '%d', '%d')", productID, stockAdded, notifyOn, totalPrice, quantity);
                DBConnection.executeUpdate(query);
    
                // Getting Id of the Last Inserted Stock to show inside table
                String idQuery = "SELECT MAX(id) FROM stock";
                lastSid = DBConnection.getIntResult(idQuery);
            } catch (SQLException sqlException) {
                System.out.println("Add Stock Exception = " + sqlException.getMessage());
            }
        });
    }
}
