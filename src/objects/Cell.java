package objects;

/**
 * Created by Dale on 8/03/16.
 */
public abstract class Cell implements ICell {
    private boolean isAlive = false;
    private boolean nextGenState = false;

    public Cell() {
    }

    @Override
    public void setIsAlive(boolean isAlive) {
        this.isAlive = isAlive;
    }

    @Override
    public boolean isAlive() {
        return isAlive;
    }

    public boolean getNextGenState() {
        return nextGenState;
    }

    public void setNextGenState(boolean b) {
        this.nextGenState = b;
    }

    public boolean goNextGen() {
        setIsAlive(getNextGenState());
        return isAlive();
    }

    public void toggleIsAlive() {
        isAlive = isAlive ? false : true;
    }
}
