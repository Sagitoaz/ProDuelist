package View;

import Controller.LoginControl;
import Controller.SceneManager;
import Model.UserInformation;
import Model.UserModel;
import Utils.UIHelper;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginView implements Initializable {
    @FXML private PasswordField passwordField;
    @FXML private TextField usernameField;
    @FXML private Label errorLabel;

    private final SceneManager sceneManager = new SceneManager();
    private final UIHelper uiHelper = new UIHelper();
    private final LoginControl loginControl = new LoginControl();
    private final UserModel userModel = new UserModel();
    @FXML
    private void onClickRegisterButton() throws IOException {
        Main.primaryStage.setScene(sceneManager.registScene());
        Main.primaryStage.centerOnScreen();
    }

    @FXML
    void onClickedLoginButton() throws IOException {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String loginError = loginControl.validateLogin(username, password);

        uiHelper.showLabelMessage(errorLabel, loginError, 3.0);
        if (loginError == null){
            String userID = userModel.getUserID(username, password);
            UserInformation.setUserID(userID);
            Main.primaryStage.setScene(sceneManager.mainMenuScene());
            Main.primaryStage.centerOnScreen();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        usernameField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                try {
                    onClickedLoginButton();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        passwordField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                try {
                    onClickedLoginButton();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}
