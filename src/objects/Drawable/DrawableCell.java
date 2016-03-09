package objects.Drawable;

import objects.Cell;

import java.awt.*;

/**
 * Created by Dale on 8/03/16.
 */
public class DrawableCell<T extends Cell> extends Rectangle implements IDrawable<Graphics> {
    private final T cell;
    private int x;
    private int y;
    private int id;

    public DrawableCell(T cell, int id, int width, int height) {
        super(width, height);
        this.id = id;
        this.cell = cell;
    }

    public T getCell() {
        return cell;
    }

    public int getPosX() {
        return x;
    }

    public void setPosX(int x) {
        this.x = x;
    }

    public int getPosY() {
        return y;
    }

    public void setPosY(int y) {
        this.y = y;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof DrawableCell))
            return false;

        return ((DrawableCell<?>) obj).getCellID() == this.getCellID();
    }

    public int getCellID() {
        return id;
    }

    @Override
    public int hashCode() {
        return getCellID();
    }

    @Override
    public void draw(Graphics g, double xPos, double yPos) {
        if (getCell().isAlive()) {
            g.fillRect((int) xPos, (int) yPos, width, height);
        } else {
            g.clearRect((int) xPos, (int) yPos, width, height);
            g.drawRect((int) xPos, (int) yPos, width, height);
        }
    }
}
