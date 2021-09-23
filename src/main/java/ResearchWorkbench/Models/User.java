package ResearchWorkbench.Models;

public class User {
    int userId;
    String userEmail;
    String userName;

    /**
     * Base constructor
     */
    public User(){
        this.userId = 0;
        this.userEmail = "";
        this.userName = "";
    }

    /**
     * Main constructor
     * @param userId the id of the user
     * @param userName the name of the user
     * @param userEmail the email of the user
     */
    public User(int userId, String userName, String userEmail){
        this.userId = userId;
        this.userName = userName;
        this.userEmail = userEmail;
    }

    /**
     * Constructor without user id
     * @param userName the name of the user
     * @param userEmail the email of the user
     */
    public User(String userName, String userEmail){
        this(0, userName, userEmail);
    }

    /**
     * Get the id of the user
     * @return returns the id of the user
     */
    public int getUserId() {
        return userId;
    }

    /**
     * Get name of the user
     * @return returns the name of the user
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Get the email of the user
     * @return returns email of the user
     */
    public String getUserEmail(){
        return userEmail;
    }

    /**
     * Set the email of the user
     * @param userEmail the email to set the user email to
     */
    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    /**
     * Set the id of the user
     * @param userId the id to set the userId to
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    /**
     * Set the username
     * @param userName the name to set the username to
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }
}
