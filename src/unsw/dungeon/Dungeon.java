
package unsw.dungeon;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 * A dungeon in the interactive dungeon player.
 *
 * A dungeon can contain many entities, each occupy a square. More than one
 * entity can occupy the same square.
 *
 * @author Robert Clifton-Everest
 *
 */
public class Dungeon implements Observer{

    
    private int width, height;
    private List<Entity> entities;
    private Player player;
    private Goal goal;
    private IntegerProperty levelComplete;

    public Dungeon(int width, int height) {
        this.width = width;
        this.height = height;
        this.entities = new ArrayList<>();
        this.player = null;
        this.goal = null;
        this.levelComplete = new SimpleIntegerProperty(0);
    }


    //IntegerPropert allows us to save multiple states
    // 0 - in progress
    // 1 - complete
    // 2 - failed
    public IntegerProperty levelComplete() {
        return levelComplete;
    }

    public int getlevelComplete() {
        return levelComplete().get();
    }

    public void setlevelComplete(int state) {
        levelComplete().set(state);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
    /**
     * 
     * @param entity
     */
    public void addEntity(Entity entity) {
        entities.add(entity);
    }

    public void removeEntity(Entity entity) {
        entities.remove(entity);
    }

    /**
     * Add a sub goal to dungeons GoalAND, This might need to be made into GoalOR
     * @param newGoal is a subGoal we wish to add to the dungeon. Only one subgoal being added is the json equivelant
     * of not using AND/OR
     */
    public void addGoal(Goal newGoal) {
        this.goal = newGoal;
    }

    public void checkGoalStatus() {
        if(goal.isComplete()){
            setlevelComplete(1);
        }
    }

    // //quick method for checking if a level is in completed mode
    // public boolean getCompletion() {
    //     return levelComplete.get();
    // }

    //method overloading on update
    @Override
    public void update(Object obj) {
        if (obj instanceof Goal) {
            update( (Goal) obj);
        }
    }

    public void update(Goal goal) {
        checkGoalStatus();
    }

    

    /**
     * Return all entities at the given coordinates in the dungeon
     * @param x
     * @param y
     * @return null if no entity at location otherwise a list of type entity
     */
    public List<Entity> getEntities(int x, int y) {
        List<Entity> tileEntities= new ArrayList<>();

        for (Entity e : entities) {
            if (e != null) {
                int eX = e.getX();
                int eY = e.getY();

                if (x == eX && y == eY) {
                    tileEntities.add(e);
                }
            }
        }

        return tileEntities;

    }

    public List<Entity> getAllEntities() {
        return entities;
    }

    public String getGoalString() {
        return goal.getGoalString(1);
    }
}


