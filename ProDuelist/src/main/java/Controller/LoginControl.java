package Controller;

import Model.UserModel;

public class LoginControl {
    private UserModel userModel = new UserModel();
    public String validateLogin(String username, String password) {
        if (!userModel.isUsernameExists(username)) {
            return "Username doesn't exist!";
        } else {
            if (userModel.getUserID(username, password) == null) {
                return "Incorrect password!";
            }
        }
        return null;
    }
}
