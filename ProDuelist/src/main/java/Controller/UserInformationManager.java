package Controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class UserInformationManager {
    private final String userInfoFilePath = "data/user.json";

    public void saveUserInfor(String userID, String userName) throws IOException {
        try (FileWriter fileWriter = new FileWriter(userInfoFilePath)) {
            Gson gson = new Gson();
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("userID", userID);
            jsonObject.addProperty("userName", userName);
            gson.toJson(jsonObject, fileWriter);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isUserLoggedIn() {
        File file = new File(userInfoFilePath);
        return file.exists();
    }

    public JsonObject loadUserInfo() {
        File file = new File(userInfoFilePath);
        if (file.exists()) {
            try (FileReader reader = new FileReader(file)) {
                Gson gson = new Gson();
                return gson.fromJson(reader, JsonObject.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public void deleteUserInfo() {
        File file = new File(userInfoFilePath);
        if (file.exists()) {
            if (file.delete()) {
                System.out.println("User info deleted successfully.");
            } else {
                System.out.println("Failed to delete user info.");
            }
        }
    }
}
