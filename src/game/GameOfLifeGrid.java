package game;

import game.interfaces.IUpdateable;
import game.interfaces.Renderable;
import game.listeners.GridMouseListener;
import objects.Drawable.DrawableCell;
import objects.ICell;
import utils.RGBColor;

import java.awt.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.function.Supplier;

/**
 * Created by Dale on 8/03/16.
 */
public final class GameOfLifeGrid<T extends ICell> extends Component implements Renderable<Graphics>, IUpdateable {
    public static final int[][] neighbouringCells = {
            {-1, -1},
            {-1, 0},
            {-1, 1},
            {0, 1},
            {0, -1},
            {1, 1},
            {1, 0},
            {1, -1}
    };
    private final HashSet<DrawableCell<T>> gridSeeds = new HashSet<>();
    private final HashMap<DrawableCell<T>, DrawableCell<T>[]> neighbourCellCache = new HashMap<>();
    private final DrawableCell<T>[] drawableCells;
    private final Dimension gridSize;
    private int gridCellWidth;
    private int gridCellHeight;
    private int yOffset = 0;
    private int xOffset = 0;
    private double scale = 1.00d;
    private Supplier<T> cellSupplier;

    {
        GridMouseListener<GameOfLifeGrid> mMotionListener = new GridMouseListener<>(this);
        addMouseListener(mMotionListener);
        addMouseMotionListener(mMotionListener);
    }

    public GameOfLifeGrid(int gridWidth, int gridHeight, int cellWidth, int cellHeight, Supplier<T> supplier) {
        gridSize = new Dimension(gridWidth, gridHeight);
        gridCellWidth = (cellWidth);
        gridCellHeight = (cellHeight);
        cellSupplier = supplier;
        setMinimumSize(gridSize);
        setPreferredSize(gridSize);
        drawableCells = new DrawableCell[(gridWidth / gridCellWidth) * (gridHeight / gridCellHeight)];
    }

    private void drawGrid(Graphics g) {
        g.setColor(Color.gray);
        loop:
        for (int y = 0; y < gridSize.height / gridCellHeight; y++) {
            for (int x = 0; x < gridSize.width / gridCellWidth; x++) {
                DrawableCell<?> c;
                if ((c = getDrawableCell(x, y)) == null)
                    c = createDrawableCell(x, y);
                c.draw(g, (x * gridCellWidth + xOffset) * scale, (y * gridCellHeight + yOffset) * scale);
            }
        }
    }

    public DrawableCell<? extends ICell> createDrawableCell(int x, int y) {
        drawableCells[(((y * (gridSize.width / gridCellWidth)) + x))] = new DrawableCell<T>(cellSupplier.get(),
                this.gridCellWidth, this.gridCellHeight, x, y, new RGBColor());
        return drawableCells[(((y * (gridSize.width / gridCellWidth)) + x))];
    }

    public int getAliveNeighbours(DrawableCell<T> cell) {
        if (!neighbourCellCache.containsKey(cell))
            neighbourCellCache.put(cell, getNeighbouringCells(cell));

        return (int) Arrays.stream(neighbourCellCache.get(cell)).filter(c -> c.getCell().isAlive()).count();
    }

    public HashSet<DrawableCell<T>> getCellsViableForNextGen(HashSet<DrawableCell<T>> seed, boolean clearSet) {
        HashSet<DrawableCell<T>> nextGenerationCells = new HashSet<>();

        seed.stream().forEach(cell -> {
            nextGenerationCells.add(cell);
            nextGenerationCells.addAll(Arrays.asList(getNeighbouringCells(cell)));
        });

        if (clearSet)
            seed.clear();

        return nextGenerationCells;
    }

    private HashSet<DrawableCell<T>> transformNextGen(HashSet<DrawableCell<T>> cells) {
        cells.stream().forEach(cell -> {
            boolean isAlive = cell.getCell().isAlive();
            int neighbours = getAliveNeighbours(cell);
            if (isAlive && ((neighbours < 2) || (neighbours > 3))) {
                cell.getCell().setIsAlive(false);
            } else if (!isAlive && neighbours == 3) {
                cell.getCell().setIsAlive(true);
            }
        });
        return cells;
    }

    public void update() {
        transformNextGen(getCellsViableForNextGen(gridSeeds, true)).stream().forEach(cell -> {
            if (cell.getCell().transform()) {
                gridSeeds.add(cell);
            } else {
                neighbourCellCache.remove(cell);
            }
        });
    }

    public boolean containsSeed(DrawableCell<T> cell) {
        return gridSeeds.contains(cell);
    }

    public DrawableCell<T>[] getNeighbouringCells(DrawableCell<T> cell) {
        if (neighbourCellCache.containsKey(cell))
            return neighbourCellCache.get(cell);

        DrawableCell<T>[] neighbourCells = new DrawableCell[8];
        for (int i = 0; i < neighbouringCells.length; i++) {
            neighbourCells[i] = getDrawableCell(cell.getPosX() + neighbouringCells[i][0],
                    cell.getPosY() + neighbouringCells[i][1]);
        }
        return neighbourCells;
    }

    public int getGridHeight() {
        return gridSize.height;
    }

    public void setGridHeight(int gridHeight) {
        gridSize.height = gridHeight;
    }

    public int getGridWidth() {
        return gridSize.width;
    }

    public void setGridWidth(int gridWidth) {
        gridSize.width = gridWidth;
    }

    public T getCellAtMouseCoords(int mouseX, int mouseY) {
        return getCell(mouseX / gridCellWidth, mouseY / gridCellHeight);
    }

    public DrawableCell<T> getDrawableCellAtMouseCoords(int mouseX, int mouseY) {
        return getDrawableCell(mouseX / gridCellWidth, mouseY / gridCellHeight);
    }

    public DrawableCell<T> getDrawableCell(int x, int y) {
        return drawableCells[(y * (gridSize.width / gridCellWidth) + x)];
    }

    public T getCell(int x, int y) {
        return drawableCells[(y * gridSize.width / gridCellWidth) + x].getCell();
    }

    @Override
    public void render(Graphics g) {
        drawGrid(g);
    }

    public void addSeed(DrawableCell<T> cell) {
        gridSeeds.add(cell);
    }

    public void removeSeed(DrawableCell<?> cell) {
        gridSeeds.remove(cell);
    }

    public void setyOffset(int yOffset) {
        this.yOffset = yOffset;
    }

    public void setxOffset(int xOffset) {
        this.xOffset = xOffset;
    }

    public void setScale(double scale) {
        this.scale = scale;
    }
}
