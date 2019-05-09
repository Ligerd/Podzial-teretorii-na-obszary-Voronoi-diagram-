package Logic;

import javafx.scene.control.Alert;
import javafx.scene.layout.Region;

public class ErrorMessage {

    public void Message(String errorMessage, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.setTitle("Błąd!");
        alert.setHeaderText("Whoops! Coś poszło niezgodnie z planem!");
        alert.setContentText(errorMessage);

        alert.showAndWait();
    }
}
