package main;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import util.PaneHandler;
import util.Panes;
import util.StageHandler;

public class StartController {
    
    public JFXButton loadMenuButton;
    
    public void initialize() {
    }
    
    @FXML
    void loadMenu(ActionEvent event) {
        StageHandler.createAndShowStage(PaneHandler.loadCachedPane(Panes.MENU));
        StageHandler.closeStage(event);
    }
}
