package project;

import javax.swing.*;
import java.awt.*;

/**
 * Osztály a rács és hangya megjelenítésére.
 */
public class Display extends JFrame {
    /**
     * A rács amit meg akarunk jeleníteni.
     */
    public Grid grid;
    /**
     * A hangya x koordinátája
     */
    private int x;
    /**
     * A hangya y koordinátája
     */
    private int y;

    /**
     * Display konstruktora.
     * @param g Grid, amit meg akarunk jeleníteni.
     * @param idx A hangya x koordinátája.
     * @param idy A hangya y koordinátája.
     */
    public Display(Grid g, int idx, int idy) {
        grid = g;
        x = idx;
        y = idy;
        init();
    }

    /**
     * Funkció, ami inicializálja a megjelenítő ablakot.
     */
    private void init() {
        setMinimumSize(new Dimension(500, 500));
        setResizable(false);
        setUndecorated(true); // Nincs rá szükség, mert a menü ablakában van külön gomb a kilépéshez
        add(new GridPanel());
    }

    /**
     * Osztály, ami megrajzolja a pályát.
     */
    private class GridPanel extends JPanel {
        /**
         * Egyetlen funkció, ami minden cella értékenek megfelelően rajzol.
         * @param g Graphics komponens.
         */
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

    /**
     * Funkció, ami frissíti a képernyőt.
     * @param newGrid Új Grid, amit megjeleníteni kívánunk
     * @param idx A hangya x koordinátája.
     * @param idy A hangya y koordinátája.
     */
    public void refresh(Grid newGrid, int idx, int idy) {
        x = idx; // Megváltozhatott a hangya pozíciója
        y = idy;
        grid = newGrid; // Megváltozhattak a cellaértékek
        repaint();
    }

    /**
     * Funkció, ami visszaad egy színt, a megadott érték alapján.
     * @param value Cella értéke.
     * @return A megadott értékhez tartozó szín.
     */
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
