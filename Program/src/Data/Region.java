package Data;

import java.util.ArrayList;

public class Region {

    private String name;
    private ArrayList<MapObject> listOfMapObjects;

    public Region(String name) {
        this.name = name;
        listOfMapObjects = new ArrayList<>();
    }

    public void addObject(MapObject object) {
        if(!listOfMapObjects.contains(object))
        {
            listOfMapObjects.add(object);
        }

     }

    public ArrayList<MapObject> getMapObjectList() {
        return listOfMapObjects;
    }

    public String getMapObjectInString(int index) {

        MapObject mapObject = listOfMapObjects.get(index);
        return mapObject.getMapObjectInString();
    }

    public int getMapObjectListSize()
    {
        return listOfMapObjects.size();
    }

    public MapObject getMapObject(int index)
    {
        return listOfMapObjects.get(index);
    }

   public void clearMapObjectList()
   {
       listOfMapObjects = new ArrayList<>();
   }


    public String getName() {
        return name;
    }
}
