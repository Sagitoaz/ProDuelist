package Model;

public class UserInformation {
    private static String userID;

    public static void setUserID(String userID) {
        UserInformation.userID = userID;
    }

    public static String getUserID() {
        return userID;
    }
}
