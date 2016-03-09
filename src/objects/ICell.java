package objects;

/**
 * Created by Dale on 9/03/16.
 */
public interface ICell {
    boolean isAlive();

    void setIsAlive(boolean b);

    void calculateNextGenState();
}
