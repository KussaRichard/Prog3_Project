package projectTests;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import project.Grid;

public class GridTest {
    Grid grid;

    @Before
    public void setUp() {
        grid = new Grid(2,3);
    }

    @Test
    public void testGetSize() { // "getHeight" és "getWidth" funkciók tesztelése
        Assert.assertEquals(2, grid.getHeight());
        Assert.assertEquals(3, grid.getWidth());
    }

    @Test
    public void testGetValue() { // "getValueAt" funkció tesztelése
        Assert.assertEquals(0, grid.getValueAt(0,0));
        Assert.assertEquals(0, grid.getValueAt(1,1));
    }

    @Test
    public void testSetValue() { // "setValueAt" funkció tesztelése
        Assert.assertEquals(0, grid.getValueAt(0,0));
        grid.setValueAt(0,0, 1);
        Assert.assertNotEquals(0, grid.getValueAt(0,0));
    }

    @Test
    public void testResize() { // "resize" funkció tesztelése
        grid.resize(5, 6);
        Assert.assertEquals(6, grid.getHeight());
        Assert.assertEquals(5, grid.getWidth());
    }
}
