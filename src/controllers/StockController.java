package controllers;

import com.jfoenix.controls.JFXDatePicker;
import javafx.beans.InvalidationListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import models.Stock;
import util.PaneHandler;
import util.Panes;
import util.SearchFilter;
import util.StageHandler;

import java.util.stream.Collectors;

public class StockController {
    
    public ComboBox<String> filterCategory_box;
    public JFXDatePicker start_date;
    public JFXDatePicker end_date;
    public Label totalProductsInStock_label;
    public Label totalStockRecords_label;
    public TableView<Stock> stock_table;
    public TableColumn<Stock, Integer> id_col;
    public TableColumn<Stock, String> productName_col;
    public TableColumn<Stock, String> productCategory_col;
    public TableColumn<Stock, String> dateAdded_col;
    public TableColumn<Stock, Integer> notifyOn_col;
    public TableColumn<Stock, Integer> pricePerProduct_col;
    public TableColumn<Stock, Integer> totalPrice_col;
    public TableColumn<Stock, Integer> quantity_col;
    public TableColumn<Stock, HBox> action_col;
    public StackPane searchBar;
    private static final ObservableList<Stock> allStock_list = FXCollections.observableArrayList();
    private static final ObservableList<Stock> filteredStock_list = FXCollections.observableArrayList();
    private static final ObservableList<String> filterCategory_list = FXCollections.observableArrayList();
    
    public void initialize() {
        createTable();
        createSearchFilter();
        addListenersAndFormValidators();
        loadStock();
        loadFilterCategories();
    }
    
    private void createTable() {
        stock_table.setPlaceholder(new Label("Stock Empty"));
        id_col.setCellValueFactory(new PropertyValueFactory<>("id"));
        productName_col.setCellValueFactory(new PropertyValueFactory<>("productName"));
        productCategory_col.setCellValueFactory(new PropertyValueFactory<>("productCategory"));
        dateAdded_col.setCellValueFactory(new PropertyValueFactory<>("dateAdded"));
        notifyOn_col.setCellValueFactory(new PropertyValueFactory<>("notifyOn"));
        pricePerProduct_col.setCellValueFactory(new PropertyValueFactory<>("pricePerProduct"));
        totalPrice_col.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
        quantity_col.setCellValueFactory(new PropertyValueFactory<>("quantity"));

//       TODO action_col
    }
    
    private void createSearchFilter() {
        SearchFilter<Stock> searchFilter = new SearchFilter<>(searchBar, stock_table, filteredStock_list);
        searchFilter.setCodeToAdjustColumnWidth(() -> {
            if (searchFilter.getMatchedRecords() > 17)
                quantity_col.setPrefWidth(100);
            else quantity_col.setPrefWidth(113);
        });
    }
    
    private void addListenersAndFormValidators() {
        // Updates "Total Products In Stock label" and "Total Stock Records label"
        allStock_list.addListener((InvalidationListener) c -> {
            int totalStockRecords = allStock_list.stream().mapToInt(Stock::getQuantity).sum();
            totalProductsInStock_label.setText(String.valueOf(totalStockRecords));
    
            String totalProducts = String.valueOf(allStock_list.size());
            totalStockRecords_label.setText(totalProducts);
        });
    }
    
    private void loadStock() {
        allStock_list.addAll(
                new Stock(1, "Test", "A", "13/04/2001", 2, 200, 1233, 12),
                new Stock(2, "Hello", "B", "13/04/2001", 2, 200, 1233, 12)
        );
        filteredStock_list.addAll(allStock_list);
    }
    
    private void loadFilterCategories() {
        filterCategory_list.addAll(allStock_list.stream()
                .map(Stock::getProductCategory)
                .distinct()
                .collect(Collectors.toList())
        );
        filterCategory_box.setItems(filterCategory_list);
        filterCategory_list.add(0, "All");
        filterCategory_box.getSelectionModel().select("All");
    }
    
    @FXML
    void filterStockByCategory(ActionEvent event) {
        filteredStock_list.clear();
        String categorySelected = filterCategory_box.getValue();
        filteredStock_list.addAll(allStock_list.stream()
                .filter(p -> p.getProductCategory().equals(categorySelected) || categorySelected.equals("All"))
                .collect(Collectors.toList()));
    }
    
    @FXML
    void AddStock(ActionEvent event) {
        AnchorPane addStock_pane = PaneHandler.loadCachedPane(Panes.ADD_STOCK);
        AddStockController.refreshData();
    
        Scene scene = StageHandler.getStage(event).getScene();
        AnchorPane centerPane = (AnchorPane) scene.lookup("#centerPane");
        Label paneName_label = (Label) scene.lookup("#heading_label");
        centerPane.getChildren().setAll(addStock_pane);
        paneName_label.setText("Add Stock");
    }
    
    @FXML
    void printStock(ActionEvent event) {
        allStock_list.add(new Stock(3, "Test", "Test", "13/04/2001", 2, 200, 32, 10));
        filteredStock_list.add(new Stock(3, "Test", "Test", "13/04/2001", 2, 200, 32, 10));
    }
}

