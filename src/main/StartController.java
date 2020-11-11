package main;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class StartController {
    
    public JFXButton loadMenuButton;
    Stage stage;
    
    public void initialize() {
        Parent Layout = null;
        try {
            Layout = FXMLLoader.load(getClass().getResource("/views/Menu.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage = new Stage();
        assert Layout != null;
        stage.setScene(new Scene(Layout));
        //TODO remove -> loadMenuButton.fire();
//        loadMenuButton.fire();
    }
    
    @FXML
    void loadMenu(ActionEvent event) {
        stage.show();
        ((Stage) (loadMenuButton.getScene().getWindow())).close();
    }
}
