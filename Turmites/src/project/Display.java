package project;

import javax.swing.*;
import java.awt.*;

public class Display extends JFrame {
    private Grid grid;
    int x;
    int y;
    public Display(Grid g, int idx, int idy) {
        grid = g;
        x = idx;
        y = idy;
        init();
    }
    private void init() {
        setMinimumSize(new Dimension(500, 500));
        setResizable(false);
        setUndecorated(true);
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
                    if(x == i && y == j) {
                        g.setColor(Color.RED);
                    }
                    else {
                        g.setColor(getColorForValue(value));
                    }
                    g.fillRect(j * cellWidth, i * cellHeight, cellWidth, cellHeight);
                }
            }
        }
    }
    public void refresh(Grid newGrid, int idx, int idy) {
        x = idx;
        y = idy;
        grid = newGrid;
        repaint();
    }
    public Color getColorForValue(int value) {
        if (value == 1) {
            return Color.WHITE;
        } else if (value == 2) {
            return Color.BLUE;
        } else if (value == 3) {
            return Color.YELLOW;
        } else if (value == 4) {
            return Color.GREEN;
        }
        return Color.BLACK;
    }
}
