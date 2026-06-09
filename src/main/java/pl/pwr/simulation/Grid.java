package pl.pwr.simulation;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Klasa reprezentująca dwuwymiarową planszę symulacji.
 */
public class Grid {
    private final int width;
    private final int height;
    private final List<SimulationObject> objects;
    private final Random random = new Random();

    public Grid(int width, int height) {
        this.width = width;
        this.height = height;
        this.objects = new ArrayList<>();
    }

    /**
     * Losowo generuje śmieci na planszy.
     */
    public void generateDirt() {
        int dirtCount = random.nextInt(20) + 5; // Generuj od 9 do 14 śmieci
        for (int i = 0; i < dirtCount; i++) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            // Upewniamy się, że nie kładziemy śmieci na stacji dokującej (0,0)
            if (x == 0 && y == 0) { x = 1; y = 1; }
            objects.add(new Dirt(x, y, random.nextInt(3) + 1));
        }
    }

    /**
     * Zwraca obiekty znajdujące się na sąsiednich polach względem podanej pozycji.
     */
    public SimulationObject[] getAdjacentCells(int x, int y) {
        List<SimulationObject> adjacent = new ArrayList<>();
        for (SimulationObject obj : objects) {
            if (Math.abs(obj.getPositionX() - x) <= 1 && Math.abs(obj.getPositionY() - y) <= 1) {
                if (!(obj.getPositionX() == x && obj.getPositionY() == y)) {
                    adjacent.add(obj);
                }
            }
        }
        return adjacent.toArray(new SimulationObject[0]);
    }

    public void addObject(SimulationObject obj) {
        objects.add(obj);
    }

    public List<SimulationObject> getObjects() {
        return objects;
    }

    public int getWidth() { return width; }
    public int getHeight() { return height; }
}