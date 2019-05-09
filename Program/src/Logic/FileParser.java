package Logic;

import Data.*;
import javafx.scene.control.Alert;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class FileParser {
    private ArrayList<KeyPoint> keyPoints;

    private HashMap<String, DefinitionObject> definitions;
    private ArrayList<MapObject> objects;
    private Contours contours;
    private int currentLineNumber = 0;
    private int partOfFile = 0;
    private ErrorMessage errorMessage;

    public HashMap<String, DefinitionObject> getDefinitions() {
        return definitions;
    }


    public FileParser() {
        this.keyPoints = new ArrayList<>();
        contours = new Contours();
        definitions = new HashMap<>();
        objects = new ArrayList<>();
        errorMessage = new ErrorMessage();
    }

    public ArrayList<KeyPoint> getKeyPoints() {
        return keyPoints;
    }

    public Contours getContours() {
        return contours;
    }

    public boolean readFile(File file) throws IOException {
        BufferedReader bufferedReader;
        boolean result;
        String line;
        if (file == null) {
            errorMessage.Message("Plik nie może być nullem.", Alert.AlertType.ERROR);
            return false;
        }
        try {
            bufferedReader = new BufferedReader(new FileReader(file));
            result = true;
        } catch (FileNotFoundException e) {
            System.out.println(e);
            return false;
        }
        while ((line = bufferedReader.readLine()) != null && result) {
            currentLineNumber++;
            if (currentLineNumber == 1 && line.isEmpty()) {
                errorMessage.Message("Wystąpił błąd podczas wczytywania pliku.  Plik nie może zaczynać się z linni pustęj. Poprawny format pierwszej linii: # Kontury terenu (wymienione w kolejności łączenia): Lp. x y oraz definiowanie puntków.", Alert.AlertType.ERROR);
                return false;
            } else if (line.isEmpty()) {
                continue;
            } else {
                result = analyseLine(line);
            }

        }

        if (partOfFile == 0 && currentLineNumber == 0 && (bufferedReader.readLine()) == null) {
            errorMessage.Message("Wystąpił błąd podczas wczytywania pliku. Plik nie może być pusty", Alert.AlertType.ERROR);
            return false;
        } else if (partOfFile == 0 && currentLineNumber != 0 && result) {
            errorMessage.Message("Wystąpił błąd podczas wczytywania pliku. Dla dzialania programu muszą być zdefiniowane punkty kluczowe. Dla rozpoczęcia definiowania punktów kluczowych muszi być podana linija :# Punkty kluczowe: Lp. x y Nazwa", Alert.AlertType.ERROR);
            return false;
        } else if (contours.getSize() < 3) {
            errorMessage.Message("Wystąpił błąd podczs wczytywania pliku. Punktów definiujących kontur nie może być mniej niż 3", Alert.AlertType.ERROR);
            return false;
        }
        for (int i = 0; i < keyPoints.size(); i++) {
            if (!contours.checkPointInContur(keyPoints.get(i))) {
                errorMessage.Message("Punkt kluczowy nie znajduje się w obszarze nazwa punktu kluczowego: " + keyPoints.get(i).getName(), Alert.AlertType.ERROR);
                return false;
            }
        }
        if (checkKeyPointAndMapObject()) {
            errorMessage.Message("Punkt kluczowy oraz obiekt mają takie same współrzędne. To jest nie dopuszczalne.", Alert.AlertType.ERROR);
            return false;
        }
        return result;
    }

    private boolean analyseLine(String line) {
        boolean result = false;
        if (currentLineNumber == 1) {
            if (line.charAt(1) == '#' || line.charAt(0) == '#') {
                result = true;
            } else {
                errorMessage.Message("Nie poprawny format pierwszej linii pliku. Plik musi zaczynać się od definicji konturów." +
                        "Żeby rozpocząć definiowanie konturów trzeba napisać liniję: # Kontury terenu (wymienione w kolejności łączenia): Lp. x y", Alert.AlertType.ERROR);
                return false;
            }
        } else if (line.startsWith("#")) {
            partOfFile++;
            result = true;
        } else if (currentLineNumber > 0 && partOfFile == 0) {
            if (checkFormatContourLine(line)) {
                result = addElementToContour(line);
            }
        } else if (currentLineNumber > 0 && partOfFile == 1) {
            if (checkFormatKeyPointLine(line)) {
                result = addKeyPointToList(line);

            }
        } else if (currentLineNumber > 0 && partOfFile == 2) {
            result = addDefinitionObject(line);
        } else if (currentLineNumber > 0 && partOfFile == 3) {
            result = addMapObject(line);
        }
        return result;
    }


    private boolean checkFormatContourLine(String line) {
        line = line.replace(",", ".");
        String[] words = line.split("\\s+");
        double x;
        double y;
        if (words.length != 3) {
            errorMessage.Message("Wystąpił błąd podczas wczytywania punktów konturu. Nie poprawne formatowanie konturów w linii " + currentLineNumber + ". " +
                    "Format linii muszi być następujący: # Kontury terenu (wymienione w kolejności łączenia): Lp. x y", Alert.AlertType.ERROR);
            return false;
        }

        try {
            Double.parseDouble(words[0]);
            x = Double.parseDouble(words[1]);
            y = Double.parseDouble(words[2]);
        } catch (NumberFormatException e) {
            System.out.println(e);
            errorMessage.Message("Wystąpił błąd podczas wczytywania punktów konturu. Numer punktu oraz współrzędne punktów definujących kontur mogą być zadane tylko liczbami. Błąd wystąpił w linii: " + currentLineNumber + "" +
                    "Poprawny format jes następujący: # Kontury terenu (wymienione w kolejności łączenia): Lp. x y", Alert.AlertType.ERROR);
            return false;
        }
        if (x < 0 || y < 0) {
            errorMessage.Message("Wystąpił bład podczas wczytywania punktów definiujących contur. Współrzędne nie mogą być ujemne. Błąd wystąpił w linijce: " + currentLineNumber, Alert.AlertType.ERROR);
            return false;
        }
        return true;
    }

    private boolean checkFormatKeyPointLine(String line) {
        line = line.replace(",", ".");
        String[] words = line.split("\\s+");
        double x;
        double y;
        if (line == null) {
            errorMessage.Message("Wystąpił bład w podczs wczytywania punktów kluczowych. Współrzędne definiujące punkty kluczowy nie zostały podane", Alert.AlertType.ERROR);
        }
        if (words.length < 4) {
            System.out.println("Wystąpił błąd podczas wczytywania punktów kluczowych. Nie poprawny formatowanie pliku w linijce: " + currentLineNumber);
            errorMessage.Message("Wystąpił błąd podczas wczytywania puktów kluczowych. Nie poprawny format w linii " + currentLineNumber + "" +
                    "Poprawny format jest następujący: # Punkty kluczowe: Lp. x y Nazwa", Alert.AlertType.ERROR
            );
            return false;
        }
        try {
            Double.parseDouble(words[0]);
            x = Double.parseDouble(words[1]);
            y = Double.parseDouble(words[2]);
        } catch (NumberFormatException e) {
            System.out.println(e);
            errorMessage.Message("Wystąpił błąd podczas wczytywania punktów kluczowych.  Numer puntku oraz współrzędne definujące punkt kluczowy mogą być zadane tylko liczbami. Błąd wystąpił w linii: " + currentLineNumber + "" +
                    "Poprawny format jest następujący: # Punkty kluczowe: Lp. x y Nazwa", Alert.AlertType.ERROR);
            return false;
        }
        if (x < 0 || y < 0) {
            errorMessage.Message("Wystąpił bład podczas wczytywania punktów kluczowych. Współrzędne nie mogą być ujemne. Błąd wystąpił w linijce: " + currentLineNumber, Alert.AlertType.ERROR);
            return false;
        }
        return true;
    }

    private boolean addElementToContour(String line) {
        String words[] = line.split("\\s+");
        double x = Double.parseDouble(words[1]);
        double y = Double.parseDouble(words[2]);
        Point point = new Point(x, y);
        contours.addPointToContour(point);
        return true;
    }

    private boolean addKeyPointToList(String line) {
        double x;
        double y;
        String name = "";
        StringBuilder stringBuilder = new StringBuilder(name);
        String[] words = line.split("\\s+");
        x = Double.parseDouble(words[1]);
        y = Double.parseDouble(words[2]);
        for (int i = 3; i < words.length; i++) {
            name = name + " " + words[i];
        }
        KeyPoint keyPoint = new KeyPoint(x, y, name);
        keyPoints.add(keyPoint);
        return true;
    }


    private boolean addDefinitionObject(String line) {
        line = line.replace(",", ".");
        String[] words = line.split("\\s+");
        DefinitionObject definitionObject = new DefinitionObject(words[1], words);
        definitions.put(words[1], definitionObject);
        return true;
    }

    private boolean addMapObject(String line) {
        line = line.replace(",", ".");
        String[] words = line.split("\\s+");
        double x;
        double y;
        MapObject mapObject = new MapObject();
        String string = "";
        int valueInt;
        double valueDouble;
        String nameOfParametr;
        String elementOfDefinition;
        DefinitionObject definitionObject = definitions.get(words[1]);
        if (definitionObject != null) {
            for (int i = 2; i < words.length; i++) {
                elementOfDefinition = definitionObject.getElementOfDefinition();

                if (elementOfDefinition.compareTo("X") == 0) {
                    x = Double.parseDouble(words[i]);
                    mapObject.setX(x);
                } else if (elementOfDefinition.compareTo("Y") == 0) {

                    y = Double.parseDouble(words[i]);
                    mapObject.setY(y);
                } else {
                    nameOfParametr = elementOfDefinition;
                    elementOfDefinition = definitionObject.getElementOfDefinition();
                    if (elementOfDefinition.compareTo("double") == 0) {
                        valueDouble = Double.parseDouble(words[i]);
                        mapObject.addDoubleParametr(nameOfParametr, valueDouble);
                    } else if (elementOfDefinition.compareTo("String") == 0) {

                        for (int index = i; index < words.length; index++) {
                            string = string + " " + words[index];
                            if (words[index].endsWith("\"")) {
                                index = index++;
                                i = index;
                                break;
                            }

                        }
                        mapObject.addStringParameter(nameOfParametr, string);

                    } else if (elementOfDefinition.compareTo("int") == 0) {
                        valueInt = Integer.parseInt(words[i]);
                        mapObject.addIntParametr(nameOfParametr, valueInt);
                    } else {
                        errorMessage.Message("Wystąpił bład podczas wczytywania obiektów. Proszę o sprawdzeniu poprawności napisania: String , int ,double, X, Y", Alert.AlertType.ERROR);
                        return false;
                    }
                }
            }
            if (contours.checkPointInContur(new Point((int) mapObject.getX(), (int) mapObject.getY()))) {
                mapObject.setType(words[1]);
                objects.add(mapObject);
            }
                definitionObject.backToStart();

        }
        return true;
    }

    public ArrayList getObjects() {
        return objects;
    }

    public boolean checkKeyPointAndMapObject() {

        for (int i = 0; i < keyPoints.size(); i++) {
            for (int index = 0; index < objects.size(); index++)
                if (Double.compare(keyPoints.get(i).getX(), objects.get(index).getX()) == 0) {
                    if (Double.compare(keyPoints.get(i).getY(), objects.get(index).getY()) == 0) {
                        return true;
                    }
                }
        }

        return false;
    }


}
