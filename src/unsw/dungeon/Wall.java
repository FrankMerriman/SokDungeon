package unsw.dungeon;

public class Wall extends Entity{

    public Wall(int x, int y) {
        super(x, y);
    }

    @Override
    public boolean isBarrier(Entity e) {
        return true;
    }
}
