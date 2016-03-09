package objects;

import objects.Drawable.DrawableCell;

/**
 * Created by Dale on 9/03/16.
 */
public interface ICell {
    boolean isAlive();

    void setIsAlive(boolean b);

    void setNextGen(boolean b);

    boolean calculateNextGenState();

    boolean hasNeighboursCached();

    void cacheNeighbours(DrawableCell[] cells);

    void clearNeighbourCache();

    int getAliveNeighbours();

    int getDeadNeighbours();
}
