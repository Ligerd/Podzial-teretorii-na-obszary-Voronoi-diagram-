package Data;

public class DefinitionObject {
    private String type;
    private String[] definition;
    private int iteratorElements;

    public DefinitionObject(String type, String[] definition) {
        this.type = type;
        this.iteratorElements = 2;
        this.definition = definition;
    }

    public String getType() {
        return type;
    }

    public String getElementOfDefinition() {
        String result;
        if (iteratorElements < definition.length) {
            if (definition[iteratorElements].compareTo("X") == 0) {
                result = definition[iteratorElements];
                iteratorElements = iteratorElements + 2;
                return result;
            } else if (definition[iteratorElements].compareTo("Y") == 0) {
                result = definition[iteratorElements];
                iteratorElements = iteratorElements + 2;
                return result;
            } else {
                result = definition[iteratorElements];
                iteratorElements++;
                return result;
            }

        }
        return null;
    }

    public void backToStart() {
        iteratorElements = 2;
    }

    public String[] getDefinition() {
        return definition;
    }
}
