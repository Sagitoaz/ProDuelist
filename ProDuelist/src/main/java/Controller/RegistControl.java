package Controller;

import Model.UserModel;

public class RegistControl {
    private UserModel userModel = new UserModel();
    public String validateRegistration(String email, String username, String password, String confirmPassword) {
        if (email.isEmpty()) {
            return "Do not leave email blank!";
        }
        if (!email.contains("@")) {
            return "Invalid email!";
        }
        if (userModel.isEmailExists(email)) {
            return "Email has been used!";
        }
        if (username.isEmpty()) {
            return "Do not leave username blank!";
        }
        if (userModel.isUsernameExists(username)) {
            return "Username already exists!";
        }
        if (password.isEmpty()) {
            return "Do not leave password blank!";
        }
        if (confirmPassword.isEmpty()) {
            return "Please confirm password!";
        }
        if (!confirmPassword.equals(password)) {
            return "Passwords do not match!";
        }
        return null;
    }
}