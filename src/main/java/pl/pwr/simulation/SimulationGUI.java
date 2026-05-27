package pl.pwr.simulation;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class SimulationGUI extends JFrame {
    private final Simulation simulation;
    private final GridPanel gridPanel;
    private final int cellSize = 50; // Rozmiar jednej kratki w pikselach

    public SimulationGUI(Simulation simulation) {
        this.simulation = simulation;
        this.gridPanel = new GridPanel();

        setTitle("Symulacja Odkurzacza - GUI");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        add(gridPanel);

        // Dynamiczne dopasowanie rozmiaru okna do wymiarów siatki z konfiguracji
        int width = simulation.getGrid().getWidth() * cellSize + 16;
        int height = simulation.getGrid().getHeight() * cellSize + 39;

        setSize(width, height);
        setResizable(false);
        setLocationRelativeTo(null); // Centrowanie okna na ekranie
    }

    // Metoda wywoływana z pętli symulacji do odświeżania widoku
    public void refresh() {
        gridPanel.repaint();
    }

    // Wewnętrzna klasa rysująca planszę
    private class GridPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Grid grid = simulation.getGrid();

            // Rysowanie białego tła
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, getWidth(), getHeight());

            // Rysowanie linii siatki
            g.setColor(Color.LIGHT_GRAY);
            for (int i = 0; i <= grid.getWidth(); i++) {
                g.drawLine(i * cellSize, 0, i * cellSize, grid.getHeight() * cellSize);
            }
            for (int j = 0; j <= grid.getHeight(); j++) {
                g.drawLine(0, j * cellSize, grid.getWidth() * cellSize, j * cellSize);
            }

            // Bezpieczne rysowanie obiektów z obsługą potencjalnego błędu współbieżności
            try {
                List<SimulationObject> objects = grid.getObjects();
                for (int i = 0; i < objects.size(); i++) {
                    SimulationObject obj = objects.get(i);
                    if (obj == null) continue;

                    int x = obj.getPositionX() * cellSize;
                    int y = obj.getPositionY() * cellSize;

                    if (obj instanceof DockingStation) {
                        // BAZA: Niebieski kwadrat
                        g.setColor(new Color(41, 128, 185));
                        g.fillRect(x + 2, y + 2, cellSize - 4, cellSize - 4);
                        g.setColor(Color.WHITE);
                        g.setFont(new Font("Arial", Font.BOLD, 11));
                        g.drawString("BAZA", x + 10, y + 28);

                    } else if (obj instanceof Dirt) {
                        // ŚMIEĆ: Brązowe kółko (rozmiar zależy od pola 'size' śmiecia)
                        g.setColor(new Color(139, 69, 19));
                        int dirtSize = ((Dirt) obj).getSize() * 6 + 10;
                        g.fillOval(x + (cellSize - dirtSize) / 2, y + (cellSize - dirtSize) / 2, dirtSize, dirtSize);

                    } else if (obj instanceof RobotVacuum robot) {
                        // ROBOT: Zielony gdy szuka, Pomarańczowy gdy wraca
                        if ("RETURNING_TO_DOCK".equals(robot.getCurrentState())) {
                            g.setColor(new Color(230, 126, 34)); // Pomarańczowy
                        } else {
                            g.setColor(new Color(46, 204, 113)); // Zielony
                        }

                        // ZMIANA: Kwadrat wypełniający pole (1px marginesu, aby siatka była widoczna)
                        g.fillRect(x + 1, y + 1, cellSize - 2, cellSize - 2);

                        // Dodatkowa czarna ramka wokół odkurzacza - idealnie widać kiedy się stykają
                        g.setColor(Color.BLACK);
                        g.drawRect(x + 1, y + 1, cellSize - 2, cellSize - 2);
                    }
                }
            } catch (Exception e) {
                // Ignorujemy błędy jednoczesnego dostępu – zostaną naprawione w kolejnej klatce animacji
            }
        }
    }
}