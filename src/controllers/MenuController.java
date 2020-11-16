package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;


public class MenuController {
    
    private static final HashMap<String, AnchorPane> cachedPanes = new HashMap<>();
    public Label paneName_label;
    @FXML
    private AnchorPane centerPane;
    
    public void initialize() throws IOException {
        String path = "/views/Product.fxml";
        cachedPanes.put(path, FXMLLoader.load(getClass().getResource(path)));
//        centerPane.getChildren().setAll(cachedPanes.get(path));
    }
    
    public void setCenterPane(ActionEvent event) throws IOException {
        Button button = (Button) event.getSource();
        String path = button.getAccessibleText();
        paneName_label.setText(button.getText());
        
        if (!cachedPanes.containsKey(path))
            cachedPanes.put(path, FXMLLoader.load(getClass().getResource(path)));
        centerPane.getChildren().setAll(cachedPanes.get(path));
        
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.titleProperty().bind(button.textProperty());
    }
}
