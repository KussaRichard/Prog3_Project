package projectTests;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import project.Grid;
import project.Save;

import javax.swing.*;
import java.io.File;

public class SaveTest {
    Grid grid;
    Save saver;
    JLabel message;
    File file;

    @Before
    public void setUp() {
        grid = new Grid(50,50);
        saver = new Save();
        message = new JLabel("");
        file = new File("output\\0.png");
    }

    @Test
    public void testPrint() { // "print" funkció tesztelése
        if(file.exists()) { // Ha már létezett 0.png nevű fájl a célhelyen akkor töröljük
            file.delete();
        }
        saver.print(grid, message);
        Assert.assertEquals("    Kép elmentve! (output\\0.png)", message.getText());
        file = new File("output\\0.png");
        Assert.assertTrue(file.exists()); // Ha képkészítő helyesen működik, akkor léteznie kell a 0.png nevű fájlnak
    }
}
