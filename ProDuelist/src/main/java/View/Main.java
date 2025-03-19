package View;

import Controller.SceneManager;
import Controller.UserInformationManager;
import Model.UserInformation;
import com.google.gson.JsonObject;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    public static Stage primaryStage;
    private final SceneManager sceneManager = SceneManager.getInstance();
    private final UserInformationManager userInformationManager = new UserInformationManager();

    @Override
    public void start(Stage stage) throws Exception {
        stage.setResizable(false);
        primaryStage = stage;
        sceneManager.getMusicController().autoPlayMusic();
        if (userInformationManager.isUserLoggedIn()) {
            JsonObject userInfo = userInformationManager.loadUserInfo();
            if (userInfo != null && userInfo.has("userID") && userInfo.has("userName")) {
                String userID = userInfo.get("userID").getAsString();
                String userName = userInfo.get("userName").getAsString();
                UserInformation.setUserID(userID);
                UserInformation.setUserName(userName);
                stage.setScene(sceneManager.AccountSettingView());
            } else {
                stage.setScene(sceneManager.loginScene());
            }
        } else {
            stage.setScene(sceneManager.loginScene());
        }
        stage.centerOnScreen();
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
