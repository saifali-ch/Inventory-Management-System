package alert;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import models.Product;

import java.io.IOException;

public class GMSAlert {
    private final AlertType alertType;
    private String fxmlPath;
    private Object object;
    private Stage stage;
    private Scene scene;
    private Runnable onCloseCode = null;
    
    public GMSAlert(AlertType alertType) {
        this.alertType = alertType;
    }
    
    public GMSAlert show() {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource(fxmlPath));
        } catch (IOException ioException) {
            System.out.println("Unable to load FXML for Alert = " + ioException.getMessage());
        }
        scene = new Scene(root);
        stage = new Stage();
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.setAlwaysOnTop(true);
        // TODO disable anchorPane when below stage is shown
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
        Button no = (Button) scene.lookup("#no");
        no.setOnMouseClicked(e -> {
            if (onCloseCode != null)
                onCloseCode.run();
            stage.close();
        });
        return this;
    }
    
    public void onYes(Runnable code) {
        if (alertType == AlertType.DELETE_PRODUCT) {
            Product product = (Product) object;
            
            Label id = (Label) scene.lookup("#id");
            Label name = (Label) scene.lookup("#name");
            Label category = (Label) scene.lookup("#category");
            
            id.setText(String.valueOf(product.getId()));
            name.setText(product.getName());
            category.setText(product.getCategory());
        } else if (alertType == AlertType.DELETE_CATEGORY) {
            int totalNoOfProducts = (int) object;
            Label totalProduct_lbl = (Label) scene.lookup("#totalProducts");
            totalProduct_lbl.setText(String.valueOf(totalNoOfProducts));
        } else if (alertType == AlertType.ADD_PRODUCT) {
            Label productName = (Label) scene.lookup("#productName");
            productName.setText((String) object);
        }
        
        Button yes = (Button) scene.lookup("#yes");
        yes.setOnMouseClicked(e -> {
            code.run();
            stage.close();
        });
    }
    
    public void onClose(Runnable code) {
        onCloseCode = code;
    }
    
    public void setFxmlPath(String fxmlPath) {
        this.fxmlPath = fxmlPath;
    }
    
    public void setObject(Object object) {
        this.object = object;
    }
}
