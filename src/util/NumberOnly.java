package util;

import javafx.scene.control.TextField;

public enum NumberOnly {
    ;
    
    public static void makeFieldsNumberOnly(TextField... textFields) {
        for (var textField : textFields) {
            textField.textProperty().addListener((o, oldVale, newValue) -> {
                if (newValue.matches("\\d*")) return;
                textField.setText(newValue.replaceAll("\\D", ""));
            });
        }
    }
}
