package util;

import javafx.beans.InvalidationListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import models.Product;

public class SearchFilter<T> {
    private final FilteredList<T> filteredList;
    public static int matchedRecords = 0;
    
    public SearchFilter(TextField searchBar, Label label, TableView<T> tableView, ObservableList<T> list, Runnable codeToAdjustColumnWidth) {
        filteredList = new FilteredList<>(list);
        searchBar.textProperty().addListener((o, v1, v2) -> filteredList.setPredicate(p -> {
            if (p instanceof Product)
                return ((Product) p).getName().toLowerCase().contains(v2.toLowerCase());
            return v2.isBlank();
        }));
        
        SortedList<T> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(tableView.comparatorProperty());
        tableView.setItems(sortedList);
        
        filteredList.addListener((InvalidationListener) o -> {
            label.setText(String.valueOf(filteredList.size()));
            matchedRecords = filteredList.size();
            if (codeToAdjustColumnWidth == null)
                System.out.println("Code to adjust column width is missing!");
            else codeToAdjustColumnWidth.run();
        });
    }
}
