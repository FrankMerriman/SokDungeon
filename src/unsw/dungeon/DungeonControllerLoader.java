package unsw.dungeon;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.io.File;

/**
 * A DungeonLoader that also creates the necessary ImageViews for the UI,
 * connects them via listeners to the model, and creates a controller.
 * @author Robert Clifton-Everest
 *
 */
public class DungeonControllerLoader extends DungeonLoader {

    private List<ImageView> stationaryEntities;
    private List<ImageView> movingEntities;

    //Images
    private Image playerImage;
    private Image wallImage;
    private Image exitImage;
    private Image boulderImage;
    private Image floorPlateImage;
    private Image hunterImage;
    private Image potionImage;
    private Image treasureImage;
    private Image closedDoorImage;
    private Image openDoorImage;
    private Image keyImage;
    private Image portalImage;
    private Image swordImage;
    private Image pickaxeImage;
    private Image waterImage;
    private Image bombImage;
    private Image bombImage1;
    private Image bombImage2;
    private Image bombImage3;
    private Image bombImage4;

    public DungeonControllerLoader(String filename)
            throws FileNotFoundException {
        super(filename);

        stationaryEntities = new ArrayList<>();
        movingEntities = new ArrayList<>();

        playerImage = new Image((new File("src/images/human_new.png")).toURI().toString());
        wallImage = new Image((new File("src/images/brick_brown_0.png")).toURI().toString());
        exitImage = new Image((new File("src/images/exit.png")).toURI().toString());
        boulderImage = new Image((new File("src/images/boulder.png")).toURI().toString());
        floorPlateImage = new Image((new File("src/images/pressure_plate.png")).toURI().toString());
        hunterImage = new Image((new File("src/images/deep_elf_master_archer.png")).toURI().toString());
        potionImage = new Image((new File("src/images/brilliant_blue_new.png")).toURI().toString());
        treasureImage = new Image((new File("src/images/gold_pile.png")).toURI().toString());
        closedDoorImage = new Image((new File("src/images/closed_door.png")).toURI().toString());
        openDoorImage = new Image((new File("src/images/open_door.png")).toURI().toString());
        keyImage = new Image((new File("src/images/key.png")).toURI().toString());
        portalImage = new Image((new File("src/images/portal.png")).toURI().toString());
        swordImage = new Image((new File("src/images/greatsword_1_new.png")).toURI().toString());
        pickaxeImage = new Image((new File("src/images/Pickaxe.png")).toURI().toString());
        waterImage = new Image((new File("src/images/water.png")).toURI().toString());
        bombImage = new Image((new File("src/images/bomb_unlit.png")).toURI().toString());
        bombImage1 = new Image((new File("src/images/bomb_lit_1.png")).toURI().toString());
        bombImage2 = new Image((new File("src/images/bomb_lit_2.png")).toURI().toString());
        bombImage3 = new Image((new File("src/images/bomb_lit_3.png")).toURI().toString());
        bombImage4 = new Image((new File("src/images/bomb_lit_4.png")).toURI().toString());
    }

    @Override
    public void onLoad(Player player) { //This was orignally public void onLoad(Enitity player) change back if it breaks
        ImageView view = new ImageView(playerImage);
        addEntity(player, view);
    }

    @Override
    public void onLoad(Wall wall) {
        ImageView view = new ImageView(wallImage);
        addEntity(wall, view);
    }

    @Override
    public void onLoad(Exit exit) {
        ImageView view = new ImageView(exitImage);
        addEntity(exit, view);
    }

    @Override
    public void onLoad(Treasure treasure) {
        ImageView view = new ImageView(treasureImage);
        addEntity(treasure, view);
    }

    @Override
    public void onLoad(Door door) {
        ImageView view = new ImageView(closedDoorImage);
        changeStateImage(door, view);
        //addItemIDHint(door, view);
        addEntity(door, view);
    }

    @Override
    public void onLoad(Key key) {
        ImageView view = new ImageView(keyImage);
        //addItemIDHint(key, view);
        addEntity(key, view);
    }

    @Override
    public void onLoad(Boulder boulder) {
        ImageView view = new ImageView(boulderImage);
        addEntity(boulder, view);
    }

    @Override
    public void onLoad(FloorSwitch floorSwitch) {
        ImageView view = new ImageView(floorPlateImage);
        addEntity(floorSwitch, view);
    }

    @Override
    public void onLoad(Portal portal) {
        ImageView view = new ImageView(portalImage);
        addEntity(portal, view);
    }

    @Override
    public void onLoad(Hunter hunter) {
        ImageView view = new ImageView(hunterImage);
        addEntity(hunter, view);
    }

    @Override
    public void onLoad(Sword sword) {
        ImageView view = new ImageView(swordImage);
        addEntity(sword, view);
    }

    @Override
    public void onLoad(Potion potion) {
        ImageView view = new ImageView(potionImage);
        addEntity(potion, view);
    }

    @Override
    public void onLoad(Pickaxe pickaxe) {
        ImageView view = new ImageView(pickaxeImage);
        addEntity(pickaxe, view);
    }

    @Override 
    public void onLoad(Water water) {
        ImageView view = new ImageView(waterImage);
        addEntity(water, view);
    }

    @Override 
    public void onLoad(Bomb bomb) {
        ImageView view = new ImageView(bombImage);
        changeStateImage(bomb, view);
        addEntity(bomb, view);
    }

    private void addEntity(Entity entity, ImageView view) {
        trackPosition(entity, view);
        
        if (entity instanceof MoveBehaviour) {
            movingEntities.add(view);
        } else {
            stationaryEntities.add(view);
        }
    }

    /**
     * Set a node in a GridPane to have its position track the position of an
     * entity in the dungeon.
     *
     * By connecting the model with the view in this way, the model requires no
     * knowledge of the view and changes to the position of entities in the
     * model will automatically be reflected in the view.
     * @param entity
     * @param node
     */
    private void trackPosition(Entity entity, Node node) {
        GridPane.setColumnIndex(node, entity.getX());
        GridPane.setRowIndex(node, entity.getY());

        entity.x().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable,
                    Number oldValue, Number newValue) {
                GridPane.setColumnIndex(node, newValue.intValue());
            }
        });
        
        entity.y().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable,
                    Number oldValue, Number newValue) {
                GridPane.setRowIndex(node, newValue.intValue());
            }
        });

        //Tracks an entity in terms of what image to display (might need to change to an enum so that door can change image etc)
        entity.displayOnScreen().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable,
                    Boolean oldValue, Boolean newValue) {
                if (newValue == false) {
                    ImageView image = (ImageView) node;
                    image.setImage(null);
                }
            }
        });
    }

    //If we want to add other images we will method overload 
    private void changeStateImage(Door door, Node node) {
        door.openState().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable,
                    Boolean oldValue, Boolean newValue) {
                if (newValue == true) {
                    ImageView image = (ImageView) node;
                    image.setImage(openDoorImage);
                } else if (newValue == false) {
                    ImageView image = (ImageView) node;
                    image.setImage(closedDoorImage);
                }
            }
        });
    }

    public void changeStateImage(Bomb bomb, ImageView view) {
            bomb.getFuseTimer().addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> observable,
                    Number oldValue, Number newValue) {
                        switch(newValue.intValue()) {
                            case -1:
                                view.setImage(bombImage);
                                break;
                            case 3:
                                view.setImage(bombImage1);
                                break;
                            case 2:
                                view.setImage(bombImage2);
                                break;
                            case 1:
                                view.setImage(bombImage3);
                                break;
                            case 0:
                                view.setImage(bombImage4);
                                break;
                            
                        }
                    }
            });
    }

    //these tooltips dont work :/
    // private void addItemIDHint(Key key, Node node) {
    //     Tooltip.install(node, new Tooltip("ID: "+key.getID()));
    // }

    // private void addItemIDHint(Door door, Node node) {
    //     Tooltip.install(node, new Tooltip("ID: "+door.getID()));
    // }

    /**
     * Create a controller that can be attached to the DungeonView with all the
     * loaded entities.
     * @return
     * @throws FileNotFoundException
     */
    public DungeonController loadController() throws FileNotFoundException {
        return new DungeonController(load(), stationaryEntities, movingEntities);
    }


}
