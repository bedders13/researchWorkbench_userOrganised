package ResearchWorkbench.Models;

import java.util.Date;

public class UserList {
    int userListId;
    String userListName;
    Boolean isPrivate;
    Date dateCreated;
    Date dateModified;
    int userId;

    public UserList(){
        this.userListId = 0;
        this.userListName = "";
        this.isPrivate = true;
        this.dateCreated = new Date();
        this.dateModified = new Date();
        this.userId = 0;
    }

    public UserList(int userListId, String userListName, Boolean isPrivate, Date dateCreated, Date dateModified, int userId){
        this.userListId = userListId;
        this.userListName = userListName;
        this.isPrivate = isPrivate;
        this.dateCreated = dateCreated;
        this.dateModified = dateModified;
        this.userId = userId;
    }

    public int getUserListId() {
        return userListId;
    }

    public void setUserListId(int userListId){
        this.userListId = userListId;
    }

    public Boolean getIsPrivate() {
        return isPrivate;
    }

    public String getUserListName() {
        return userListName;
    }

    public void setUserListName(String userListName) {
        this.userListName = userListName;
    }

    public void setIsPrivate(Boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Date getDateModified() {
        return dateModified;
    }

    public void setDateModified(Date dateModified) {
        this.dateModified = dateModified;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
