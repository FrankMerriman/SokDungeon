package unsw.dungeon;

import java.util.List;
import java.lang.Math;

public class Hunter extends Entity implements MoveBehaviour, Observer{

    private Dungeon dungeon;

    public Hunter(Dungeon dungeon, int x, int y) {
        super(x, y);
        this.dungeon = dungeon;
    }

    @Override
    public void update(Object obj) {
        if (obj instanceof Player) {
            update( (Player) obj);
        }
    }

    //Make sure that code that auto attaches enemy observers to Player does so after all entities added from json (maybe when adding goals?)
    public void update(Player player) {
        int targetX = player.getX();
        int targetY = player.getY();
        boolean fearPlayer = player.isInvincible();
        findClosestPath(targetX, targetY, fearPlayer);
    }

    /**
     * Given a target coordinate it will attempt to move in a straight line towards it
     * It will always move along the axis that has the smallest difference between target and current
     * If both are the same it prefers moving horizontally (even if both diff are 0)
     * The enemy has reverse behaviour when the player is under potion influence
     * @param targetX int giving location on x axis
     * @param targetY int giving location on y axis
     */
    public void findClosestPath(int targetX, int targetY, boolean fearPlayer) {
        int currX = getX();
        int currY = getY();

        int xDiff = Math.abs(targetX - currX);
        int yDiff = Math.abs(targetY - currY);

        // Moves away if fearPlayer is true
        if (fearPlayer == false) {
            if (yDiff == 0 || xDiff <= yDiff) {
                moveHorizontal(currX, currY, targetX, targetY);
            } else {
                moveVertical(currX, currY, targetX, targetY);
            }
        } else {
            if (xDiff > 0 && xDiff <= yDiff) {
                moveHorizontal(targetX, targetY, currX, currY);
            } else {
                moveVertical(targetX, targetY, currX, currY);

            }
        }

    }
    //dont check yet if target is within dngeon constraints
    public void moveHorizontal(int currX, int currY, int targetX, int targetY) {
        if (targetX < currX) {
            moveTo(currX - 1, currY);
        } else {
            moveTo(currX + 1, currY);
        }
    }

    public void moveVertical(int currX, int currY, int targetX, int targetY) {
        if (targetY < currY) {
            moveTo(currX, currY - 1);
        } else {
            moveTo(currX, currY + 1);
        }
    }

    @Override
    public void moveTo(int x, int y) {
        if (canMove(x, y)) {
            x().set(x);
            y().set(y);
        }
        
    }

    @Override
    public boolean canMove(int x, int y) {
        List<Entity> tileEntities = dungeon.getEntities(x, y);

        if (tileEntities.size() < 1) {
            return true;
        }
        
        for (Entity e : tileEntities) {
            if (e.isBarrier(this)) {
                return false;
            } else {
                e.onCollide(this);
            }
        }
        
        return true;
    }

    @Override
    public void onCollide(Entity e) {
        if (e instanceof Player) {
            Player p = (Player) e;

            if(p.canFight(this)) {
                dungeon.removeEntity(this);
            } else {
                dungeon.removeEntity(p);
            }

        }
    }

}