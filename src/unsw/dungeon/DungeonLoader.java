package unsw.dungeon;

import java.io.FileNotFoundException;
import java.io.FileReader;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.List;

/**
 * Loads a dungeon from a .json file.
 *
 * By extending this class, a subclass can hook into entity creation. This is
 * useful for creating UI elements with corresponding entities.
 *
 * @author Robert Clifton-Everest, Frank Merriman, The Tran
 *
 */
public abstract class DungeonLoader {

    private JSONObject json;

    public DungeonLoader(String filename) throws FileNotFoundException {
        json = new JSONObject(new JSONTokener(new FileReader("dungeons/" + filename)));
    }

    /**
     * Parses the JSON to create a dungeon.
     * @return dungeon that is created from json data
     */
    public Dungeon load() {
        int width = json.getInt("width");
        int height = json.getInt("height");

        Dungeon dungeon = new Dungeon(width, height);

        //Adds entities to dungeon
        JSONArray jsonEntities = json.getJSONArray("entities");
        for (int i = 0; i < jsonEntities.length(); i++) {
            loadEntity(dungeon, jsonEntities.getJSONObject(i));
        }
        
        //Creates a goal tree and adds to dungeon
        JSONObject jsonGoals = json.getJSONObject("goal-condition");
        dungeon.addGoal(loadGoal(dungeon, jsonGoals, null));

        //Initialise any enemies to track player
        loadEnemies(dungeon.getAllEntities(), dungeon.getPlayer());

        return dungeon;
    }

    /**
     * Reads a json object, creates corresponding entity and adds it to dungeons
     * list of entities
     * @param dungeon that is being inserted into
     * @param json that is being parsed to create entities
     */
    private void loadEntity(Dungeon dungeon, JSONObject json) {
        String type = json.getString("type");
        int x = json.getInt("x");
        int y = json.getInt("y");

        Entity entity = null;
        switch (type) {
        case "player":
            Player player = new Player(dungeon, x, y);
            dungeon.setPlayer(player);
            onLoad(player);
            entity = player;
            break;

        case "wall":
            Wall wall = new Wall(x, y);
            onLoad(wall);
            entity = wall;
            break;

        case "exit":
            Exit exit = new Exit(x, y);
            exit.attach(dungeon);
            onLoad(exit);
            entity = exit;
            break;

        case "treasure":
            Treasure treasure = new Treasure(x, y);
            treasure.attach(dungeon);
            onLoad(treasure);
            entity = treasure;
            break;

        case "door":
            int id = json.getInt("id");
            Door door = new Door(x, y, id);
            onLoad(door);
            entity = door;
            break;

        case "key":
            int id2 = json.getInt("id");
            Key key = new Key(x, y, id2);
            onLoad(key);
            entity = key;
            break;

        case "boulder":
            Boulder boulder = new Boulder(dungeon, x, y);
            onLoad(boulder);
            entity = boulder;
            break;

        case "switch":
            FloorSwitch floorSwitch = new FloorSwitch(dungeon, x, y);
            floorSwitch.attach(dungeon);
            onLoad(floorSwitch);
            entity = floorSwitch;
            break;

        case "portal":
            int id3 = json.getInt("id");
            Portal portal = new Portal(dungeon, x, y, id3);
            onLoad(portal);
            entity = portal;
            break;

        case "enemy":
            Hunter hunter = new Hunter(dungeon, x, y);
            hunter.attach(dungeon);
            onLoad(hunter);
            entity = hunter;
            break;

        case "sword":
            Sword sword = new Sword(x, y);
            onLoad(sword);
            entity = sword;
            break;

        case "invincibility":
            Potion potion = new Potion(x, y);
            onLoad(potion);
            entity = potion;
            break;

        case "pickaxe":
            Pickaxe pickaxe = new Pickaxe(x,y);
            onLoad(pickaxe);
            entity = pickaxe;
            break;
        
        case "water":
            Water water = new Water(dungeon, x, y);
            onLoad(water);
            entity = water;
            break;
        case "bomb":
            Bomb bomb = new Bomb(dungeon, x, y);
            onLoad(bomb);
            entity = bomb;
        }

        dungeon.addEntity(entity);
    }

    /**
     * Recursively builds a tree of goals using the composite Goal pattern
     * Goals are decided by parsing JSONObject data
     * @param dungeon to add goal to
     * @param json data to be read
     * @return the Goal created on current pass of the method
     */
    private Goal loadGoal (Dungeon dungeon, JSONObject json, Goal parentGoal) {

        String goal = json.getString("goal");

        if (goal.equals("AND")) {
            GoalAND newGoal = new GoalAND();
            JSONArray subgoals = json.getJSONArray("subgoals");

            for (int i = 0; i < subgoals.length(); i++) {
                newGoal.addSubGoal(loadGoal(dungeon, subgoals.getJSONObject(i), newGoal));
            }
            return newGoal;

        } else if (goal.equals("OR")) {
            GoalOR newGoal = new GoalOR();
            JSONArray subgoals = json.getJSONArray("subgoals");

            for (int i = 0; i < subgoals.length(); i++) {
                newGoal.addSubGoal(loadGoal(dungeon, subgoals.getJSONObject(i), newGoal));
            }
            return newGoal;

        } else if (goal.equals("exit")) { //Only allows 1 exit as a goal (per layer of subgoals)
            Goal newGoal = null;
            List<Entity> entities = dungeon.getAllEntities();
            for (Entity e : entities) {
                if (e instanceof Exit) {
                    //special case that exits must be completed last
                    if (parentGoal != null && parentGoal instanceof GoalAND) {
                        GoalAND parent = (GoalAND) parentGoal;
                        Exit exit = (Exit) e;
                        exit.setParentAND(parent);
                    }

                    return newGoal = (Goal) e;
                }
            }
            return newGoal;

        } else if (goal.equals("enemies")) {
            GoalAND newGoal = new GoalAND();
            List<Entity> entities = dungeon.getAllEntities();
            for (Entity e : entities) {
                if (e instanceof Hunter) {
                    newGoal.addSubGoal( (Goal) e);
                }
            }
            return newGoal;

        } else if (goal.equals("treasure")) {
            GoalAND newGoal = new GoalAND();
            List<Entity> entities = dungeon.getAllEntities();
            for (Entity e : entities) {
                if (e instanceof Treasure) {
                    newGoal.addSubGoal( (Goal) e);
                }
            }
            return newGoal;

        } else if (goal.equals("boulders")) {
            GoalAND newGoal = new GoalAND();
            List<Entity> entities = dungeon.getAllEntities();
            for (Entity e : entities) {
                if (e instanceof FloorSwitch) {
                    newGoal.addSubGoal( (Goal) e);
                }
            }
            return newGoal;
        }

        return null;
    }


    /**
     * Sets every Hunter in dungeon to observe player
     * which implements subject
     * @param entities the potential observers
     * @param player the subject
     */
    private void loadEnemies(List<Entity> entities, Player player) {
        if (player == null) {
            return;
        }
        for (Entity e : entities) {
            if (e instanceof Hunter) {
                player.attach( (Hunter) e);
            }
        }
    }

    public abstract void onLoad(Player player); // This used to be Entity player change back if breaks

    public abstract void onLoad(Wall wall);

    public abstract void onLoad(Exit exit);

    public abstract void onLoad(Treasure treasure);

    public abstract void onLoad(Door door);

    public abstract void onLoad(Key key);

    public abstract void onLoad(Boulder boulder);

    public abstract void onLoad(FloorSwitch floorSwitch);

    public abstract void onLoad (Portal portal);

    public abstract void onLoad(Hunter hunter);

    public abstract void onLoad(Sword sword);

    public abstract void onLoad(Potion potion);

    public abstract void onLoad(Pickaxe pickaxe);

    public abstract void onLoad(Water water);

    public abstract void onLoad(Bomb bom);
}
