package projectTests;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import project.Display;
import project.Grid;
import project.Turmite;

import java.io.IOException;

public class TurmiteTest {
    Grid grid;
    Grid grid2;
    Display display;
    Display display2;
    Turmite turmite;
    Turmite turmite2;

    @Before
    public void setUp() {
        grid = new Grid(6, 6);
        grid2 = new Grid(6, 6);
        display = new Display(grid, (int) Math.floor((double)grid.getWidth()/2), (int) Math.floor((double)grid.getHeight()/2));
        display2 = new Display(grid2, (int) Math.floor((double)grid.getWidth()/2), (int) Math.floor((double)grid.getHeight()/2));
        try {
            turmite = new Turmite(grid, display, "input\\LangtonSAnt.txt", 5);
            turmite2 = new Turmite(grid2, display2, "input\\LangtonSAnt.txt", 1);
        } catch (IOException e) { /* A default bemenetnek jónak kell lennie */ }
    }

    @Test
    public void testTerminate() { // "terminate" funkció tesztelése
        Assert.assertFalse(turmite.isTerminated());
        turmite.start();
        turmite.terminate();
        Assert.assertTrue(turmite.isTerminated()); // Igen, leállítottok
        Assert.assertTrue(turmite.isAlive()); // Ettől még a szál életben van
    }

    @Test
    public void testGetXY() { // "getX" és "getY" funkció tesztelése
        grid2.resize(10,10);
        Assert.assertEquals(3, turmite.getX());
        Assert.assertEquals(3, turmite.getY());
        turmite.setStartingPosition(grid2);
        Assert.assertEquals(5, turmite.getX());
        Assert.assertEquals(5, turmite.getY());
    }

    @Test
    public void testSetDirection() { // "setDirection" funkció tesztelése
        turmite.start();
        while (!turmite.isTerminated()) {
            // Megvárjuk amíg lefut a pályáról
        }
        display = new Display(grid2, (int) Math.floor((double) grid.getWidth()/2), (int) Math.floor((double) grid.getHeight()/2));
        try {
            turmite = new Turmite(grid2, display, "input\\LangtonSAnt.txt", 5);
        } catch (IOException e) { /* A default bemenetnek jónak kell lennie */ }
        turmite.setDirection(2); // Eredetileg ez az érték 0 volt
        turmite.start();
        while (!turmite.isTerminated()) {
            // Megvárjuk amíg lefut a pályáról
        }
        Assert.assertNotEquals(grid, grid2); // Ha más irányba nézett mikor másodszorra indult, akkor az eredeti rács elforgatott verzióját kapjuk eredméyül
    }

    @Test
    public void testSpeed() { // "setSpeed" funkció tesztelése
        turmite2.start(); // Lassabb hangya
        turmite.start(); // Sokkal gyorsabb hangya
        while (!turmite.isTerminated() && !turmite2.isTerminated()) {
            // Megvárjuk amíg egyikük lefut a pályáról
        }
        Assert.assertFalse(turmite2.isTerminated());
        Assert.assertTrue(turmite.isTerminated()); // Ekkor a gyorsabb hangyának kellett előbb végeznie

        /// Akkor cseréljük fel a szerepeket ///
        grid = new Grid(6, 6);
        grid2 = new Grid(6, 6);
        display = new Display(grid, (int) Math.floor((double) grid.getWidth()/2), (int) Math.floor((double) grid.getHeight()/2));
        display2 = new Display(grid2, (int) Math.floor((double) grid.getWidth()/2), (int) Math.floor((double) grid.getHeight()/2));
        try {
            turmite = new Turmite(grid, display, "input\\LangtonSAnt.txt", 5);
            turmite2 = new Turmite(grid2, display2, "input\\LangtonSAnt.txt", 1);
        } catch (IOException e) { /* A default bemenetnek jónak kell lennie */ }
        turmite.setSpeed(1); // Használjuk a tesztelni kívánt függvényt
        turmite2.setSpeed(5);
        turmite.start();
        turmite2.start(); // Ezúttal ő a gyorsabb
        while (!turmite.isTerminated() && !turmite2.isTerminated()) {
            // Megvárjuk amíg egyikük lefut a pályáról
        }
        Assert.assertTrue(turmite2.isTerminated());
        Assert.assertFalse(turmite.isTerminated());
    }

    @Test
    public void testSleepNWakeUp() { // "sleepNWakeUp" funkció tesztelése
        grid.resize(3,3);
        grid2.resize(3,3);
        try {
            turmite = new Turmite(grid, display, "input\\LangtonSAnt.txt", 5); // Most mindketten ugyanolyan gyorsak
            turmite2 = new Turmite(grid2, display2, "input\\LangtonSAnt.txt", 5);
        } catch (IOException e) { /* A default bemenetnek jónak kell lennie */ }
        turmite.sleepNWakeUp(); // Most őt elaltattuk
        turmite.start();
        turmite2.start();
        while (!turmite.isTerminated() && !turmite2.isTerminated()) {
            // Megvárjuk amíg egyikük lefut a pályáról
        }
        Assert.assertTrue(turmite2.isTerminated());
        Assert.assertFalse(turmite.isTerminated()); // Végzett volna (előbb is indult, mint a társa), de elaludt

        /// Ébresszük fel ///
        turmite.sleepNWakeUp(); // Ugyanazzal a függvénnyel tudjuk felébreszteni is
        while (!turmite.isTerminated()) {
            // Megvárjuk amíg lefut a pályáról
        }
        Assert.assertTrue(turmite.isTerminated());
    }

    @Test
    public void testRead() { // "read" funkció tesztelése
        try {
            turmite = new Turmite(grid, display, "input\\Good.txt", 1); // Teljesen jó bemenet, csak kicsit furán néz ki
            turmite = new Turmite(grid, display, "input\\NotWorking.txt", 1); // Lehet hogy a hangya nem működik, ettől még a bemenet jó lesz
        } catch (IOException e) {
            throw new AssertionError();
        }
        Assert.assertThrows(IOException.class, () -> {
            turmite = new Turmite(grid, display, "input\\Bad.txt", 1); // Rossz a bemenet, nem követi a formai szabályokat
        });
        Assert.assertThrows(IOException.class, () -> {
            turmite = new Turmite(grid, display, "input\\DoesntExist.txt", 1); // Nem létezik a bemenet
        });
    }
}
