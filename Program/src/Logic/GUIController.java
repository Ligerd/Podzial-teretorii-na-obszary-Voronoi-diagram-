package Logic;

import Data.*;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Set;

public class GUIController {

    @FXML
    private Pane mainPane;

    @FXML
    private VBox contourPointsBox;

    @FXML
    private VBox keyPointsBox;

    @FXML
    private VBox regionObjectsBox;

    @FXML
    private VBox setObjectTypeBox;


    @FXML
    private VBox typeObjectsBox;

    private double scale = 1;
    private ArrayList<CheckBox> keyPointsCheckBoxes;
    private ArrayList<CheckBox> contoursCheckBoxes;
    private ArrayList<CheckBox> objectTypeCheckBoxes;
    private RegionSetter regionSetter;
    private Contours contours;
    private Group groupOfPoints;
    private Group groupOfContours;
    private Rectangle[][] graphicalPoints;
    private ErrorMessage errorMessage;
    private Stage dialog;
    private FileParser fileParser;
    private ImageView iv1;


    public void initialize() throws IOException {
        errorMessage = new ErrorMessage();
    }

    private void show() throws IOException {
        mapClear();
        drawMap();
        drawContours(contours);
        showContoursList(contours);
        showKeyPointsList();
    }

    private void drawMap() {

        if (groupOfPoints == null) {
            groupOfPoints = new Group();
            mainPane.getChildren().add(groupOfPoints);
        }

        Point[][] points = regionSetter.getPoints();

        for (int i = 0; i < points.length; ++i)
            for (int j = 0; j < points[0].length; ++j) {
                if (points[i][j].getKeyPoint() != null) {
                    if (graphicalPoints[i][j] == null) {
                        graphicalPoints[i][j] = new Rectangle();
                        groupOfPoints.getChildren().add(graphicalPoints[i][j]);
                    }

                    graphicalPoints[i][j].setHeight(scale);
                    graphicalPoints[i][j].setWidth(scale);
                    graphicalPoints[i][j].setX(i * scale);
                    graphicalPoints[i][j].setY(j * scale);
                    graphicalPoints[i][j].setFill(points[i][j].getKeyPoint().getColor());

                    EventHandler showRegion = new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            showRegionObjects(points[(int) event.getX()][(int) event.getY()].getKeyPoint());
                        }
                    };

                    graphicalPoints[i][j].addEventHandler(MouseEvent.MOUSE_PRESSED, showRegion);

                }
            }

        ArrayList<MapObject> objects = fileParser.getObjects();
        for (MapObject object : objects) {
            if (contours.checkPointInContur(points[(int) object.getX()][(int) object.getY()])) {
                graphicalPoints[(int) object.getX()][(int) object.getY()].setFill(Color.BLACK);
                graphicalPoints[(int) object.getX()][(int) object.getY()].setHeight(scale * 2);
                graphicalPoints[(int) object.getX()][(int) object.getY()].setWidth(scale * 2);
            }
        }


    }

    private void mapClear() {
        mainPane.getChildren().remove(groupOfPoints);
        graphicalPoints = new Rectangle[regionSetter.getHeight()][regionSetter.getWidth()];
        groupOfPoints = null;
    }


    private void drawContours(Contours contours) {

        if (groupOfContours != null) {
            mainPane.getChildren().remove(groupOfContours);
        }

        groupOfContours = new Group();

        Line[] lineContours = new Line[contours.getSize()];
        int i;
        for (i = 0; i < contours.getSize() - 1; ++i) {
            lineContours[i] = new Line();
            lineContours[i].setStartX(scale * contours.getPointFromContour(i).getX());
            lineContours[i].setStartY(scale * contours.getPointFromContour(i).getY());
            lineContours[i].setEndX(scale * contours.getPointFromContour(i + 1).getX());
            lineContours[i].setEndY(scale * contours.getPointFromContour(i + 1).getY());
            lineContours[i].setStrokeWidth(2);
            lineContours[i].setStroke(Color.BLUE);
            groupOfContours.getChildren().add(lineContours[i]);
        }

        lineContours[i] = new Line();
        lineContours[i].setStartX(scale * contours.getPointFromContour(i).getX());
        lineContours[i].setStartY(scale * contours.getPointFromContour(i).getY());
        lineContours[i].setEndX(scale * contours.getPointFromContour(0).getX());
        lineContours[i].setEndY(scale * contours.getPointFromContour(0).getY());
        lineContours[i].setStrokeWidth(2);
        lineContours[i].setStroke(Color.BLUE);
        groupOfContours.getChildren().add(lineContours[i]);

        mainPane.getChildren().add(groupOfContours);

    }

    private void showContoursList(Contours contours) {
        contoursCheckBoxes = new ArrayList<>();
        for (int i = 0; i < contours.getSize(); ++i) {
            contoursCheckBoxes.add(new CheckBox());
            contoursCheckBoxes.get(i).setMinHeight(20);
            contoursCheckBoxes.get(i).setText(i + 1 + ") " + "X = " + contours.getPointFromContour(i).getX() + "  Y = " + contours.getPointFromContour(i).getY());
            contourPointsBox.getChildren().add(contoursCheckBoxes.get(i));

        }

    }

    public void removeContourPoint() {
        if (contours.getSize() == 3) {
            errorMessage.Message("Punktów konturu nie może być mniej niż 3", Alert.AlertType.ERROR);
            return;
        }
        for (int i = 0; i < contours.getSize(); ++i) {
            if (contoursCheckBoxes.get(i).isSelected()) {
                Point point = contours.removeContour(i);

                if (!contours.contourСheck()) {
                    KeyPoint keyPoint = contours.checkKeyPoints(regionSetter.getKeyPoints());
                    if (keyPoint == null) {
                        contourPointsBox.getChildren().remove(contoursCheckBoxes.get(i));
                        contoursCheckBoxes.remove(i);
                        contours.update();
                        regionSetter.update(contours.getMapHeight(), contours.getMapWidth());
                        regionSetter.assignPoints(contours);
                        regionSetter.assignObjectToRegion(fileParser.getObjects());
                        mapClear();
                        drawMap();
                        drawContours(contours);
                        i--;
                    } else {
                        contours.addPointToContourAtPlace(point, i);
                        errorMessage.Message("Wystąpił błąd usuwania punktu konturu! Usunięcie tego punktu spowoduje, że punkt kluczowy \'" + keyPoint.getName() + "\' znajdzie się poza konturem.", Alert.AlertType.ERROR);
                    }

                } else {
                    contours.addPointToContourAtPlace(point, i);
                    errorMessage.Message("Wystąpił błąd usuwania punktu konturu! Usunięcie tego punktu spowoduje przecinanie się konturów.", Alert.AlertType.ERROR);
                }

            }
        }
    }


    public void removeKeyPointFromList() {
        for (int i = 0; i < keyPointsCheckBoxes.size(); ++i) {

            if (keyPointsCheckBoxes.get(i).isSelected()) {
                keyPointsBox.getChildren().remove(keyPointsCheckBoxes.get(i));
                keyPointsCheckBoxes.remove(i);
                regionSetter.removeKeyPoint(i);

                i--;
            }
        }

        regionSetter.assignPoints(contours);
        regionSetter.assignObjectToRegion(fileParser.getObjects());
        drawMap();

    }


    protected void showKeyPointsList() {
        ArrayList<KeyPoint> keyPoints = regionSetter.getKeyPoints();
        keyPointsCheckBoxes = new ArrayList<>();
        for (int i = 0; i < keyPoints.size(); ++i) {
            keyPointsCheckBoxes.add(new CheckBox());
            keyPointsCheckBoxes.get(i).setMinHeight(20);
            keyPointsCheckBoxes.get(i).setText(keyPoints.get(i).getName() + "(" + keyPoints.get(i).getX() + ", " + keyPoints.get(i).getY() + ")");
            keyPointsBox.getChildren().add(keyPointsCheckBoxes.get(i));

        }
    }


    @FXML
    public void readFile() throws IOException {
        fileParser = new FileParser();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open File Dialog");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("TXT Files", "*.txt", "*.TXT", "*.Txt"));
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            if (fileParser.readFile(file)) {
                contourPointsBox.getChildren().clear();
                keyPointsBox.getChildren().clear();
                contours = fileParser.getContours();
                regionSetter = new RegionSetter(fileParser.getKeyPoints(), contours.getMapHeight(), contours.getMapWidth());
                regionSetter.assignPoints(contours);
                regionSetter.assignObjectToRegion(fileParser.getObjects());
                if (contours.contourСheck()) {
                    errorMessage.Message("Wystąpił bład. Według podanych punktów definiujących kontur występuje samoprzecięcie konturu. " +
                            "Proszę o sprawdzeniue punktów definujących kontur", Alert.AlertType.ERROR);
                    return;
                }
                objectTypeCheckBoxes = new ArrayList<>();
                show();
                showTypesOfObjects();

            }
        }
    }

    @FXML
    public void readFileImage() throws IOException {
        if (iv1 != null) {
            mainPane.getChildren().remove(iv1);
        }
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open File Dialog");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("png , jpg Files", "*.png", "*.jpg"));
        File file = fileChooser.showOpenDialog(null);
        if (file == null) {
            return;
        }
        URL url = file.toURI().toURL();
        Image img = new Image(String.valueOf(url));
        iv1 = new ImageView();
        iv1.setImage(img);
        iv1.setX(0);
        iv1.setY(0);
        mainPane.getChildren().add(iv1);
    }

    public void createKeyPointWindow() throws IOException {
        if (contours != null) {
            dialog = new Stage();
            FXMLLoader popupFXML = new FXMLLoader();
            popupFXML.setLocation(this.getClass().getResource("AddKeyPointWindow.fxml"));
            Parent root = popupFXML.load();
            dialog.initModality(Modality.APPLICATION_MODAL);
            Scene dialogScene = new Scene(root);
            dialog.setScene(dialogScene);
            dialog.show();
            PopupWindowController popupWindowController = popupFXML.getController();
            popupWindowController.setGuiController(this);

        } else {
            errorMessage.Message("Nie można dodać punktu kluczowego, bez wcześniejszego wczytania pliku.", Alert.AlertType.ERROR);
        }


    }

    public void addKeyPoint(double x, double y, String name) {

        if (regionSetter.addKeyPointAndCheckIfIsInContour(x, y, name, contours)) {
            regionSetter.assignPoints(contours);
            regionSetter.assignObjectToRegion(fileParser.getObjects());
            ArrayList<KeyPoint> keyPoints = regionSetter.getKeyPoints();
            keyPointsCheckBoxes.add(new CheckBox());
            keyPointsCheckBoxes.get(keyPointsCheckBoxes.size() - 1).setMinHeight(20);
            keyPointsCheckBoxes.get(keyPointsCheckBoxes.size() - 1).setText(keyPoints.get(keyPointsCheckBoxes.size() - 1).getName() + "(" + keyPoints.get(keyPointsCheckBoxes.size() - 1).getX() + ", " + keyPoints.get(keyPointsCheckBoxes.size() - 1).getY() + ")");
            keyPointsBox.getChildren().add(keyPointsCheckBoxes.get(keyPointsCheckBoxes.size() - 1));
            drawMap();
            dialog.close();
        } else {
            errorMessage.Message("Dodany punkt leży poza konturem!", Alert.AlertType.ERROR);
        }
    }

    public void addContourPoint(double x, double y, int index) {

        index--;
        if (index > contours.getSize()) {
            index = contours.getSize();
        }
        contours.addPointToContourAtPlace(new Point(x, y), index);
        if (!contours.contourСheck()) {
            contoursCheckBoxes.add(index, new CheckBox());
            contoursCheckBoxes.get(index).setMinHeight(20);
            contoursCheckBoxes.get(index).setText(index + 1 + ") " + "X = " + contours.getPointFromContour(index).getX() + "  Y = " + contours.getPointFromContour(index).getY());
            contourPointsBox.getChildren().add(index, contoursCheckBoxes.get(index));
            drawContours(contours);
            contours.update();
            regionSetter.update(contours.getMapHeight(), contours.getMapWidth());
            regionSetter.assignPoints(contours);
            regionSetter.assignObjectToRegion(fileParser.getObjects());
            mapClear();
            drawMap();
            dialog.close();
        } else {
            contours.removeContour(index);
            errorMessage.Message("Dodawany punkt konturu spowoduje przecinanie się linii konturu.", Alert.AlertType.ERROR);
        }


    }

    public void createContourWindow() throws IOException {
        dialog = new Stage();
        FXMLLoader popupFXML = new FXMLLoader();
        popupFXML.setLocation(this.getClass().getResource("AddContourPointWindow.fxml"));
        Parent root = popupFXML.load();
        dialog.initModality(Modality.APPLICATION_MODAL);
        Scene dialogScene = new Scene(root);
        dialog.setScene(dialogScene);
        dialog.show();
        PopupWindowController popupWindowController = popupFXML.getController();
        popupWindowController.setGuiController(this);


    }

    public void showRegionObjects(KeyPoint keyPoint) {

        regionObjectsBox.getChildren().remove(0, regionObjectsBox.getChildren().size());
        Region region = keyPoint.getRegion();
        for (int i = 0; i < region.getMapObjectListSize(); i++) {

            Text text = new Text(region.getMapObjectInString(i));
            text.setLineSpacing(10);
            regionObjectsBox.getChildren().add(text);
        }

    }

    public void showTypesOfObjects() {
        setObjectTypeBox.getChildren().clear();
        Set<String> typeOfObject = fileParser.getDefinitions().keySet();
        for (String type : typeOfObject) {
            CheckBox objectTypeCheckBox = new CheckBox();
            objectTypeCheckBoxes.add(objectTypeCheckBox);
            objectTypeCheckBox.setMinHeight(20);
            objectTypeCheckBox.setText(type);

            EventHandler showObjectsOfTypeHandler = new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    CheckBox eventCheckBox = (CheckBox) event.getSource();
                    showObjectsOfType(eventCheckBox.getText());
                    for (CheckBox checkBox : objectTypeCheckBoxes) {
                        if (checkBox != eventCheckBox) {
                            checkBox.setSelected(false);
                        }
                    }

                }
            };
            objectTypeCheckBox.addEventHandler(MouseEvent.MOUSE_PRESSED, showObjectsOfTypeHandler);

            setObjectTypeBox.getChildren().add(objectTypeCheckBox);
        }
    }


    public void showObjectsOfType(String type) {


        typeObjectsBox.getChildren().clear();
        ArrayList<MapObject> objects = fileParser.getObjects();

        for (MapObject object : objects) {
            if (object.getType().compareTo(type) == 0) {
                Text text = new Text(object.getMapObjectInString());
                text.setLineSpacing(10);
                typeObjectsBox.getChildren().add(text);
            }

        }

    }


}
