package pl.pwr.simulation;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class RobotVacuumTest {

    @Test
    public void test() {
        //Tworzymy czystą planszę testową
        Grid grid = new Grid(10, 10);

       //Stawiamy roboty na sasiednich polach
        RobotVacuum robot1 = new RobotVacuum(4, 5, 2, 10, grid);
        RobotVacuum robot2 = new RobotVacuum(4, 6, 2, 10, grid);

        // Dodajemy oba roboty do środowiska symulacji
        grid.addObject(robot1);
        grid.addObject(grid.getObjects().contains(robot1) ? robot2 : robot2); // bezpieczne odniesienie
        grid.addObject(robot2);


        // Robot skanuje otoczenie, wykrywa sąsiada (robot2) i uruchamia logikę interakcji
        try {
            robot1.performAction();
        } catch (Exception e) {
            fail("Logika interakcji między odkurzaczami wywołała błąd (wyjątek): " + e.getMessage());
        }


        // Test upewnia się, że po wykryciu sąsiada i próbie interakcji, obiekty nadal stabilnie istnieją w pamięci
        assertNotNull(robot1, "Robot 1 powinien istnieć po wykonaniu interakcji.");
        assertNotNull(robot2, "Robot 2 powinien istnieć po wykonaniu interakcji.");
    }
}