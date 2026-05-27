package pl.pwr.simulation;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Simulation {
    private boolean isRunning;
    private final Grid grid;
    private final List<RobotVacuum> vacuums;
    private final ConfigLoader config; // Przechowujemy obiekt konfiguracji

    // Konstruktor przyjmuje teraz gotowy obiekt konfiguracji (Wstrzykiwanie zależności)
    public Simulation(ConfigLoader config) {
        this.config = config;
        this.grid = new Grid(config.getWidth(), config.getHeight());
        this.vacuums = new ArrayList<>();
        this.isRunning = false;
    }

    public void startSimulation() {
        this.isRunning = true;

        // Inicjalizacja stacji dokującej
        DockingStation dock = new DockingStation(0, 0);
        grid.addObject(dock);
        grid.generateDirt();

        Random random = new Random();

        // LOGIKA RESPONSOWANIA AGENTÓW ZALEŻNA OD PLIKU
        if ("LOSOWO".equals(config.getSpawnMode())) {
            // Opcja 1: Losowe pozycje
            for (int i = 0; i < config.getRandomCount(); i++) {
                int x = random.nextInt(config.getWidth());
                int y = random.nextInt(config.getHeight());
                RobotVacuum vacuum = new RobotVacuum(x, y, 2, 10, grid);
                vacuums.add(vacuum);
                grid.addObject(vacuum);
                System.out.println("Zrespowano odkurzacz " + i + " LOSOWO na pozycji: [" + x + "," + y + "]");
            }
        } else if ("PLIK".equals(config.getSpawnMode())) {
            // Opcja 2: Dokładne pozycje z pliku CSV
            List<int[]> positions = config.getRobotPositions();
            for (int i = 0; i < positions.size(); i++) {
                int[] pos = positions.get(i);
                // Zabezpieczenie przed wyjściem współrzędnych poza wygenerowaną planszę
                int x = Math.max(0, Math.min(config.getWidth() - 1, pos[0]));
                int y = Math.max(0, Math.min(config.getHeight() - 1, pos[1]));

                RobotVacuum vacuum = new RobotVacuum(x, y, 2, 10, grid);
                vacuums.add(vacuum);
                grid.addObject(vacuum);
                System.out.println("Zrespowano odkurzacz " + (i + 1) + " z PLIKU na pozycji: [" + x + "," + y + "]");
            }
        }

        int epoch = 0;
        while (isRunning) {
            System.out.println("--- Epoka " + epoch + " ---");

            List<SimulationObject> currentObjects = new ArrayList<>(grid.getObjects());
            for (SimulationObject obj : currentObjects) {
                obj.performAction();
            }

            boolean dirtRemaining = false;
            for (SimulationObject obj : grid.getObjects()) {
                if (obj instanceof Dirt) {
                    dirtRemaining = true;
                    break;
                }
            }

            if (!dirtRemaining) {
                isRunning = false;
                System.out.println("Symulacja zakończona! Wszystkie śmieci zostały zebrane.");
            }

            epoch++;

            if (epoch > 100) {
                isRunning = false;
            }
        }

        exportDataToCSV();
    }

    public void exportDataToCSV() {
        String csvFile = "statystyki_symulacji.csv";
        try (FileWriter writer = new FileWriter(csvFile)) {
            writer.append("Id_Odkurzacza,Zebrany_Smiec_Lacznie\n");
            for (int i = 0; i < vacuums.size(); i++) {
                writer.append("Odkurzacz_")
                        .append(String.valueOf(i + 1))
                        .append(",")
                        .append(String.valueOf(vacuums.get(i).getCollectedDirtTotal()))
                        .append("\n");
            }
            System.out.println("Dane pomyślnie zapisane do pliku: " + csvFile);
        } catch (IOException e) {
            System.err.println("Błąd podczas zapisu do pliku CSV: " + e.getMessage());
        }
    }
    public Grid getGrid() {
        return grid;
    }
}