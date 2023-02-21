package unsw.dungeon;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 * An entity in the dungeon.
 * @author Robert Clifton-Everest, Frank Merriman, The Tran
 *
 */
public class Entity {

    // IntegerProperty is used so that changes to the entities position can be
    // externally observed.
    private IntegerProperty x, y;

    private BooleanProperty displayOnScreen;

    /**
     * Create an entity positioned in square (x,y)
     * @param x x axis position
     * @param y y axis position
     */
    public Entity(int x, int y) {
        this.x = new SimpleIntegerProperty(x);
        this.y = new SimpleIntegerProperty(y);
        this.displayOnScreen = new SimpleBooleanProperty(true);
    }

    public BooleanProperty displayOnScreen() {
        return displayOnScreen;
    }

    public boolean getDisplayOnScreen() {
        return displayOnScreen().get();
    }

    public void setDisplayOnScreen(boolean state) {
        displayOnScreen().set(state);
    }

    public IntegerProperty x() {
        return x;
    }

    public IntegerProperty y() {
        return y;
    }

    public int getY() {
        return y().get();
    }

    public int getX() {
        return x().get();
    }

    public void setX(int x) {
        x().set(x);
    }

    public void setY(int y) {
        y().set(y);
    }

    /**
     * This method returns a boolean as to whether an entity
     * can occupy the same tile as this.
     * @param e Entity that is trying to enter this objects space in the game
     * @return false if not overridden to be true by another method
     */
    public boolean isBarrier(Entity e) {
        return false;
    }

    /**
     * This method performs behaviour specific to
     * certain entity interactions via TypeCasting e and then
     * performing other methods depending on what this is versus e
     * @param e Entity colliding with this Entity
     */
    public void onCollide(Entity e) {
        
    }


}
