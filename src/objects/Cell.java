package objects;

/**
 * Created by Dale on 8/03/16.
 */
public abstract class Cell implements ICell {
    private boolean isAlive = false;
    private boolean nextGenState = false;

    public Cell() {
    }

    public void setIsAlive(boolean isAlive) {
        this.isAlive = isAlive;
    }

    public boolean isAlive() {
        return isAlive;
    }


    public boolean getNextGenState() {
        return nextGenState;
    }


    public void toggleIsAlive() {
        isAlive = isAlive ? false : true;
    }

    @Override
    public void setNextGen(boolean b) {
        this.nextGenState = b;
    }
}
