package unsw.dungeon;


/**
 * A entity that blocks all entities from passing
 * @author Frank Merriman, The Tran
 */
public class Wall extends Entity{

    /**
     * Creates a new wall at given location
     * @param x axis location
     * @param y axis location
     */
    public Wall(int x, int y) {
        super(x, y);
    }

    @Override
    public boolean isBarrier(Entity e) {
        if (e instanceof Player) {
            Player player = (Player) e;
            if (player.getPickaxe() != null) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onCollide(Entity e) {
        if (e instanceof Player) {
            Player player = (Player) e;
            playerCollision(player);
        }
    }

    private void playerCollision(Player player) {
        Pickaxe playerPickaxe = player.getPickaxe();
        if (playerPickaxe != null) {
            playerPickaxe.useWeapon();
            if (playerPickaxe.isBroken()) {
                player.unequip(playerPickaxe);
            }
        }
        player.removeWall(this);
        setDisplayOnScreen(false);
    }
}
