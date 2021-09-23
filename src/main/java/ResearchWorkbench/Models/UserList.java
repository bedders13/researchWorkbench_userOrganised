package ResearchWorkbench.Models;

import java.util.Date;

public class UserList {
    int userListId;
    String userListName;
    Boolean isPrivate;
    Date dateCreated;
    Date dateModified;
    int userId;

    /**
     * Base constructor
     */
    public UserList(){
        this.userListId = 0;
        this.userListName = "";
        this.isPrivate = true;
        this.dateCreated = new Date();
        this.dateModified = new Date();
        this.userId = 0;
    }

    /**
     * Main constructor
     * @param userListId the id of the user list
     * @param userListName the name of the user list
     * @param isPrivate the privacy of the user list
     * @param userId the id of the user creating the list
     */
    public UserList(int userListId, String userListName, Boolean isPrivate, int userId){
        this.userListId = userListId;
        this.userListName = userListName;
        this.isPrivate = isPrivate;
        this.dateCreated = new Date();
        this.dateModified = new Date();
        this.userId = userId;
    }

    /**
     * Constructor without user list id
     * @param userListName the name of the user list
     * @param isPrivate the privacy of the user list
     * @param userId the id of the user creating the list
     */
    public UserList(String userListName, Boolean isPrivate, int userId){
        this(0, userListName, isPrivate, userId);
    }

    /**
     * Get the id of the user list
     * @return returns the id of the user list
     */
    public int getUserListId() {
        return userListId;
    }

    /**
     * Get the privacy of the list
     * @return returns true if the list is private, otherwise false
     */
    public Boolean getIsPrivate() {
        return isPrivate;
    }

    /**
     * Get the name of the user list
     * @return returns the name of the user list
     */
    public String getUserListName() {
        return userListName;
    }

    /**
     * Get the date created of the user list
     * @return returns the date created of the user list
     */
    public Date getDateCreated() {
        return dateCreated;
    }

    /**
     * Get the date modified of the user list
     * @return returns date modified of the user list
     */
    public Date getDateModified() {
        return dateModified;
    }

    /**
     * Get the id of the user that created the user list
     * @return returns the id of the user list
     */
    public int getUserId() {
        return userId;
    }

    /**
     * Set the date the user list was last modified
     * @param dateModified the date the list was last modified
     */
    public void setDateModified(Date dateModified) {
        this.dateModified = dateModified;
    }

    /**
     * Set the id of the user that created the user list
     * @param userId the id of the user that created the list
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    /**
     * Set the name of the user list
     * @param userListName the name of the user list
     */
    public void setUserListName(String userListName) {
        this.userListName = userListName;
    }

    /**
     * Set the privacy of the user list
     * @param isPrivate true if the list is private, false otherwise
     */
    public void setIsPrivate(Boolean isPrivate) {
        this.isPrivate = isPrivate;
    }

    /**
     * Set the id of the user list
     * @param userListId the id of the user list
     */
    public void setUserListId(int userListId){
        this.userListId = userListId;
    }

    /**
     * Set the date the user list was created
     * @param dateCreated the date the user list was created
     */
    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }
}
