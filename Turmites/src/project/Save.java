package project;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Save {
    public void print(Grid grid, JLabel m) { // Egyetlen funkció kép készítéséhez
        BufferedImage image = new BufferedImage(grid.getWidth(), grid.getHeight(), BufferedImage.TYPE_INT_RGB);

        /// Színezés ///
        for (int y = 0; y < grid.getHeight(); y++) {
            for (int x = 0; x < grid.getWidth(); x++) {
                int colorValue = Color.BLACK.getRGB();
                if (grid.getValueAt(y,x) == 1) {
                    colorValue = Color.WHITE.getRGB();
                } else if (grid.getValueAt(y,x) == 2) {
                    colorValue = Color.BLUE.getRGB();
                } else if (grid.getValueAt(y,x) == 3) {
                    colorValue = Color.YELLOW.getRGB();
                } else if (grid.getValueAt(y,x) == 4) {
                    colorValue = Color.GREEN.getRGB();
                }
                image.setRGB(x, y, colorValue);
            }
        }

        /// Mentés ///
        try {
            int i = 0;
            String fileName;
            while (true) { // Olyan fájlt keresünk, ami eddig nem létezett (a korábbi képek is megmaradnak)
                fileName = "output\\" + Integer.toString(i) + ".png";
                File f = new File(fileName);
                if(!f.exists() && !f.isDirectory()) {
                    break;
                }
                i++;
            }
            File output = new File(fileName);
            ImageIO.write(image, "png", output);
            m.setText("    Kép elmentve! (" + fileName + ")");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}