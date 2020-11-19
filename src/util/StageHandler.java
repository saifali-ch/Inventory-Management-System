package util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public enum StageHandler {
    ;
    
    public static Stage createAlertStage(String FXMLPath) {
        Stage stage = createStage(FXMLPath);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.initModality(Modality.APPLICATION_MODAL);
        return stage;
    }
    
    public static Stage createStage(String path) {
        Parent pane = null;
        Image icon = null;
        try {
            pane = FXMLLoader.load(StageHandler.class.getResource(path));
            icon = new Image(StageHandler.class.getResource("/media/icon.png").toExternalForm());
        } catch (IOException e) {
            System.out.println("Unable to load FXML/Icon -> Message = " + e.getMessage());
        }
        assert pane != null;
        Scene scene = new Scene(pane);
        Stage stage = new Stage();
        stage.getIcons().add(icon);
        stage.setScene(scene);
        return stage;
    }
}
