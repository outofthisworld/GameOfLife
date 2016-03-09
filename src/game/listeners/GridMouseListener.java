package game.listeners;

import game.GameOfLifeGrid;
import objects.Drawable.DrawableCell;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 * Created by Dale on 9/03/16.
 */
public class GridMouseListener<T extends GameOfLifeGrid> implements MouseListener, MouseMotionListener {
    private T gameOfLifeGrid;
    private boolean mouseIsDown = false;

    public GridMouseListener(T grid) {
        this.gameOfLifeGrid = grid;
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        mouseIsDown = true;
        DrawableCell c = gameOfLifeGrid.getDrawableCellAtMouseCoords(e.getX(), e.getY());
        c.getCell().toggleIsAlive();
        if (c.getCell().isAlive()) {
            gameOfLifeGrid.addSeed(c);
        } else {
            gameOfLifeGrid.removeSeed(c);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        mouseIsDown = false;
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (!mouseIsDown)
            return;

        DrawableCell c = gameOfLifeGrid.getDrawableCellAtMouseCoords(e.getX(), e.getY());
        c.getCell().setIsAlive(true);
        if (!gameOfLifeGrid.containsSeed(c))
            gameOfLifeGrid.addSeed(c);
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

}
