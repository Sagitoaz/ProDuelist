package View;

import Controller.RegistControl;
import Controller.SceneManager;
import Model.UserModel;
import Utils.UIHelper;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;

public class RegistView {
    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField usernameField;

    @FXML
    private Label errorLabel;

    @FXML
    private Label registSuccessLabel;

    private final SceneManager sceneManager = new SceneManager();
    private final UIHelper uiHelper = new UIHelper();
    private final RegistControl registControl = new RegistControl();
    private final UserModel userModel = new UserModel();

    @FXML
    private void onClickAlreadyHaveAccount() throws IOException {
        Main.primaryStage.setScene(sceneManager.loginScene());
    }

    @FXML
    private void onClickedRegisterButton() {
        String email = emailField.getText();
        String username = usernameField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        String registError = registControl.validateRegistration(email, username, password, confirmPassword);
        uiHelper.showLabelMessage(errorLabel, registError, 3.0);

        boolean registSuccessful = userModel.registUser(email, username, password);
        if (registSuccessful) {
            uiHelper.showLabelMessage(registSuccessLabel, "Registration successful", 3.0);
            clearFields();
        }
    }

    private void clearFields() {
        emailField.clear();
        usernameField.clear();
        passwordField.clear();
        confirmPasswordField.clear();
    }
}
