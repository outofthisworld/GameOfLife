package objects;

import objects.Drawable.DrawableCell;

import java.util.Arrays;

/**
 * Created by Dale on 8/03/16.
 */
public abstract class Cell implements ICell {
    public static final int[][] neighbouringCells = {
            {-1, -1},
            {-1, 0},
            {-1, 1},
            {0, 1},
            {0, -1},
            {1, 1},
            {1, 0},
            {1, -1}};

    private boolean isAlive = false;
    private boolean nextGenState = false;
    private DrawableCell<?>[] cachedNeighbours = null;

    public Cell() {

    }

    public void setIsAlive(boolean isAlive) {
        this.isAlive = isAlive;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public int getAliveNeighbours() {
        if (cachedNeighbours == null) return 0;
        return (int) Arrays.stream(cachedNeighbours).filter(cell -> cell.getCell().isAlive()).count();
    }

    @Override
    public int getDeadNeighbours() {
        if (cachedNeighbours == null) return 0;
        return (int) Arrays.stream(cachedNeighbours).filter(cell -> !cell.getCell().isAlive()).count();
    }

    public boolean getNextGenState() {
        return nextGenState;
    }

    @Override
    public boolean hasNeighboursCached() {
        return cachedNeighbours != null;
    }

    @Override
    public void cacheNeighbours(DrawableCell[] cells) {
        cachedNeighbours = cells;
    }

    public void toggleIsAlive() {
        isAlive = isAlive ? false : true;
    }

    @Override
    public void clearNeighbourCache() {
        Arrays.stream(cachedNeighbours).forEach(c -> c = null);
    }

    public DrawableCell<?>[] getCachedNeighbours() {
        return cachedNeighbours;
    }

    @Override
    public void setNextGen(boolean b) {
        this.nextGenState = b;
    }
}
