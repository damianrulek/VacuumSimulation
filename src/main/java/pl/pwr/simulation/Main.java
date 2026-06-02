package pl.pwr.simulation;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        // 1. Definiujemy nazwę pliku z konfiguracją wejściową
        String configFile = "konfiguracja.csv";

        // 2. Ładujemy parametry
        ConfigLoader config = new ConfigLoader();
        config.loadConfig(configFile);

        // 3. Przekazujemy konfigurację do symulacji
        Simulation simulation = new Simulation(config);

        // 4. Uruchamiamy GUI i symulację
        SwingUtilities.invokeLater(() -> {
            SimulationGUI gui = new SimulationGUI(simulation);
            simulation.setGUI(gui); // Łączymy symulację z widokiem GUI
            gui.setVisible(true);   // Pokazujemy okienko

            // Odpalamy logikę w nowym wątku, żeby GUI się nie zacięło
            new Thread(simulation::startSimulation).start();
        });
    }
}