package game;

import game.interfaces.IUpdateable;
import game.interfaces.Renderable;
import game.listeners.GridMouseListener;
import objects.ICell;
import objects.drawable.DrawableCell;
import utils.IRGBColor;
import utils.RGBColor;

import java.awt.*;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.function.Supplier;

/**
 * Created by Dale on 8/03/16.
 */
public abstract class GameOfLifeGrid<T extends ICell> extends Component implements Renderable<Graphics>, IUpdateable {
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
    private final IRGBColor cellColor = new RGBColor(100, 100, 100);
    private int gridCellWidth;
    private int gridCellHeight;
    private int yOffset = 0;
    private int xOffset = 0;
    private double scale = 1d;
    private BufferedImage imageBuffer;
    private Supplier<T> cellSupplier;


    {
        MouseMotionListener mMotionListener = new GridMouseListener<>(this);
        addMouseListener((MouseListener) mMotionListener);
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
        imageBuffer = new BufferedImage(getViewPortSize().width, getViewPortSize().height, BufferedImage.TYPE_INT_RGB);
    }

    private void drawGrid(Graphics g) {
        if (imageBuffer.getWidth() != getViewPortSize().width || imageBuffer.getHeight() != getViewPortSize().height)
            imageBuffer = new BufferedImage(getViewPortSize().width, getViewPortSize().height, BufferedImage.TYPE_INT_RGB);

        loop:
        for (int y = 0; y < getViewPortSize().height / gridCellHeight; y++) {
            for (int x = 0; x < getViewPortSize().width / gridCellWidth; x++) {
                Optional<DrawableCell<T>> c;
                if (!(c = getDrawableCell(x, y)).isPresent())
                    c = createDrawableCell(x, y);

                if (c.isPresent()) {
                    c.get().draw(imageBuffer.getGraphics(), (x * gridCellWidth + xOffset) * scale, (y * gridCellHeight + yOffset) * scale);
                }
            }
        }

        g.drawImage(imageBuffer, 0, 0, getViewPortSize().width, getViewPortSize().height, null);
        imageBuffer.getGraphics().clearRect(0, 0, getViewPortSize().width, getViewPortSize().height);
    }

    public Optional<DrawableCell<T>> createDrawableCell(int x, int y) {
        if ((y * (gridSize.width / gridCellWidth) + x) > drawableCells.length ||
                y * (gridSize.width / gridCellWidth) + x < 0)
            return Optional.empty();

        drawableCells[(((y * (gridSize.width / gridCellWidth)) + x))] = new DrawableCell<T>(cellSupplier.get(),
                this.gridCellWidth, this.gridCellHeight, x, y, cellColor);
        return Optional.of(drawableCells[(((y * (gridSize.width / gridCellWidth)) + x))]);
    }

    public int getAliveNeighbours(DrawableCell<T> cell) {
        if (!neighbourCellCache.containsKey(cell))
            neighbourCellCache.put(cell, getNeighbouringCells(cell));

        return (int) Arrays.stream(neighbourCellCache.get(cell)).filter(c -> c != null && c.getCell().isAlive()).count();
    }

    public HashSet<DrawableCell<T>> getCellsViableForNextGen(boolean clearSet) {
        HashSet<DrawableCell<T>> nextGenerationCells = new HashSet<>();

        gridSeeds.stream().forEach(cell -> {
            nextGenerationCells.add(cell);
            nextGenerationCells.addAll(Arrays.asList(getNeighbouringCells(cell)));
        });

        if (clearSet)
            gridSeeds.clear();

        return nextGenerationCells;
    }

    private HashSet<DrawableCell<T>> calculateNextGenSeedState(HashSet<DrawableCell<T>> cells) {
        cells.stream().forEach(cell -> {
            boolean isAlive = cell.getCell().isAlive();
            int neighbours = getAliveNeighbours(cell);
            if (isAlive && ((neighbours < 2) || (neighbours > 3))) {
                cell.getCell().setNextGenState(false);
            } else if (!isAlive && neighbours == 3) {
                cell.getCell().setNextGenState(true);
            }
        });
        return cells;
    }

    public void update() {
        calculateNextGenSeedState(getCellsViableForNextGen(true)).stream().forEach(cell -> {
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

        ArrayList<DrawableCell<T>> nCells = new ArrayList<>();

        for (int i = 0; i < neighbouringCells.length; i++) {
            Optional<DrawableCell<T>> nCell;
            if ((nCell = getDrawableCell(cell.getPosX() + neighbouringCells[i][0],
                    cell.getPosY() + neighbouringCells[i][1])).isPresent()) {
                nCells.add(nCell.get());
            }
        }
        return nCells.toArray(new DrawableCell[]{});
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

    public Optional<DrawableCell<T>> getDrawableCellAtMouseCoords(int mouseX, int mouseY) {
        return getDrawableCell((int) (mouseX / (gridCellWidth * getScale())), (int) (mouseY / (gridCellHeight * getScale())));
    }

    public Optional<DrawableCell<T>> getDrawableCell(int x, int y) {
        int index = ((y * (gridSize.width / gridCellWidth) + x));
        if (index > drawableCells.length || index < 0)
            return Optional.empty();
        return Optional.ofNullable(drawableCells[index]);
    }

    public T getCell(int x, int y) {
        return drawableCells[(y * gridSize.width / gridCellWidth) + x].getCell();
    }

    @Override
    public void render(Graphics g) {
        drawGrid(g);
    }

    public double getScale() {
        return scale;
    }

    public void setScale(double scale) {
        this.scale = scale;
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

    protected abstract Dimension getViewPortSize();
}
