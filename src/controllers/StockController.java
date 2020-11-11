package controllers;

import com.jfoenix.controls.JFXDatePicker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class StockController {
    
    public ComboBox<?> filterCategory_box;
    public TextField searchBar_field;
    public Label searchBar_label;
    public JFXDatePicker start_date;
    public JFXDatePicker end_date;
    public Label totalProducts_label;
    public Label totalRecords_label;
    public TableView<?> stock_table;
    public TableColumn<?, ?> id_col;
    public TableColumn<?, ?> productName_col;
    public TableColumn<?, ?> productCategory_col;
    public TableColumn<?, ?> dateAdded_col;
    public TableColumn<?, ?> notifyOn_col;
    public TableColumn<?, ?> pricePerProduct_col;
    public TableColumn<?, ?> totalPrice_col;
    public TableColumn<?, ?> quantity_col;
    public TableColumn<?, ?> action_col;
    
    public void initialize() {
    
    }
    
    @FXML
    void AddStock(ActionEvent event) throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("/views/AddStock.fxml"));
        Scene scene = new Scene(pane);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
    }
    
    @FXML
    void filterStockByCategory_box(ActionEvent event) {
    }
    
    @FXML
    void printStock(ActionEvent event) {
    
    }
}

