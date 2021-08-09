package ResearchWorkbench.Models;

public class User {
    int userId;
    String userEmail;
    String userName;

    public User(){
        this.userId = 0;
        this.userEmail = "";
        this.userName = "";
    }

    public User(int userId, String userName, String userEmail){
        this.userId = userId;
        this.userName = userName;
        this.userEmail = userEmail;
    }

    public User(String userName, String userEmail){
        this(0, userName, userEmail);
    }
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail(){
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}
