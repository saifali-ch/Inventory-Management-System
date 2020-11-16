package models;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Stock {
    SimpleIntegerProperty id;
    SimpleStringProperty productName;
    SimpleStringProperty productCategory;
    SimpleStringProperty dateAdded;
    SimpleIntegerProperty notifyOn;
    SimpleIntegerProperty pricePerProduct;
    SimpleIntegerProperty totalPrice;
    SimpleIntegerProperty quantity;
    
    public Stock(Integer id, String productName, String productCategory, String dateAdded, Integer notifyOn, Integer pricePerProduct, Integer totalPrice, Integer quantity) {
        this.id = new SimpleIntegerProperty(id);
        this.productName = new SimpleStringProperty(productName);
        this.productCategory = new SimpleStringProperty(productCategory);
        this.dateAdded = new SimpleStringProperty(dateAdded);
        this.notifyOn = new SimpleIntegerProperty(notifyOn);
        this.pricePerProduct = new SimpleIntegerProperty(pricePerProduct);
        this.totalPrice = new SimpleIntegerProperty(totalPrice);
        this.quantity = new SimpleIntegerProperty(quantity);
    }
    
    public int getId() {
        return id.get();
    }
    
    public SimpleIntegerProperty idProperty() {
        return id;
    }
    
    public String getProductName() {
        return productName.get();
    }
    
    public SimpleStringProperty productNameProperty() {
        return productName;
    }
    
    public String getProductCategory() {
        return productCategory.get();
    }
    
    public SimpleStringProperty productCategoryProperty() {
        return productCategory;
    }
    
    public String getDateAdded() {
        return dateAdded.get();
    }
    
    public SimpleStringProperty dateAddedProperty() {
        return dateAdded;
    }
    
    public int getNotifyOn() {
        return notifyOn.get();
    }
    
    public SimpleIntegerProperty notifyOnProperty() {
        return notifyOn;
    }
    
    public int getPricePerProduct() {
        return pricePerProduct.get();
    }
    
    public SimpleIntegerProperty pricePerProductProperty() {
        return pricePerProduct;
    }
    
    public int getTotalPrice() {
        return totalPrice.get();
    }
    
    public SimpleIntegerProperty totalPriceProperty() {
        return totalPrice;
    }
    
    public int getQuantity() {
        return quantity.get();
    }
    
    public SimpleIntegerProperty quantityProperty() {
        return quantity;
    }
}
