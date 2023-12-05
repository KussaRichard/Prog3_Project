package projectTests;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import project.Grid;

/**
 * Osztály a Grid osztály függvényeinek tesztelésére.
 */
public class GridTest {
    Grid grid;

    /**
     * Inicializálja a tesztekhez szükséges változókat.
     */
    @Before
    public void setUp() {
        grid = new Grid(2,3);
    }

    /**
     * Teszteli a getHeight és getWidth funkciókat.
     */
    @Test
    public void testGetSize() {
        Assert.assertEquals(2, grid.getHeight());
        Assert.assertEquals(3, grid.getWidth());
    }

    /**
     * Teszteli a getValueAt funkciót.
     */
    @Test
    public void testGetValue() {
        Assert.assertEquals(0, grid.getValueAt(0,0));
        Assert.assertEquals(0, grid.getValueAt(1,1));
    }

    /**
     * Teszteli a setValueAt funkciót.
     */
    @Test
    public void testSetValue() {
        Assert.assertEquals(0, grid.getValueAt(0,0));
        grid.setValueAt(0,0, 1);
        Assert.assertNotEquals(0, grid.getValueAt(0,0));
    }

    /**
     * Teszteli a resize funkciót.
     */
    @Test
    public void testResize() {
        grid.resize(5, 6);
        Assert.assertEquals(6, grid.getHeight());
        Assert.assertEquals(5, grid.getWidth());
    }
}
