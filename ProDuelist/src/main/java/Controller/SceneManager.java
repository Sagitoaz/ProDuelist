package Controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;

public class SceneManager {
    public Scene loginScene() throws IOException {
        Parent loginView = FXMLLoader.load(getClass().getResource("/FXML/LoginView/LoginView.fxml"));
        return new Scene(loginView);
    }

    public Scene registScene() throws IOException {
        Parent registerView = FXMLLoader.load(getClass().getResource("/FXML/RegisterView/RegisterView.fxml"));
        return new Scene(registerView);
    }

    public Scene mainMenuScene() throws IOException {
        Parent mainMenuScene = FXMLLoader.load(getClass().getResource("/FXML/MainMenuView/MainMenuView.fxml"));
        return new Scene(mainMenuScene);
    }
}
