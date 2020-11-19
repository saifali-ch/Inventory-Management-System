package main;

import database.DBConnection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.sql.SQLException;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage stage) throws Exception {
        DBConnection.createConnection();
        Parent root = FXMLLoader.load(getClass().getResource("Start.fxml"));
//        JFXDecorator decorator = new JFXDecorator(stage, root);
//        decorator.setCustomMaximize(true);
        Scene scene = new Scene(root);
        stage.setTitle("Faizi Traders");
        stage.setScene(scene);
        stage.getIcons().add(new Image(getClass().getResource("/media/icon.png").toExternalForm()));
        stage.show();
        stage.setOnCloseRequest(e -> {
            try {
                DBConnection.getConnection().commit();
                DBConnection.getConnection().close();
                stage.close();
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }
        });
    }
}