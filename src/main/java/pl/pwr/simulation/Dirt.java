package pl.pwr.simulation;

/**
 * Klasa reprezentująca śmieć (zasób) na planszy.
 */
public class Dirt extends SimulationObject {
    private final int size;

    public Dirt(int positionX, int positionY, int size) {
        super(positionX, positionY);
        this.size = size;
    }

    @Override
    public void performAction() {
        // Śmieci same z siebie nie wykonują akcji
    }

    public int getSize() { return size; }
}