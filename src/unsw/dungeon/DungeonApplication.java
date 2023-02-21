package unsw.dungeon;

import java.io.IOException;

import javafx.application.Application;
import javafx.stage.Stage;

public class DungeonApplication extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {

        MenuScreen menuScreen = new MenuScreen(primaryStage);
        TutorialScreen tutorialScreen = new TutorialScreen(primaryStage);

        menuScreen.getController().setTutorialScreen(tutorialScreen);
        tutorialScreen.getController().setMenuScreen(menuScreen);

        menuScreen.start();

    }

    public static void main(String[] args) {
        launch(args);
    }
}
