package pl.pwr.simulation;

/**
 * Klasa reprezentująca wczytywanie z pliku
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ConfigLoader {
    // Bezpieczne wartości domyślne na wypadek, gdyby plik był całkowicie zepsuty
    private int width = 10;
    private int height = 10;
    private String spawnMode = "LOSOWO";
    private int randomCount = 3;
    private final List<int[]> robotPositions = new ArrayList<>();

    public void loadConfig(String fileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            int lineNumber = 0; // Licznik linii dla lepszych komunikatów o błędach

            while ((line = br.readLine()) != null) {
                lineNumber++;
                if (line.trim().isEmpty() || line.startsWith("#")) {
                    continue;
                }

                String[] parts = line.split(",");
                String key = parts[0].trim().toUpperCase();

                // WEWNĘTRZNY BLOK TRY-CATCH DO WYŁAPYWANIA BŁĘDÓW UŻYTKOWNIKA
                try {
                    switch (key) {
                        case "SZEROKOSC" -> width = Integer.parseInt(parts[1].trim());
                        case "WYSOKOSC" -> height = Integer.parseInt(parts[1].trim());
                        case "TRYB_SPAWNU" -> spawnMode = parts[1].trim().toUpperCase();
                        case "ILOSC_ROBOTOW" -> randomCount = Integer.parseInt(parts[1].trim());
                        case "POZYCJA_ROBOTA" -> {
                            int x = Integer.parseInt(parts[1].trim());
                            int y = Integer.parseInt(parts[2].trim());
                            robotPositions.add(new int[]{x, y});
                        }
                    }
                } catch (NumberFormatException e) {
                    System.err.println("[BŁĄD] Linia " + lineNumber + ": Oczekiwano liczby, a wpisano literę lub znak! -> (" + line + "). Zignorowano tę linię.");
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.err.println("[BŁĄD] Linia " + lineNumber + ": Brakuje wartości po przecinku! -> (" + line + "). Zignorowano tę linię.");
                }
            }
        } catch (IOException e) {
            System.err.println("Nie udało się odczytać pliku: " + e.getMessage() + ". Uruchamiam z ustawieniami domyślnymi.");
        }
    }

    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public String getSpawnMode() { return spawnMode; }
    public int getRandomCount() { return randomCount; }
    public List<int[]> getRobotPositions() { return robotPositions; }
}