package game;

import game.interfaces.Renderable;
import game.listeners.GridMouseListener;
import objects.Cell;
import objects.Drawable.DrawableCell;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by Dale on 8/03/16.
 */
public final class GameOfLifeGrid extends JPanel implements Renderable<Graphics> {
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
    private final HashSet<DrawableCell<?>> gridSeeds = new HashSet<>();
    private final HashMap<DrawableCell<?>, DrawableCell<?>[]> neighbourCellCache = new HashMap<>();
    private final DrawableCell<Cell>[] drawableCells;
    private final Dimension gridSize;
    private int gridCellWidth;
    private int gridCellHeight;
    private int yOffset = 0;
    private int xOffset = 0;
    private double scale = 1.00d;

    public GameOfLifeGrid(int gridWidth, int gridHeight, int cellWidth, int cellHeight) {
        gridSize = new Dimension(gridWidth, gridHeight);
        gridCellWidth = (cellWidth);
        gridCellHeight = (cellHeight);
        drawableCells = new DrawableCell[(gridWidth / gridCellWidth) * (gridHeight / gridCellHeight)];
    }

    public void init() {
        setMinimumSize(gridSize);
        setPreferredSize(gridSize);
        GridMouseListener<GameOfLifeGrid> mMotionListener = new GridMouseListener<>(this);
        addMouseListener(mMotionListener);
        addMouseMotionListener(mMotionListener);

        for (int i = 0; i < drawableCells.length; i++) {
            final int j = i;
            drawableCells[i] = new DrawableCell<>(new Cell() {
                @Override
                public void calculateNextGenState() {
                    boolean isAlive = isAlive();
                    int liveNeighbours = getAliveNeighbours(drawableCells[j]);

                    if (!isAlive && liveNeighbours == 3) {
                        setNextGen(true);
                    } else if (isAlive) {
                        if (liveNeighbours < 2) {
                            setNextGen(false);
                        } else if (liveNeighbours > 3) {
                            setNextGen(false);
                        } else {
                            setNextGen(true);
                        }
                    }
                }

            }, i, this.gridCellWidth, this.gridCellHeight);
        }
    }

    public int getAliveNeighbours(DrawableCell<?> cell) {
        if (!neighbourCellCache.containsKey(cell))
            neighbourCellCache.put(cell, getNeighbouringCells(cell));

        return (int) Arrays.stream(neighbourCellCache.get(cell)).filter(c -> c.getCell().isAlive()).count();
    }

    public DrawableCell<?>[] getNeighboursWithCache(DrawableCell<?> cell) {
        if (!neighbourCellCache.containsKey(cell))
            neighbourCellCache.put(cell, getNeighbouringCells(cell));

        return neighbourCellCache.get(cell);
    }

    public void drawGrid(Graphics g) {
        g.setColor(Color.gray);

        for (int y = 0; y < gridSize.height / gridCellHeight; y++) {
            for (int x = 0; x < gridSize.width / gridCellWidth; x++) {
                DrawableCell<Cell> c = getDrawableCell(x, y);
                c.setPosX(x);
                c.setPosY(y);
                c.draw(g, (x * gridCellWidth + xOffset) * scale, (y * gridCellHeight + yOffset) * scale);
            }
        }
    }

    public void nextGeneration() {
        HashSet<DrawableCell<?>> updateableCells = new HashSet<>();
        gridSeeds.stream().forEach(seed -> {
            updateableCells.addAll(Arrays.asList(getNeighbouringCells(seed)));
        });
        updateableCells.addAll(gridSeeds);
        gridSeeds.clear();
        System.out.println(updateableCells.size());

        updateableCells.stream().forEach(cell -> {

            System.out.println("is alive? :" + cell.getCell().isAlive() + " live neighbours " + getAliveNeighbours(cell));
            cell.getCell().calculateNextGenState();
            cell.getCell().setIsAlive(cell.getCell().getNextGenState());
            if (cell.getCell().isAlive()) {
                gridSeeds.add(cell);
            }
        });
    }

    public boolean containsSeed(DrawableCell<?> cell) {
        return gridSeeds.contains(cell);
    }

    public DrawableCell<?>[] getNeighbouringCells(DrawableCell<?> cell) {
        DrawableCell<?>[] neighbourCells = new DrawableCell[8];
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

    public Cell getCellAtMouseCoords(int mouseX, int mouseY) {
        return getCell(mouseX / gridCellWidth, mouseY / gridCellHeight);
    }

    public DrawableCell<Cell> getDrawableCellAtMouseCoords(int mouseX, int mouseY) {
        return getDrawableCell(mouseX / gridCellWidth, mouseY / gridCellHeight);
    }

    public DrawableCell<Cell> getDrawableCell(int x, int y) {
        return drawableCells[(y * (gridSize.width / gridCellWidth)) + x];
    }

    public Cell getCell(int x, int y) {
        return drawableCells[(y * gridSize.width / gridCellWidth) + x].getCell();
    }

    @Override
    public void render(Graphics g) {
        drawGrid(g);
    }

    public void addSeed(DrawableCell<?> cell) {
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
