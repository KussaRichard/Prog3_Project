package projectTests;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import project.Grid;
import project.Save;

import javax.swing.*;
import java.io.File;

/**
 * Osztály a Save osztály függvényeinek tesztelésére.
 */
public class SaveTest {
    Grid grid;
    Save saver;
    JLabel message;
    File file;

    /**
     * Inicializálja a tesztekhez szükséges változókat.
     */
    @Before
    public void setUp() {
        grid = new Grid(50,50);
        saver = new Save();
        message = new JLabel("");
        file = new File("output\\0.png");
    }

    /**
     * Teszteli print funkciót
     */
    @Test
    public void testPrint() {
        if(file.exists()) { // Ha már létezett 0.png nevű fájl a célhelyen akkor töröljük
            file.delete();
        }
        saver.print(grid, message);
        Assert.assertEquals("    Kép elmentve! (output\\0.png)", message.getText());
        file = new File("output\\0.png");
        Assert.assertTrue(file.exists()); // Ha képkészítő helyesen működik, akkor léteznie kell a 0.png nevű fájlnak
    }
}
