package pl.pwr.simulation;

/**
 * Klasa abstrakcyjna reprezentująca obiekt w symulacji.
 */
public abstract class SimulationObject {
    // Hermetyzacja danych - pola chronione (protected) zgodnie z diagramem
    protected int positionX;
    protected int positionY;

    public SimulationObject(int positionX, int positionY) {
        this.positionX = positionX;
        this.positionY = positionY;
    }

    /**
     * Metoda polimorficzna wykonywana w każdym kroku symulacji.
     */
    public abstract void performAction();

    // Gettery i settery realizujące hermetyzację danych
    public int getPositionX() { return positionX; }
    public void setPositionX(int positionX) { this.positionX = positionX; }

    public int getPositionY() { return positionY; }
    public void setPositionY(int positionY) { this.positionY = positionY; }
}