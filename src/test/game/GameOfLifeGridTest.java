package game;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import objects.Cell;
import objects.drawable.DrawableCell;
import org.junit.Assert;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashSet;

/**
 * GameOfLifeGrid Tester.
 *
 * @author <Authors name>
 * @since <pre>03/13/2016</pre>
 * @version 1.0
 */
public class GameOfLifeGridTest extends TestCase {
    private final GameOfLifeGrid<Cell> gameOfLifeGrid = new GameOfLifeGrid(2000, 2000, 0, 0, 10, 10, () -> new Cell()) {
        @Override
        protected Dimension getViewPortSize() {
            return new Dimension(800, 800);
        }
    };

    public GameOfLifeGridTest(String name) {
        super(name);
    }

    public static Test suite() {
        return new TestSuite(GameOfLifeGridTest.class);
    }

    public void setUp() throws Exception {
        super.setUp();
        BufferedImage bufferedImage = new BufferedImage(800, 800, BufferedImage.TYPE_INT_RGB);
        gameOfLifeGrid.render(bufferedImage.getGraphics());
    }

    public void tearDown() throws Exception {
        super.tearDown();
    }

    public void testGetAliveNeighbours() throws Exception {
        Assert.assertTrue(gameOfLifeGrid.setCellAlive(10, 10));
        Assert.assertTrue(gameOfLifeGrid.setCellAlive(10, 11));
        int neighbours = gameOfLifeGrid.getAliveNeighbours(gameOfLifeGrid.getDrawableCell(10, 10).get());
        Assert.assertTrue(neighbours == 1);
    }

    public void testGetCellsViableForNextGen() throws Exception {
        gameOfLifeGrid.addSeed(10, 10);
        gameOfLifeGrid.addSeed(10, 11);
        gameOfLifeGrid.addSeed(10, 12);
        HashSet<DrawableCell<Cell>> nGenCells = gameOfLifeGrid.getCellsViableForNextGen(true);

        //S = Number of seeds
        //D = duplicate neighbours
        // Formulae = ((S*8) - D) + S
        Assert.assertEquals(nGenCells.size(), 15);
    }

    public void testGetNeighbouringCells() throws Exception {
        //TODO: Test goes here...
    }

    public void testSetGetGridHeight() throws Exception {
        //TODO: Test goes here...
    }

    public void testSetGetGridWidth() throws Exception {
        //TODO: Test goes here...
    }

    public void testGetCellAtMouseCoords() throws Exception {
        //TODO: Test goes here...
    }

    public void testGetDrawableCellAtMouseCoords() throws Exception {
        //TODO: Test goes here...
    }

    public void testGetDrawableCell() throws Exception {
        //TODO: Test goes here...
    }

    public void testGetCell() throws Exception {
        //TODO: Test goes here...
    }

    public void testSetGetScale() throws Exception {
        //TODO: Test goes here...
    }

    public void testSetyOffset() throws Exception {
        //TODO: Test goes here...
    }

    public void testSetxOffset() throws Exception {
        //TODO: Test goes here...
    }

    public void testGetViewPortSize() throws Exception {
        //TODO: Test goes here...
    }

    public void testGetAccessibleName() throws Exception {
        //TODO: Test goes here...
    }

    public void testGetAccessibleDescription() throws Exception {
        //TODO: Test goes here...
    }

    public void testGetAccessibleRole() throws Exception {
        //TODO: Test goes here...
    }

    public void testGetAccessibleStateSet() throws Exception {
        //TODO: Test goes here...
    }

    public void testGetAccessibleParent() throws Exception {
        //TODO: Test goes here...
    }

    public void testGetAccessibleIndexInParent() throws Exception {
        //TODO: Test goes here...
    }

    public void testGetAccessibleChildrenCount() throws Exception {
        //TODO: Test goes here...
    }

    public void testGetAccessibleChild() throws Exception {
        //TODO: Test goes here...
    }

    public void testGetLocale() throws Exception {
        //TODO: Test goes here...
    }

    public void testGetAccessibleComponent() throws Exception {
        //TODO: Test goes here...
    }

    public void testSetGetBackground() throws Exception {
        //TODO: Test goes here...
    }

    public void testSetGetForeground() throws Exception {
        //TODO: Test goes here...
    }

    public void testSetGetCursor() throws Exception {
        //TODO: Test goes here...
    }

    public void testSetGetFont() throws Exception {
        //TODO: Test goes here...
    }

    public void testGetFontMetrics() throws Exception {
        //TODO: Test goes here...
    }

    public void testSetEnabled() throws Exception {
        //TODO: Test goes here...
    }

    public void testSetVisible() throws Exception {
        //TODO: Test goes here...
    }

    public void testGetLocationOnScreen() throws Exception {
        //TODO: Test goes here...
    }

    public void testSetGetLocation() throws Exception {
        //TODO: Test goes here...
    }

    public void testSetGetBounds() throws Exception {
        //TODO: Test goes here...
    }

    public void testSetGetSize() throws Exception {
        //TODO: Test goes here...
    }

    public void testGetAccessibleAt() throws Exception {
        //TODO: Test goes here...
    }

    public void testGetCapabilities() throws Exception {
        //TODO: Test goes here...
    }

    public void testGetDrawGraphics() throws Exception {
        //TODO: Test goes here...
    }

    public void testGetCapabilities1() throws Exception {
        //TODO: Test goes here...
    }

    public void testGetDrawGraphics1() throws Exception {
        //TODO: Test goes here...
    }

    public void testGetBackBuffer() throws Exception {
        //TODO: Test goes here...
    }

    public void testGetBackBuffer1() throws Exception {
        //TODO: Test goes here...
    }

    public void testGetCapabilities2() throws Exception {
        //TODO: Test goes here...
    }

    public void testGetDrawGraphics2() throws Exception {
        //TODO: Test goes here...
    }
}
