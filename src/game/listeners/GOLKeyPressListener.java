package game.listeners;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

/**
 * Created by Dale on 9/03/16.
 */
public class GOLKeyPressListener implements KeyListener {
    private final ArrayList<IGOLKeyPressListener> al = new ArrayList<>();

    public void addListener(IGOLKeyPressListener keyPressListener) {
        al.add(keyPressListener);
    }

    public void notifyAllListeners(KeyEvent k) {
        for (IGOLKeyPressListener listener : al) {
            listener.accept(k);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        notifyAllListeners(e);
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
