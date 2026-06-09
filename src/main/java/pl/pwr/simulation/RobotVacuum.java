package pl.pwr.simulation;

import java.util.Random;

/**
 * Klasa reprezentująca agenta - inteligentny odkurzacz.
 */
public class RobotVacuum extends SimulationObject {
    private final int speed;
    private final int maxCapacity;
    private int currentTankLevel;
    private int collectedDirtTotal;
    private String currentState; // Np. "SEARCHING", "RETURNING_TO_DOCK", "IDLE"
    private final Grid grid;
    private final Random random = new Random();

    public RobotVacuum(int x, int y, int speed, int maxCapacity, Grid grid) {
        super(x, y);
        this.speed = speed;
        this.maxCapacity = maxCapacity;
        this.currentTankLevel = 0;
        this.collectedDirtTotal = 0;
        this.currentState = "SEARCHING";
        this.grid = grid;
    }

    @Override
    public void performAction() {
        if ("RETURNING_TO_DOCK".equals(currentState)) {
            returnToDock();
        } else if ("SEARCHING".equals(currentState)) {
            searchForDirt();
        }
    }

    /**
     * Logika przeszukiwania planszy i zbierania śmieci.
     */
    public void searchForDirt() {
        // Ruch o zadaną prędkość (w uproszczeniu: losowy krok w granicach planszy)
        for (int step = 0; step < speed; step++) {
            int newX = Math.max(0, Math.min(grid.getWidth() - 1, positionX + random.nextInt(3) - 1));
            int newY = Math.max(0, Math.min(grid.getHeight() - 1, positionY + random.nextInt(3) - 1));
            this.positionX = newX;
            this.positionY = newY;

            // Sprawdzenie interakcji z obiektami na tej samej pozycji
            SimulationObject toRemove = null;
            for (SimulationObject obj : grid.getObjects()) {
                if (obj.getPositionX() == this.positionX && obj.getPositionY() == this.positionY) {
                    if (obj instanceof Dirt dirt) {
                        // Zbieranie śmieci
                        this.currentTankLevel += dirt.getSize();
                        this.collectedDirtTotal += dirt.getSize();
                        toRemove = dirt;
                        System.out.println("Odkurzacz na [" + positionX + "," + positionY + "] zebrał śmieć o rozmiarze: " + dirt.getSize());

                        if (this.currentTankLevel >= maxCapacity) {
                            this.currentState = "RETURNING_TO_DOCK"; // Zmiana stanu
                        }
                        break;
                    } else if (obj instanceof DockingStation dock && "RETURNING_TO_DOCK".equals(currentState)) {
                        dock.emptyVacuum(this);
                    }
                }
            }
            if (toRemove != null) {
                grid.getObjects().remove(toRemove);
            }

            // Sprawdzenie interakcji z sąsiednimi odkurzaczami
            SimulationObject[] adjacent = grid.getAdjacentCells(positionX, positionY);
            for (SimulationObject adj : adjacent) {
                if (adj instanceof RobotVacuum other) {
                    interact(other);
                }
            }
        }
    }

    /**
     * Logika powrotu najkrótszą drogą do stacji dokującej (zakładamy pozycję stacji na 0,0).
     */
    public void returnToDock() {
        if (positionX > 0) positionX--;
        if (positionY > 0) positionY--;

        if (positionX == 0 && positionY == 0) {
            // Dotarto do stacji dokującej
            for (SimulationObject obj : grid.getObjects()) {
                if (obj instanceof DockingStation dock) {
                    dock.emptyVacuum(this);
                    break;
                }
            }
        }
    }

    /**
     * Interakcja między dwoma odkurzaczami
     */
    public void interact(RobotVacuum other) {
        if (random.nextBoolean()) {
            System.out.println("Odkurzacze wymieniły się informacjami o czystych strefach.");
        } else {
            System.out.println("Kolizja odkurzaczy! Utrata czasu.");
        }
    }

    public void resetTank() {
        this.currentTankLevel = 0;
        this.currentState = "SEARCHING";
    }

    // Gettery do statystyk
    public int getCollectedDirtTotal() { return collectedDirtTotal; }
    public String getCurrentState() { return currentState; }
}
