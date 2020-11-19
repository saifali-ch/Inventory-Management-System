package main;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import util.StageHandler;

public class StartController {
    
    public JFXButton loadMenuButton;
    
    public void initialize() {
    }
    
    @FXML
    void loadMenu(ActionEvent event) {
        String fxmlPath = "/views/Menu.fxml";
        Stage stage = StageHandler.createStage(fxmlPath);
        stage.show();
        ((Stage) (loadMenuButton.getScene().getWindow())).close();
    }
}
