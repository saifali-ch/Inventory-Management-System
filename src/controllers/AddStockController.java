package controllers;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import database.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import models.Product;
import util.GMSAlert;
import util.NumberTextField;
import util.Panes;
import util.SearchFilter;

import java.time.LocalDate;
import java.util.stream.Collectors;


public class AddStockController extends StockController {
    public TableView<Product> product_table;
    public TableColumn<Product, Integer> id_col;
    public TableColumn<Product, String> name_col;
    public TableColumn<Product, String> category_col;
    public TableColumn<Product, String> description_col;
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
    private static final ObservableList<Product> product_list = FXCollections.observableArrayList();
    
    public void initialize() {
        createTable();
        createSearchFilter();
        AddStockController.refreshData();
        
        filterCategory_box.setItems(ProductController.filterCategory_list);
        filterCategory_box.getSelectionModel().select("All");
        totalProducts_label.setText(String.valueOf(product_list.size()));
        NumberTextField.makeFieldsNumberOnly(notifyOn_field, quantity_field, totalPrice_field);
        
        // All the product related fields are view only. So, make them un-editable
        productID_field.setEditable(false);
        productName_field.setEditable(false);
        productCategory_field.setEditable(false);
        productDescription_txt.setEditable(false);
    }
    
    private void createTable() {
        product_table.setPlaceholder(new Label("Add Some Products First"));
        id_col.setCellValueFactory(new PropertyValueFactory<>("id"));
        name_col.setCellValueFactory(new PropertyValueFactory<>("name"));
        category_col.setCellValueFactory(new PropertyValueFactory<>("category"));
        description_col.setCellValueFactory(new PropertyValueFactory<>("description"));
    
        product_table.setOnMouseClicked(e -> {
            if (!product_table.getSelectionModel().isEmpty()) {
                Product selectedProduct = product_table.getSelectionModel().getSelectedItem();
                productID_field.setText(String.valueOf(selectedProduct.getId()));
                productName_field.setText(selectedProduct.getName());
                productCategory_field.setText(selectedProduct.getCategory());
                productDescription_txt.setText(selectedProduct.getDescription());
            }
        });
    }
    
    private void createSearchFilter() {
        SearchFilter<Product> searchFilter = new SearchFilter<>(searchBar, product_table, product_list);
        searchFilter.setCodeToAdjustColumnWidth(() -> {
                    if (searchFilter.getMatchedRecords() > 16)
                        description_col.setPrefWidth(378);
                    else description_col.setPrefWidth(391);
                }
        );
    }
    
    public static void refreshData() {
        product_list.clear(); // Must clear otherwise it may contain duplicate value
        product_list.addAll(ProductController.productBackup_list);
    }
    
    @FXML
    void filterProductsByCategory(ActionEvent event) {
        product_list.clear();
        String categorySelected = filterCategory_box.getValue();
        product_list.addAll(ProductController.productBackup_list.stream()
                .filter(p -> p.getCategory().equals(categorySelected) || categorySelected.equals("All"))
                .collect(Collectors.toList()));
    }
    
    public void updateStock(ActionEvent event) {
    }
    
    public void addStock(ActionEvent event) {
        String stock = "Are you sure to add the selected product to Stock";
        GMSAlert alert = new GMSAlert(GMSAlert.AlertType.ADD_PRODUCT, stock);
        alert.show(Panes.ADD_PRODUCT_ALERT);
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
    
            // AutoIncrement via Sequence, Inserting stock into database
            String query = String.format("Insert into stock values(stock_seq.nextval, %d, to_date('%s', 'yyyy-mm-dd hh24:mi:ss'),'%d', '%d', '%d')", productID, stockAdded, notifyOn, totalPrice, quantity);
            DBConnection.executeUpdate(query);
            // Getting Id of the Last Inserted Stock to show inside table
            String idQuery = "SELECT MAX(id) FROM stock";
            int lastSid = DBConnection.getIntResult(idQuery);
        });
    }
}
