package test;

import org.junit.Test;

import unsw.dungeon.*;
// Test that the boulder moves as intended and correctly triggers and untriggers floor switches
public class testBoulderAndSwitches {
    
    @Test
    // Test Boulder movement without walls
    public void testMovement() {
        Dungeon dungeon = new Dungeon(5, 5);
        Boulder boulder = new Boulder(dungeon, 2, 2);
        dungeon.addEntity(boulder);
        // move boulder down
        boulder.moveTo(2, 3);
        assert(boulder.getX() == 2 && boulder.getY() == 3);
        // move bounder right
        boulder.moveTo(3,3);
        assert(boulder.getX() == 3 && boulder.getY() == 3);
        
    }

    @Test
    // Test boulder movement against barriers such as walls and other boulders
    public void testBarriers() {
        // Spawn a boulder and another boulder to the right of it.
        Dungeon dungeon = new Dungeon(5, 5);
        Boulder boulderMove = new Boulder(dungeon, 2, 2);
        Boulder boulderWall = new Boulder(dungeon, 3, 2);
        dungeon.addEntity(boulderMove);
        dungeon.addEntity(boulderWall);
        // Spawn a wall to the left of the first boulder.
        Wall wall = new Wall(1, 2);
        dungeon.addEntity(wall);
        // Move boulder right (test collision against another boulder)
        boulderMove.moveTo(3, 2);
        // boulder should not move
        assert(boulderMove.getX() == 2 && boulderMove.getY() == 2);
        boulderMove.moveTo(1, 2);
        // boulder should not move
        assert(boulderMove.getX() == 2 && boulderMove.getY() == 2);
    }

    @Test
    // Test boulder interacting with floor switches
    public void testFloorSwitch() {
        Dungeon dungeon = new Dungeon(5, 5);

        Boulder boulder = new Boulder(dungeon, 2, 2);
        dungeon.addEntity(boulder);

        FloorSwitch floorSwitch = new FloorSwitch(dungeon, 2, 2);
        dungeon.addEntity(floorSwitch);

        // Boulder and floor switch occupy the same coordinates
        assert(floorSwitch.isTriggered());
        //Move boulder left
        boulder.moveTo(3, 2);
        assert(!floorSwitch.isTriggered());
        //Move boulder right (back to original position)
        boulder.moveTo(2, 2);
        assert(floorSwitch.isTriggered());
        
    }
}