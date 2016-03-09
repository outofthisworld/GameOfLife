package game;

import game.interfaces.Renderable;
import game.listeners.GridMouseListener;
import game.listeners.IGOLKeyPressListener;
import objects.Cell;
import objects.Drawable.DrawableCell;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Dale on 8/03/16.
 */
public class GameOfLifeGrid extends JPanel implements Renderable<Graphics>, IGOLKeyPressListener {
    private final DrawableCell<Cell>[] drawableCells;
    private int gridWidth;
    private int gridHeight;
    private int gridCellWidth;
    private int gridCellHeight;
    private int yOffset = 0;
    private int xOffset = 0;
    private double scale = 1.00d;
    private ArrayList<DrawableCell<?>> gridSeeds = new ArrayList<>();
    private ArrayList<DrawableCell<?>> nextGenCells = new ArrayList<>();


    public GameOfLifeGrid(int gridWidth, int gridHeight, int cellWidth, int cellHeight) {
        this.setGridWidth(gridWidth);
        this.setGridHeight(gridHeight);
        Dimension gridSize = new Dimension(gridWidth, gridHeight);
        setMinimumSize(gridSize);
        setPreferredSize(gridSize);
        this.setGridCellWidth(cellWidth);
        this.setGridCellHeight(cellHeight);
        drawableCells = new DrawableCell[(gridWidth / gridCellWidth) * (gridHeight / gridCellHeight)];
    }

    public void init() {
        GridMouseListener<GameOfLifeGrid> mMotionListener = new GridMouseListener<>(this);
        addMouseListener(mMotionListener);
        addMouseMotionListener(mMotionListener);

        for (int i = 0; i < drawableCells.length; i++) {
            drawableCells[i] = new DrawableCell(new Cell() {
                @Override
                public boolean calculateNextGenState() {
                    boolean isAlive = isAlive();
                    boolean nextGenState = getNextGenState();
                    int liveNeighbours = getAliveNeighbours();

                    if (!isAlive && liveNeighbours == 3) {
                        this.setNextGen(true);
                    } else if (isAlive) {
                        if (liveNeighbours < 2) {
                            this.setNextGen(false);
                        } else if (liveNeighbours > 3) {
                            this.setNextGen(false);
                        }
                    }
                    return nextGenState != getNextGenState();
                }

            }, this.gridCellWidth, this.gridCellHeight);
        }
    }

    public void drawGrid(Graphics g) {
        g.setColor(Color.gray);

        for (int y = 0; y < gridHeight / gridCellHeight; y++) {
            for (int x = 0; x < gridWidth / gridCellWidth; x++) {
                DrawableCell c = getDrawableCell(x, y);
                c.setPosX(x);
                c.setPosY(y);
                c.draw(g, (x * gridCellWidth + xOffset) * scale, (y * gridCellHeight + yOffset) * scale);
            }
        }
    }

    public void update(DrawableCell<?> cell) {
        if (!cell.getCell().hasNeighboursCached())
            cell.getCell().cacheNeighbours(getNeighbouringCells(cell));

        if (cell.getCell().calculateNextGenState()) {
            nextGenCells.add(cell);
            Arrays.stream(cell.getCell().getCachedNeighbours()).forEach(c -> update(c));
        }
    }

    public void start() {
        for (DrawableCell<?> cell : gridSeeds) {
            update(cell);
        }
        System.out.println(nextGenCells.size());
        nextGenCells.stream().forEach(c -> c.getCell().setIsAlive(c.getCell().getNextGenState()));
        nextGenCells.clear();
    }

    public boolean containsSeed(DrawableCell<?> cell) {
        return gridSeeds.contains(cell);
    }

    public DrawableCell[] getNeighbouringCells(DrawableCell<?> cell) {
        DrawableCell<?>[] neighbourCells = new DrawableCell[8];
        for (int i = 0; i < Cell.neighbouringCells.length; i++) {
            neighbourCells[i] = getDrawableCell(cell.getPosX() + Cell.neighbouringCells[i][0],
                    cell.getPosY() + Cell.neighbouringCells[i][1]);
        }
        return neighbourCells;
    }

    public int getGridHeight() {
        return gridHeight;
    }

    public void setGridHeight(int gridHeight) {
        this.gridHeight = gridHeight;
    }

    public int getGridWidth() {
        return gridWidth;
    }

    public void setGridWidth(int gridWidth) {
        this.gridWidth = gridWidth;
    }

    public Cell getCellAtMouseCoords(int mouseX, int mouseY) {
        return getCell(mouseX / gridCellWidth, mouseY / gridCellHeight);
    }

    public DrawableCell<Cell> getDrawableCellAtMouseCoords(int mouseX, int mouseY) {
        return getDrawableCell(mouseX / gridCellWidth, mouseY / gridCellHeight);
    }

    public DrawableCell<Cell> getDrawableCell(int x, int y) {
        return drawableCells[(y * (gridWidth / gridCellWidth)) + x];
    }

    public Cell getCell(int x, int y) {
        return drawableCells[(y * gridWidth / gridCellWidth) + x].getCell();
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

    @Override
    public void accept(KeyEvent k) {
        switch (k.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                setxOffset(xOffset + gridCellWidth);
                break;
            case KeyEvent.VK_RIGHT:
                setxOffset(xOffset - gridCellWidth);
                break;
            case KeyEvent.VK_UP:
                setyOffset(yOffset + gridCellHeight);
                break;
            case KeyEvent.VK_DOWN:
                setyOffset(yOffset - gridCellHeight);
                break;
        }
    }

    public void setGridCellWidth(int gridCellWidth) {
        this.gridCellWidth = gridCellWidth;
    }

    public void setGridCellHeight(int gridCellHeight) {
        this.gridCellHeight = gridCellHeight;
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

    public void setGridSeeds(ArrayList<DrawableCell<?>> gridSeeds) {
        this.gridSeeds = gridSeeds;
    }

    public void setNextGenCells(ArrayList<DrawableCell<?>> nextGenCells) {
        this.nextGenCells = nextGenCells;
    }
}
