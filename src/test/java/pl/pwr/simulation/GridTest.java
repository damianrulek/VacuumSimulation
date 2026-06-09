package pl.pwr.simulation;

/**
 * Klasa testowa weryfikująca poprawność dodawania obiektó  oraz skanowania otoczenia planszy.
 */
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GridTest {

    @Test
    public void testAddObjectAndRetrieve() {
        Grid grid = new Grid(10, 10);
        Dirt dirt = new Dirt(5, 5, 2);

        grid.addObject(dirt);

        assertEquals(1, grid.getObjects().size(), "Plansza powinna zawierać dokładnie 1 obiekt.");
        assertTrue(grid.getObjects().contains(dirt), "Plansza powinna zawierać dodany śmieć.");
    }

    @Test
    public void testGetAdjacentCells() {
        Grid grid = new Grid(10, 10);
        // Główny obiekt na 5,5
        RobotVacuum robot = new RobotVacuum(5, 5, 1, 10, grid);
        // Sąsiad tuż obok na 6,5
        Dirt adjacentDirt = new Dirt(6, 5, 1);
        // Obiekt daleko na 1,1
        Dirt farDirt = new Dirt(1, 1, 1);

        grid.addObject(robot);
        grid.addObject(adjacentDirt);
        grid.addObject(farDirt);

        SimulationObject[] adjacent = grid.getAdjacentCells(5, 5);

        assertEquals(1, adjacent.length, "Powinien zostać wykryty tylko jeden sąsiad.");
        assertEquals(adjacentDirt, adjacent[0], "Wykrytym sąsiadem powinien być śmieć na [6,5].");
    }
}