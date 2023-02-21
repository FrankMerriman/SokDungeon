package unsw.dungeon;

import java.util.ArrayList;
import java.util.List;

/**
 * The player entity
 * @author Robert Clifton-Everest, Frank Merriman, The Tran
 *
 */
public class Player extends Entity implements MoveBehaviour, Subject{

    private List<Observer> listObservers;
    private List<Entity> inventory;
    private Dungeon dungeon;
    private int prevX;
    private int prevY;

    /**
     * Create a player positioned in square (x,y)
     * @param x
     * @param y
     */
    public Player(Dungeon dungeon, int x, int y) {
        super(x, y);
        this.dungeon = dungeon;
        this.listObservers = new ArrayList<>();
        this.inventory = new ArrayList<>();
    }

    public void moveUp() {
        if (getY() > 0 )
            moveTo(getX(), getY() - 1);
    }

    public void moveDown() {
        if (getY() < dungeon.getHeight() - 1)
            moveTo(getX(), getY() + 1);
    }

    public void moveLeft() {
        if (getX() > 0 )
            moveTo(getX() - 1, getY());
    }

    public void moveRight() {
        if (getX() < dungeon.getWidth() - 1)
            moveTo(getX() + 1, getY());
    }

    /**
     * This method takes in an x and y coordinate and attempts to move the player into it, checking collide behaviour first
     * @param x cooridnate of tile we are moving to
     * @param y cooridnate of tile we are moving to
     */
    public void moveTo (int newX, int newY) {
        if (canMove(newX, newY)) {
            setPrevX(getX());
            setPrevY(getY());
            x().set(newX);
            y().set(newY);
            collide(newX, newY);
            updateObservers();
        }
    }

    public void plantBomb() {
        Bomb bomb = getBomb();
        unequip(bomb);
        bomb.ignite();
        bomb.plant(getX(), getY());
    }

    /**
     * This method takes in an x and y coordinate and then checks the dungeon to see if the player can move
     * into said coordinates
     * @param x cooridnate of tile we are checking
     * @param y cooridnate of tile we are checking
     * @return true if eligible location for player movement
     */
    public boolean canMove(int x, int y) {

        //if we do a state pattern for player we could have it observe this value to change to 'dead'
        if (dungeon.getlevelComplete() != 0) { //when true dungeon is complete so player cannot move
            return false;
        }

        List<Entity> tileEntities = dungeon.getEntities(x, y);

        if (tileEntities.size() < 1) { //Basically same as old code for entity = null
            return true;
        }
        
        for (Entity e : tileEntities) {
            if (e.isBarrier(this)) {
                return false;
            } else {
                //e.onCollide(this);
            }
        }
        return true;
        
    }

    /**
     * Runs the oncollide methods for any entity in the given coordinates
     * passing Player as the object
     * @param x axis location
     * @param y axis location
     */
    private void collide(int x, int y) {
        List<Entity> entities = dungeon.getEntities(x, y);
        for (Entity e : entities) {
            if (e != null) {
                e.onCollide(this);
            }
        }
    }

    public int getPrevX() {
        return prevX;
    }
    
    public int getPrevY() {
        return prevY;
    }

    private void setPrevX(int x) {
        prevX = x;
    }

    private void setPrevY(int y) {
        prevY = y;
    }
    /**
     * Computers the relative x direction of an entity from the player's
     * current x-coordinate.
     * @param e An entity to compute against
     * @return The relative x direction of the entity.
     */
    public int computeXDirection(Entity e) {
        return e.getX() - getX();
        
    }
    /**
     * Computers the relative y direction of an entity from the player's
     * current y-coordinate.
     * @param e An entity to compute against
     * @return The relative y direction of the entity.
     */
    public int computeYDirection(Entity e) {
        return e.getY() - getY();
        
    }
    /**
     * Computers the relative x direction of an entity from the player's
     * previous x-coordinate.
     * @param e An entity to compute against
     * @return The relative x direction of the entity.
     */
    public int computePrevXDirection(Entity e) {
        return e.getX() - getPrevX();
        
    }
    /**
     * Computers the relative y direction of an entity from the player's
     * previous y-coordinate.
     * @param e An entity to compute against
     * @return The relative y direction of the entity.
     */
    public int computePrevYDirection(Entity e) {
        return e.getY() - getPrevY();
        
    }

    /**
     * Add a given entity to players inventory. Set
     * up entity to observe player if it is of type Potion
     * @param e entity being added
     */
    public void equip(Entity e) {
        inventory.add(e);
        //if e is a potion its also an observer to player movement
        if (e instanceof Potion) {
            attach( (Potion) e);
        } else if (e instanceof Bomb) {
            attach( (Bomb) e);
        }
        dungeon.removeEntity(e);
    }

    /**
     * Remove a given entity from players inventory
     * @param e entity to be removed
     */
    public void unequip(Entity e) {
        inventory.remove(e);
    }

    /**
     * Method that checks players inventory for a sword
     * @return sword if player has a sword inventory else null
     */
    public Sword getSword() {
        for (Entity e : inventory) {
            if (e instanceof Sword) {
                return (Sword) e;
            }
        }
        return null;
    }

    public Pickaxe getPickaxe() {
        for (Entity e: inventory) {
            if (e instanceof Pickaxe) {
                return (Pickaxe) e;
            }
        }
        return null;
    }

    public Key getKey() {
        for (Entity e: inventory) {
            if (e instanceof Key) {
                return (Key) e;
            }
        }
        return null;
    }

    public Bomb getBomb() {
        for (Entity e : inventory) {
            if (e instanceof Bomb) {
                return (Bomb) e;
            }
        }
        return null;
    }

    public void removeWall(Wall wall) {
        dungeon.removeEntity(wall);
    }

    /**
     * Method that checks players inventory for a potion
     * @return potion if one else null
     */
    public Potion getPotion() {
        for (Entity e : inventory) {
            if (e instanceof Potion) {
                return (Potion) e;
            }
        }
        return null;
    }

    /**
     * Remove an observer from a subject.
     * @param o An observer to remove
     */
    @Override
    public void detach(Observer o) {
        listObservers.remove(o);
    }

    /**
     * Add an observer to the subject.
     * @param o An observer to add
     */
    @Override
    public void attach(Observer o) {
        listObservers.add(o);
    }

    /**
     * Notify all observers that are attached to subject
     */
    @Override
    public void updateObservers() {
        Bomb bomb = null;
        for (Observer o : listObservers) {
            o.update(this);
            if (o instanceof Bomb) {
                bomb = (Bomb) o;
            }
        }
        detachBomb(bomb);
    }

    private void detachBomb(Bomb bomb) {
        if (bomb != null) {
            if (bomb.hasDetonated()) {
                detach(bomb);
            }
        }
    }

    /**
     * Checks if a player is equipped to fight and reduces durability on sword
     * if it is only option
     * @param hunter enemy the player is fighting
     * @return true if player has an item that can be used to fight
     */
    public boolean canFight(Hunter hunter) {
        Sword playerSword = getSword();

        if (getPotion() != null) {
            return true;
        } else if (playerSword != null) {
            playerSword.useWeapon();
            if (playerSword.isBroken()) {
                unequip(playerSword);
            }
            return true;
        }

        return false;
    }

    @Override
    public void onCollide(Entity e) {
        if (e instanceof Hunter) {
            Hunter h = (Hunter) e;
            if(this.canFight(h)) {
                dungeon.removeEntity(h);
                h.defeat();
                h.setDisplayOnScreen(false);
            } else {
                dungeon.removeEntity(this);
                setDisplayOnScreen(false);
                dungeon.setlevelComplete(2); //level failed
            }
        }
    }

    public List<Entity> getInventory() {
        return inventory;
    }
}
