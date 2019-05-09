package Logic;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.io.IOException;

public class PopupWindowController {

    @FXML
    private TextField xTextField;

    @FXML
    private TextField yTextField;

    @FXML
    private TextField nameTextField;

    private GUIController guiController;

    private ErrorMessage errorMessage = new ErrorMessage();

    public void setGuiController(GUIController guiController) {
        this.guiController = guiController;
    }


    @FXML
    public void makeKeyPoint() throws IOException {

        double x = 0;
        double y = 0;
        String name = nameTextField.getText();
        boolean canAdd = true;
        try {
            x = Double.valueOf(xTextField.getText());
            y = Double.valueOf(yTextField.getText());

        } catch (NumberFormatException e) {
            canAdd = false;
            errorMessage.Message("Współrzędne punktu kluczowego muszą mieć postać liczbową.", Alert.AlertType.ERROR);
        }


        if ( x < 0 || y < 0 )
        {
            canAdd = false;
            errorMessage.Message("Podane wartości nie mogą być ujemne.", Alert.AlertType.ERROR);
        }

        if (canAdd) {
            guiController.addKeyPoint(x, y, name);
        }

    }

    @FXML
    public void makeContourPoint() throws IOException {
        double x = 0;
        double y = 0;
        int index = 0;
        boolean canAdd = true;
        try {
            x = Double.valueOf(xTextField.getText());
            y = Double.valueOf(yTextField.getText());

        } catch (NumberFormatException e) {
            canAdd = false;
            errorMessage.Message("Współrzędne punktu kluczowego muszą mieć postać liczbową.", Alert.AlertType.ERROR);
        }

        try {
            index = Integer.valueOf(nameTextField.getText());

        } catch (NumberFormatException e) {
            canAdd = false;
            errorMessage.Message("Numer porządkowy pliku musi być określony przez wartość całkowitą.", Alert.AlertType.ERROR);
        }

        if ( x < 0 || y < 0 || index <= 0)
        {
            canAdd = false;
            errorMessage.Message("Podane wartości nie mogą być ujemne.", Alert.AlertType.ERROR);
        }

        if (canAdd) {
            guiController.addContourPoint(x, y, index);
        }


    }


}
