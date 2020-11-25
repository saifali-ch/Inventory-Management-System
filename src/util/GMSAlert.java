package util;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import models.Product;

public class GMSAlert {
    private final AlertType alertType;
    private String fxmlPath;
    private Object object;
    private Stage stage;
    private Scene scene;
    private Runnable onCancelCode = null;
    
    public GMSAlert(AlertType alertType) {
        this.alertType = alertType;
    }
    
    public GMSAlert(AlertType alertType, Object object) {
        this.alertType = alertType;
        this.object = object;
    }
    
    public void show() {
        stage = StageHandler.createAlertStage(fxmlPath);
        scene = stage.getScene();
        stage.show();
        setStageContent();
        Button no = (Button) scene.lookup("#no");
        no.setOnMouseClicked(e -> {
            if (onCancelCode != null)
                onCancelCode.run();
            stage.close();
        });
    }
    
    private void setStageContent() {
        switch (alertType) {
            case DELETE_PRODUCT:
                Label id = (Label) scene.lookup("#id");
                Label name = (Label) scene.lookup("#name");
                Label category = (Label) scene.lookup("#category");
                
                Product product = (Product) object;
                id.setText(String.valueOf(product.getId()));
                name.setText(product.getName());
                category.setText(product.getCategory());
                break;
            case DELETE_CATEGORY:
                Label totalProduct_lbl = (Label) scene.lookup("#totalProducts");
                totalProduct_lbl.setText(String.valueOf((int) object));
                break;
            case ADD_PRODUCT:
                Label productName = (Label) scene.lookup("#productName");
                productName.setText((String) object);
                break;
            default:
                System.out.println("Not a valid alert type is selected");
                throw new IllegalArgumentException();
        }
    }
    
    public void onYes(Runnable code) {
        Button yes = (Button) scene.lookup("#yes");
        yes.setOnMouseClicked(e -> {
            code.run();
            stage.close();
        });
    }
    
    public void onCancelRun(Runnable code) {
        onCancelCode = code;
    }
    
    public void setFxmlPath(String fxmlPath) {
        this.fxmlPath = fxmlPath;
    }
    
    public void setObject(Object object) {
        this.object = object;
    }
    
    public enum AlertType {
        DELETE_PRODUCT,
        DELETE_CATEGORY,
        ADD_PRODUCT,
        NONE,
    }
}
