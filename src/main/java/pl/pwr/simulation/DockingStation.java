package pl.pwr.simulation;

/**
 * Klasa reprezentująca stację dokującą dla odkurzaczy.
 */
public class DockingStation extends SimulationObject {

    public DockingStation(int positionX, int positionY) {
        super(positionX, positionY);
    }

    @Override
    public void performAction() {
        // Stacja dokująca nie wykonuje akcji autonomicznych
    }

    /**
     * Opróżnia zbiornik podanego odkurzacza.
     */
    public void emptyVacuum(RobotVacuum robot) {
        System.out.println("Odkurzacz opróżnia zbiornik w stacji dokującej.");
        robot.resetTank();
    }
}