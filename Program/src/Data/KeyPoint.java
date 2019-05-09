package Data;

import javafx.scene.paint.Color;

import java.util.Random;

public class KeyPoint extends Point {

    private String name;
    private Color color;
    private Region region;
    public KeyPoint(double x, double y, String name) {
        super(x, y);
        this.name = name;
        Random random = new Random();
        color = new Color(random.nextDouble(), random.nextDouble(), random.nextDouble(), 0.7);
        this.region= new Region(name);
    }

    public Region getRegion() {
        return region;
    }

    public String getName() {
        return name;
    }

    public Color getColor() {
        return color;
    }
}
