package pl.pwr.simulation;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Klasa odpowiedzialna za wczytywanie parametrów początkowych symulacji z pliku CSV.
 * Realizuje zasadę SRP (Single Responsibility Principle).
 */
public class ConfigLoader {
    private int width;
    private int height;
    private String spawnMode; // "LOSOWO" lub "PLIK"
    private int randomCount;
    private final List<int[]> robotPositions = new ArrayList<>();

    /**
     * Wczytuje konfigurację z podanego pliku CSV.
     */
    public void loadConfig(String fileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty() || line.startsWith("#")) {
                    continue; // Pomijaj puste linie i komentarze
                }
                String[] parts = line.split(",");
                String key = parts[0].trim().toUpperCase();

                switch (key) {
                    case "SZEROKOSC" -> width = Integer.parseInt(parts[1].trim());
                    case "WYSOKOSC" -> height = Integer.parseInt(parts[1].trim());
                    case "TRYB_SPAWNU" -> spawnMode = parts[1].trim().toUpperCase();
                    case "ILOSC_LOSOWYCH" -> randomCount = Integer.parseInt(parts[1].trim());
                    case "POZYCJA_ROBOTA" -> {
                        int x = Integer.parseInt(parts[1].trim());
                        int y = Integer.parseInt(parts[2].trim());
                        robotPositions.add(new int[]{x, y});
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Błąd ładowania konfiguracji, używam wartości domyślnych: " + e.getMessage());
            // Bezpieczne wartości domyślne, aby program nigdy się nie wywalił
            width = 10;
            height = 10;
            spawnMode = "LOSOWO";
            randomCount = 3;
        }
    }

    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public String getSpawnMode() { return spawnMode; }
    public int getRandomCount() { return randomCount; }
    public List<int[]> getRobotPositions() { return robotPositions; }
}