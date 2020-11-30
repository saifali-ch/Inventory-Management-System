package controllers;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import util.PaneHandler;


public class MenuController {
    
    public AnchorPane centerPane;
    public Label heading_label;
    
    public void setCenterPane(ActionEvent event) {
        Button button = (Button) event.getSource();
        heading_label.setText(button.getText());
        String fxmlPath = button.getAccessibleText(); // It contains the fxml path
        AnchorPane cachedPane = PaneHandler.loadCachedPane(fxmlPath);
        centerPane.getChildren().setAll(cachedPane);
    }
}
