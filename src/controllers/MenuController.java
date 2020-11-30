package controllers;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import util.PaneHandler;
import util.StageHandler;


public class MenuController {
    
    public Label paneName_label;
    public AnchorPane centerPane;
    
    public void initialize() {
    }
    
    public void setCenterPane(ActionEvent event) {
        Button button = (Button) event.getSource();
        paneName_label.setText(button.getText());
        
        String path = button.getAccessibleText(); // It contains the fxml path
        centerPane.getChildren().setAll(PaneHandler.loadCachedPane(path));
        
        Stage stage = StageHandler.getStage(event);
        stage.titleProperty().bind(button.textProperty());
    }
}
