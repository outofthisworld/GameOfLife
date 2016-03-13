package game.listeners;

import game.GameOfLifeGrid;
import objects.drawable.DrawableCell;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Optional;

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
        if (e.getX() > gameOfLifeGrid.getWidth() || e.getY() > gameOfLifeGrid.getHeight())
            return;

        mouseIsDown = true;
        Optional<DrawableCell<?>> c = gameOfLifeGrid.getDrawableCellAtMouseCoords((e.getX()), (e.getY()));
        if (c.isPresent()) {
            c.get().getCell().toggleIsAlive();
            c.get().getCell().setNextGenState(c.get().getCell().isAlive());
            if (c.get().getCell().isAlive()) {
                gameOfLifeGrid.addSeed(c.get());
            } else {
                gameOfLifeGrid.removeSeed(c.get());
            }
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

        Optional<DrawableCell<?>> c = gameOfLifeGrid.getDrawableCellAtMouseCoords((e.getX()), e.getY());
        if (!c.isPresent())
            return;

        c.get().getCell().setIsAlive(true);
        c.get().getCell().setNextGenState(true);

        if (!gameOfLifeGrid.containsSeed(c.get()))
            gameOfLifeGrid.addSeed(c.get());
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

}
