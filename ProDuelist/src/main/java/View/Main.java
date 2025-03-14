package View;

import Controller.SceneManager;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    public static Stage primaryStage;
    private final SceneManager sceneManager = new SceneManager();

    @Override
    public void start(Stage stage) throws Exception {
        stage.setResizable(false);
        primaryStage = stage;
        stage.setScene(sceneManager.loginScene());
        stage.centerOnScreen();
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
