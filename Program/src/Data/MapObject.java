package Data;

import java.util.HashMap;

public class MapObject {

    private String type;
    private double x;
    private double y;


    private HashMap<String, String> stringParametersMap;
    private HashMap<String, Double> doubleParametersMap;
    private HashMap<String, Integer> intParametersMap;


    public void writeToCheck() {
        System.out.println(type + " " + " x " + x + " y " + y);
    }


    public void addStringParameter(String name, String parametr) {
        stringParametersMap.put(name, parametr);
    }

    public void addDoubleParametr(String name, double parametr) {
        doubleParametersMap.put(name, parametr);
    }

    public void addIntParametr(String name, int parametr) {
        intParametersMap.put(name, parametr);
    }


    public String getMapObjectInString() {
        StringBuilder mapObjectInString = new StringBuilder();

        mapObjectInString.append( "Typ obiektu: ").append(this.getType());
        mapObjectInString.append("\n X = ").append(this.getX()).append(" Y = ").append(this.getY()).append("\n");
        mapObjectInString.append(this.getStringWithDoubleParameters());
        mapObjectInString.append(this.getStringWithIntParameters());
        mapObjectInString.append(this.getStringWithStringParameters());

        mapObjectInString.append("\n");
        return mapObjectInString.toString();
    }


    public MapObject() {
        this.stringParametersMap = new HashMap<>();
        this.intParametersMap = new HashMap<>();
        this.doubleParametersMap = new HashMap<>();
    }

    public double getY() {
        return y;
    }

    public double getX() {
        return x;
    }

    public String getType() {
        return type;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStringWithDoubleParameters()
    {
        StringBuilder stringWithDoubleParameters = new StringBuilder();
        for (String key : doubleParametersMap.keySet())
        {
            stringWithDoubleParameters.append(key).append(": ").append(doubleParametersMap.get(key));
        }
        return stringWithDoubleParameters.toString();
    }

    public String getStringWithIntParameters()
    {
        StringBuilder stringWithIntParameters = new StringBuilder();
        for (String key : intParametersMap.keySet())
        {
            stringWithIntParameters.append(key).append(": ").append(intParametersMap.get(key)).append("\n");
        }
        return stringWithIntParameters.toString();
    }

    public String getStringWithStringParameters()
    {
        StringBuilder stringWithIntParameters = new StringBuilder();
        for (String key : stringParametersMap.keySet())
        {
            stringWithIntParameters.append(key).append(": ").append(stringParametersMap.get(key)).append("\n");
        }
        return stringWithIntParameters.toString();
    }

    public HashMap<String, Double> getDoubleParametersMap() {
        return doubleParametersMap;
    }

    public HashMap<String, Integer> getIntParametersMap() {
        return intParametersMap;
    }

    public HashMap<String, String> getStringParametersMap() {
        return stringParametersMap;
    }
}
