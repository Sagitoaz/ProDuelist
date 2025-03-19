package Model;

public class UserInformation {
    private static String userID;
    private static String userName;

    public static void setUserID(String userID) {
        UserInformation.userID = userID;
    }

    public static String getUserName() {
        return userName;
    }

    public static void setUserName(String userName) {
        UserInformation.userName = userName;
    }

    public static String getUserID() {
        return userID;
    }
}
