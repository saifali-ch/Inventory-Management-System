package util;

import javafx.beans.InvalidationListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import models.Product;

public class SearchFilter<T> {
    private Integer matchedRecords = 0;
    private Runnable codeToAdjustColumnWidth = null;
    
    public SearchFilter(StackPane searchBar, TableView<T> tableView, ObservableList<T> list) {
        FilteredList<T> filteredList = new FilteredList<>(list);
        
        Label searchBar_label = (Label) searchBar.lookup(".label");
        TextField searchBar_field = (TextField) searchBar.lookup(".text-field");
        
        searchBar_label.setText(String.valueOf(filteredList.size()));
        searchBar_field.textProperty().addListener((o, v1, v2) -> filteredList.setPredicate(p -> {
            if (p instanceof Product)
                return ((Product) p).getName().toLowerCase().contains(v2.toLowerCase());
            return v2.isBlank();
        }));
        
        SortedList<T> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(tableView.comparatorProperty());
        tableView.setItems(sortedList);
        
        filteredList.addListener((InvalidationListener) o -> {
            matchedRecords = filteredList.size();
            searchBar_label.setText(String.valueOf(filteredList.size()));
            if (codeToAdjustColumnWidth != null)
                codeToAdjustColumnWidth.run();
        });
    }
    
    public void setCodeToAdjustColumnWidth(Runnable codeToAdjustColumnWidth) {
        this.codeToAdjustColumnWidth = codeToAdjustColumnWidth;
    }
    
    public int getMatchedRecords() {
        return matchedRecords;
    }
}
