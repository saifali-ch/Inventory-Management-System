package util;

import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public enum StageHandler {
    ;
    
    public static Stage createAlertStage(Parent pane) {
        Stage stage = createStage(pane);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.initModality(Modality.APPLICATION_MODAL);
        return stage;
    }
    
    public static Stage createStage(Parent pane) {
        Image icon = new Image(StageHandler.class.getResource("/media/icon.png").toExternalForm());
        Scene scene = new Scene(pane);
        Stage stage = new Stage();
        stage.getIcons().add(icon);
        stage.setScene(scene);
        return stage;
    }
    
    public static void createAndShowStage(Parent pane) {
        createStage(pane).show();
    }
    
    public static void closeStage(Event event) {
        getStage(event).close();
    }
    
    public static Stage getStage(Event event) {
        return (Stage) ((Node) event.getSource()).getScene().getWindow();
    }
}
