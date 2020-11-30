package util;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.HashMap;


public class PaneHandler {
    private static final HashMap<String, AnchorPane> cachedPanes = new HashMap<>();
    
    public static AnchorPane loadCachedPane(String fxmlPath) {
        if (!cachedPanes.containsKey(fxmlPath))
            cachedPanes.put(fxmlPath, loadPane(fxmlPath));
        return cachedPanes.get(fxmlPath);
    }
    
    public static AnchorPane loadPane(String fxmlPath) {
        AnchorPane pane = null;
        try {
            pane = FXMLLoader.load(PaneHandler.class.getResource(fxmlPath));
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return pane;
    }
}

