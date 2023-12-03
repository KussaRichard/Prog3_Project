package projectTests;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import project.Display;
import project.Grid;

import java.awt.*;

public class DisplayTest {
    Grid grid;
    Display display;

    @Before
    public void setUp() {
        grid = new Grid(5, 5);
        grid.setValueAt(0, 0, 0);
        grid.setValueAt(1, 1, 1);
        grid.setValueAt(2, 2, 2);
        grid.setValueAt(3, 3, 3);
        grid.setValueAt(4, 4, 4);
        display = new Display(grid, 0, 0);
    }

    @Test
    public void testColor() { // "getColorForValue" funkció tesztelése minden színre
        Assert.assertEquals(Color.BLACK, display.getColorForValue(grid.getValueAt(0,0)));
        Assert.assertEquals(Color.WHITE, display.getColorForValue(grid.getValueAt(1,1)));
        Assert.assertEquals(Color.BLUE, display.getColorForValue(grid.getValueAt(2,2)));
        Assert.assertEquals(Color.YELLOW, display.getColorForValue(grid.getValueAt(3,3)));
        Assert.assertEquals(Color.GREEN, display.getColorForValue(grid.getValueAt(4,4)));
    }

    @Test
    public void testRefresh() { // "refresh" funkció tesztelése
        int width = grid.getWidth();
        int height = grid.getHeight();
        grid.resize(6, 6);
        display.refresh(grid, 0, 0);
        Assert.assertNotEquals(width, display.grid.getWidth());
        Assert.assertNotEquals(height, display.grid.getHeight());
    }
}
