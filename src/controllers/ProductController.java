package controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import database.DBConnection;
import javafx.beans.InvalidationListener;
import javafx.beans.binding.BooleanBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.skin.ComboBoxListViewSkin;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import models.Product;
import util.GMSAlert;
import util.SearchFilter;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductController {
    public TableView<Product> product_table;
    public TableColumn<Product, Integer> id_col;
    public TableColumn<Product, String> name_col;
    public TableColumn<Product, String> category_col;
    public TableColumn<Product, String> description_col;
    public TableColumn<Product, HBox> action_col;
    public ComboBox<String> filterCategory_box;
    public ComboBox<String> deleteCategory_box;
    public ComboBox<String> productCategory_box;
    public TextField productName_field;
    public TextField categoryName_field;
    public JFXTextArea productDescription_field;
    public JFXButton addProduct_btn;
    public JFXButton addCategory_btn;
    public JFXButton deleteCategory_btn;
    public Label totalProducts_label;
    public StackPane searchBar;
    public static final ObservableList<String> allCategory_list = FXCollections.observableArrayList();
    public static final ObservableList<Product> allProduct_list = FXCollections.observableArrayList();
    public static final ObservableList<String> filterCategory_list = FXCollections.observableArrayList();
    public static final ObservableList<Product> filteredProduct_list = FXCollections.observableArrayList();
    private Product globalProduct_obj; // Used to update product
    
    public void initialize() {
        createTable();
        configComboBoxes();
        createSearchFilter();
        addListenersAndFormValidators();
        loadProducts();
        loadAllCategories();
        loadFilterCategories();
    }
    
    private void createTable() {
        product_table.setPlaceholder(new Label("No Product Available"));
        id_col.setCellValueFactory(new PropertyValueFactory<>("id"));
        name_col.setCellValueFactory(new PropertyValueFactory<>("name"));
        category_col.setCellValueFactory(new PropertyValueFactory<>("category"));
        description_col.setCellValueFactory(new PropertyValueFactory<>("description"));
        action_col.setCellFactory(p -> new TableCell<>() {
            HBox hbox = null;
            
            {
                try {
                    hbox = FXMLLoader.load(getClass().getResource("/views/ActionColumn.fxml"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                hbox.setOnMouseEntered(e -> hbox.setCursor(Cursor.HAND));
                
                StackPane delete_pane = (StackPane) hbox.lookup("#delete_pane");
                delete_pane.setOnMouseClicked(e -> deleteProduct());
                
                StackPane update_pane = (StackPane) hbox.lookup("#update_pane");
                update_pane.setOnMouseClicked(e -> {
//                    globalProduct = getTableView().getItems().get(getIndex());
                    globalProduct_obj = getTableRow().getItem();
                    productCategory_box.getSelectionModel().select(globalProduct_obj.getCategory());
                    productName_field.setText(globalProduct_obj.getName());
                    productDescription_field.setText(globalProduct_obj.getDescription());
                });
            }
    
            @Override
            protected void updateItem(HBox hBox, boolean empty) {
                super.updateItem(hBox, empty);
                setGraphic(empty ? null : hbox);
            }
        });
    }
    
    @FXML
    void deleteProduct() {
        Product product = product_table.getSelectionModel().getSelectedItem();
        GMSAlert deleteAlert = new GMSAlert(GMSAlert.AlertType.DELETE_PRODUCT, product);
        deleteAlert.setFxmlPath("/views/alerts/DeleteProductAlert.fxml");
        deleteAlert.show();
        deleteAlert.onYes(() -> {
            allProduct_list.remove(product);
            filteredProduct_list.remove(product);
            long no_of_products_in_category = allProduct_list.stream()
                    .filter(p -> p.getCategory().equals(product.getCategory()))
                    .count();
            if (no_of_products_in_category == 0) {
                filterCategory_list.remove(product.getCategory());
                filterCategory_box.getSelectionModel().select("All");
            }
            String query = String.format("Delete from product where id = %d", product.getId());
            try {
                DBConnection.executeUpdate(query);
            } catch (SQLException productRemove) {
                productRemove.printStackTrace();
            }
        });
    }
    
    private void configComboBoxes() {
        productCategory_box.setOnShowing(this::comboBoxScrollConfig);
        deleteCategory_box.setOnShowing(this::comboBoxScrollConfig);
        filterCategory_box.setOnShowing(this::comboBoxScrollConfig);
        Label label = new Label("Empty Category List");
        productCategory_box.setPlaceholder(label);
        deleteCategory_box.setPlaceholder(label);
        filterCategory_box.setPlaceholder(label);
    }
    
    private void addListenersAndFormValidators() {
        // Updates total no of products label
        allProduct_list.addListener((InvalidationListener) c -> {
            String totalProducts = String.valueOf(allProduct_list.size());
            totalProducts_label.setText(totalProducts);
        });
        
        // Add Category Button will be disabled if category name is blank or empty
        categoryName_field.textProperty().addListener((observableValue, oldValue, newValue) -> {
            addCategory_btn.setDisable(newValue.trim().isEmpty());
        });
        
        // Delete category button will be disabled if category being deleted is not selected.
        BooleanBinding delCatSelected = deleteCategory_box.getSelectionModel().selectedIndexProperty().isEqualTo(-1);
        deleteCategory_btn.disableProperty().bind(delCatSelected);
        
        // Product name field will be disabled if product category is not selected.
        BooleanBinding proCatSelected = productCategory_box.getSelectionModel().selectedIndexProperty().isEqualTo(-1);
        productName_field.disableProperty().bind(proCatSelected);
        
        // Add products button will be disabled if product name is blank or empty
        productName_field.textProperty().addListener((observableValue, oldValue, newValue) -> {
            addProduct_btn.setDisable(newValue.trim().isEmpty());
        });
    }
    
    private void loadProducts() {
        String query = "select * from product_view";
        try {
            ResultSet rs = DBConnection.executeQuery(query);
            allProduct_list.clear();
            filteredProduct_list.clear();
            while (rs.next()) {
                Product product = new Product(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4));
                allProduct_list.add(product);
            }
        } catch (SQLException sqlException) {
            System.out.println("Load Products Exception = " + sqlException.getMessage());
        }
        filteredProduct_list.addAll(allProduct_list);
    }
    
    private void createSearchFilter() {
        SearchFilter<Product> searchFilter = new SearchFilter<>(searchBar, product_table, filteredProduct_list);
        searchFilter.setCodeToAdjustColumnWidth(() -> {
                    if (searchFilter.getMatchedRecords() > 21)
                        description_col.setPrefWidth(320);
                    else description_col.setPrefWidth(335);
                }
        );
    }
    
    private void loadFilterCategories() {
        String query = "select * from filter_category_view";
        try {
            ResultSet rs = DBConnection.executeQuery(query);
            filterCategory_list.clear();
            filterCategory_list.add(0, "All");
            while (rs.next())
                filterCategory_list.add(rs.getString(1));
        } catch (SQLException sqlException) {
            System.out.println("Load Filter Categories Exception = " + sqlException.getMessage());
        }
        filterCategory_box.setItems(filterCategory_list);
        filterCategory_box.getSelectionModel().select("All");
    }
    
    private void loadAllCategories() {
        String query = "Select name from category order by name";
        try {
            ResultSet rs = DBConnection.executeQuery(query);
            allCategory_list.clear();
            while (rs.next())
                allCategory_list.add(rs.getString(1));
        } catch (SQLException sqlException) {
            System.out.println("Load All Categories Exception = " + sqlException.getMessage());
        }
        productCategory_box.setItems(allCategory_list);
        deleteCategory_box.setItems(allCategory_list);
    }
    
    private void comboBoxScrollConfig(Event event) {
        ComboBox<?> comboBox = (ComboBox<?>) event.getTarget();
        ListView<?> listView = (ListView<?>) ((ComboBoxListViewSkin<?>) comboBox.getSkin()).getPopupContent();
        listView.scrollTo(comboBox.getSelectionModel().getSelectedIndex());
    }
    
    @FXML
    void filterProductsByCategory(ActionEvent event) {
        filteredProduct_list.clear();
        String categorySelected = filterCategory_box.getValue();
        allProduct_list.stream()
                .filter(p -> p.getCategory().equals(categorySelected) || categorySelected.equals("All"))
                .forEach(filteredProduct_list::add);
    }
    
    @FXML
    void addCategory(ActionEvent event) throws SQLException {
        String categoryName = categoryName_field.getText().trim();
        boolean categoryAlreadyExists = false;
        for (String s : allCategory_list) {
            if (s.equalsIgnoreCase(categoryName)) {
                categoryAlreadyExists = true;
                break;
            }
        }
        if (categoryAlreadyExists) {
            String alertString = String.format("Category \"%s\" Already Exists!", categoryName);
            new Alert(Alert.AlertType.WARNING, alertString).showAndWait();
            categoryName_field.requestFocus();
            categoryName_field.selectAll();
        } else {
            String query = String.format("insert into category values(cat_seq.nextval, '%s')", categoryName);
            DBConnection.executeUpdate(query);
            allCategory_list.add(categoryName);
            categoryName_field.clear();
            productCategory_box.getSelectionModel().selectLast();
        }
    }
    
    @FXML
    void deleteCategory(ActionEvent event) throws SQLException {
        List<Integer> productIDList = new ArrayList<>();
        String categoryBeingDeleted = deleteCategory_box.getValue();
        
        allProduct_list.forEach(product -> {
            if (product.getCategory().equals(categoryBeingDeleted))
                productIDList.add(product.getId());
        });
        
        if (productIDList.size() > 0) {
            String oldCatSelected = filterCategory_box.getValue();
            filterCategory_box.getSelectionModel().select(categoryBeingDeleted); // Show products of the selected category in table
            GMSAlert alert = new GMSAlert(GMSAlert.AlertType.DELETE_CATEGORY, productIDList.size());
            alert.setFxmlPath("/views/alerts/DeleteCategoryAlert.fxml");
            alert.show();
            alert.onYes(() -> {
                allProduct_list.removeAll(filteredProduct_list); // Deleting local copy of the products of the selected category
                filteredProduct_list.clear(); // Deleting all the products from table
                for (var i : productIDList) { // deleting all the products of selected category from database
                    try {
                        DBConnection.executeUpdate("Delete from product where id = " + i);
                    } catch (SQLException sqlException) {
                        sqlException.printStackTrace();
                    }
                }
                allCategory_list.remove(categoryBeingDeleted);
                filterCategory_list.remove(categoryBeingDeleted);
                try {
                    DBConnection.executeUpdate(String.format("Delete from category where name = '%s'", categoryBeingDeleted));
                } catch (SQLException sqlException) {
                    sqlException.printStackTrace();
                }
                
                //Following statement must be executed at the end of all statements
                filterCategory_box.getSelectionModel().select("All");
            });
            alert.onCancelRun(() -> filterCategory_box.getSelectionModel().select(oldCatSelected));
        } else { // If no products exists in the selected category
            allCategory_list.remove(categoryBeingDeleted);
            DBConnection.executeUpdate(String.format("Delete from category where name = '%s'", categoryBeingDeleted));
        }
    }
    
    @FXML
    void updateProduct(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Update Product");
        alert.setContentText("Are you sure to update the product");
        Optional<ButtonType> option = alert.showAndWait();
        option.ifPresent(e -> {
            if (option.get().getText().equalsIgnoreCase("ok")) {
                int productID = globalProduct_obj.getId();
                String productName = productName_field.getText();
                String productCategory = productCategory_box.getValue();
                String productDescription = productDescription_field.getText();
                globalProduct_obj.setName(productName);
                globalProduct_obj.setCategory(productCategory);
                globalProduct_obj.setDescription(productDescription);
                try {
                    // Get id of the selected category from Category Table to Insert into Product Table
                    String categoryIdQuery = String.format("Select id from category where name='%s'", productCategory);
                    int categoryID = DBConnection.getIntResult(categoryIdQuery);
    
                    // Updating Product details in database
                    String query = String.format("Update product set name='%s', category_id=%d, description='%s' where id=%d",
                            productName, categoryID, productDescription, productID);
                    
                    // Clears Product & Description Fields
                    productName_field.clear();
                    productDescription_field.clear();
                    DBConnection.executeQuery(query);
                } catch (SQLException sqlException) {
                    sqlException.printStackTrace();
                }
            }
        });
    }
    
    @FXML
    void addProduct(ActionEvent event) {
        String productName = productName_field.getText().trim();
        GMSAlert alert = new GMSAlert(GMSAlert.AlertType.ADD_PRODUCT, productName);
        alert.setFxmlPath("/views/alerts/AddProductAlert.fxml");
        alert.show();
        alert.onYes(() -> {
            String productCategory = productCategory_box.getValue();
            String productDescription = productDescription_field.getText().trim();
            productDescription = productDescription.isEmpty() ? "Description not Provided" : productDescription;
            int lastPid = -1;
            try {
                // Get id of the selected category from Category Table to Insert into Product Table
                String categoryIdQuery = String.format("Select id from category where name='%s'", productCategory);
                int categoryID = DBConnection.getIntResult(categoryIdQuery);
                
                // AutoIncrement via Sequence, Inserting product into database
                String query = String.format("Insert into product values(pro_seq.nextval, '%s', %d, '%s')", productName, categoryID, productDescription);
                DBConnection.executeUpdate(query);
                
                // Getting Id of the Last Inserted Product to show inside table
                String idQuery = "SELECT MAX(id) FROM product";
                lastPid = DBConnection.getIntResult(idQuery);
            } catch (SQLException sqlException) {
                System.out.println("Add Product Exception = " + sqlException.getMessage());
            }
            
            // Adding product to local copy of products.
            Product p = new Product(lastPid, productName, productCategory, productDescription);
            allProduct_list.add(p);
            filteredProduct_list.add(p);
            
            // Check weather category already exists in FilterCategoryList if not exists add it.
            if (!filterCategory_list.contains(productCategory))
                filterCategory_list.add(productCategory);
            
            // Clears ProductName & Description Fields
            productName_field.clear();
            productDescription_field.clear();
        });
    }
}

