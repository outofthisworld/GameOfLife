package game;

import game.interfaces.IGameOfLife;
import game.interfaces.Renderable;
import game.listeners.GOLKeyPressListener;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Dale on 8/03/16.
 */
public class GameOfLifeView implements IGameOfLife, Runnable {
    //The amount of time to wait between game updates.
    public static final long UPDATE_FPS = 1000L / 30L;
    private static final GameOfLifeView gol = new GameOfLifeView("Game of life",
            new Dimension(800, 700));
    //JComponents
    private final JFrame gameFrame;
    private final GameOfLifeGrid gameOfLifePanel = new GameOfLifeGrid(800, 600, 10, 10);
    //Game dimensions
    private final Dimension gameDimension;
    private final double frameWidth;
    private final double frameHeight;
    //Listeners
    private final GOLKeyPressListener golKeyPressListener = new GOLKeyPressListener();
    //Items to render
    private final Renderable<Graphics>[] renderableItems = new Renderable[]{gameOfLifePanel};

    //Instance initializer
    {
        golKeyPressListener.addListener(gameOfLifePanel);
    }

    //Class constructor
    private GameOfLifeView(String gameTitle, Dimension d) {
        gameFrame = new JFrame(gameTitle);
        Button b = new Button("start");
        b.setBounds(400 - 70, 650, 80, 30);
        b.addActionListener(e -> gameOfLifePanel.start());
        gameFrame.add(b);
        frameWidth = d.getWidth();
        frameHeight = d.getHeight();
        gameDimension = d;
        gameFrame.setSize(d);
        gameFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    /**
     * Get the singleton instance game of life view.
     *
     * @return the game of life view
     */
    public static GameOfLifeView _getInstance() {
        return gol;
    }

    /**
     * Main.
     *
     * @param args the args
     */
    public static void main(String[] args) {
        GameOfLifeView._getInstance().init();
    }

    @Override
    public void init() {
        gameOfLifePanel.init();
        gameOfLifePanel.setSize(gameDimension);
        gameFrame.add(gameOfLifePanel);
        gameFrame.setVisible(true);
        gameFrame.addKeyListener(golKeyPressListener);
        new Thread(GameOfLifeView._getInstance()).start();
    }

    @Override
    public void stop() {
        try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.exit(0);
    }

    @Override
    public void nextGeneration() {

    }

    /**
     * Render game.
     *
     * @param <T>      the type parameter
     * @param graphics the graphics
     */
    public <T extends Graphics> void renderGame(T graphics) {
        for (Renderable<Graphics> obj : renderableItems) {
            obj.render(graphics);
        }
    }

    /**
     * Gets frame width.
     *
     * @return the frame width
     */
    public double getFrameWidth() {
        return frameWidth;
    }

    /**
     * Gets frame height.
     *
     * @return the frame height
     */
    public double getFrameHeight() {
        return frameHeight;
    }

    /**
     * Gets game dimension.
     *
     * @return the game dimension
     */
    public Dimension getGameDimension() {
        return gameDimension;
    }

    /**
     * Gets game of life panel.
     *
     * @return the game of life panel
     */
    public GameOfLifeGrid getGameOfLifePanel() {
        return gameOfLifePanel;
    }

    @Override
    public void run() {
        long loopStartTime = System.currentTimeMillis();
        while (true) {
            if ((System.currentTimeMillis() - loopStartTime) >= UPDATE_FPS) {
                loopStartTime = System.currentTimeMillis();
                renderGame(gameOfLifePanel.getGraphics());
                nextGeneration();
            }
        }
    }
}
