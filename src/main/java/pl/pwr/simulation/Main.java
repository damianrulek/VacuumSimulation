package pl.pwr.simulation;

public class Main {
    public static void main(String[] args) {
        // 1. Definiujemy nazwę pliku z konfiguracją wejściową
        String configFile = "konfiguracja.csv";

        // 2. Ładujemy parametry
        ConfigLoader config = new ConfigLoader();
        config.loadConfig(configFile);

        // 3. Przekazujemy konfigurację i odpalamy symulację
        Simulation simulation = new Simulation(config);
        simulation.startSimulation();
    }
}