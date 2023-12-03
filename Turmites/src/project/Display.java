package project;

import javax.swing.*;
import java.awt.*;

public class Display extends JFrame {
    public Grid grid; // A rács
    private int x; // Turmesz x koordinátája
    private int y; // Turmesz y koordinátája
    public Display(Grid g, int idx, int idy) {
        grid = g;
        x = idx;
        y = idy;
        init();
    }
    private void init() {
        setMinimumSize(new Dimension(500, 500));
        setResizable(false);
        setUndecorated(true); // Nincs rá szükség, mert a menü ablakában van külön gomb a kilépéshez
        add(new GridPanel());
    }
    private class GridPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            int rows = grid.getHeight();
            int cols = grid.getWidth();

            int cellWidth = getWidth() / cols;
            int cellHeight = getHeight() / rows;

            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    int value = grid.getValueAt(i,j);
                    if(x == i && y == j) { // Azt a helyet, ahol a hangya áll pirossal jelöljük
                        g.setColor(Color.RED);
                    }
                    else { // Minden más cellát értékének megfelelően színezzük
                        g.setColor(getColorForValue(value));
                    }
                    g.fillRect(j * cellWidth, i * cellHeight, cellWidth, cellHeight);
                }
            }
        }
    }
    /// Képernyő frissítése ///
    public void refresh(Grid newGrid, int idx, int idy) {
        x = idx; // Megváltozhatott a hangya pozíciója
        y = idy;
        grid = newGrid; // Megváltozhattak a cellaértékek
        repaint();
    }
    /// Színek megadása érték szerint ///
    public Color getColorForValue(int value) {
        if (value == 1) { // Jelenleg csak négy színnel dolgozunk, de ezt lehet tovább bővíteni
            return Color.WHITE;
        } else if (value == 2) {
            return Color.BLUE;
        } else if (value == 3) {
            return Color.YELLOW;
        } else if (value == 4) {
            return Color.GREEN;
        }
        return Color.BLACK; // A háttérszín fekete
    }
}
