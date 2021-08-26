package ResearchWorkbench.Models;

public class ListItem {
    int listItemId;
    String objectId;
    String objectTitle;
    String objectAuthor;
    String objectDate;
    int userListId;

    public ListItem(){
        listItemId = 0;
        objectId = "null";
        objectTitle = "null";
        objectAuthor = "null";
        objectDate = "null";
        userListId = 0;
    }
    public ListItem(String objectId, String objectTitle, String objectAuthor, String objectDate, int userListId){
        this(0, objectId, objectTitle, objectAuthor, objectDate, userListId);
    }

    public ListItem(int bookmarkId, String objectId, String objectTitle, String objectAuthor, String objectDate, int userListId){
        this.listItemId = bookmarkId;
        this.objectId = objectId;
        this.objectTitle = objectTitle;
        this.objectAuthor = objectAuthor;
        this.objectDate = objectDate;
        this.userListId = userListId;
    }

    public int getListItemId() {
        return listItemId;
    }

    public void setListItemId(int listItemId) {
        this.listItemId = listItemId;
    }

    public String getListObjectId() {
        return objectId;
    }

    public String getObjectTitle() {
        return objectTitle;
    }

    public String getObjectAuthor() {
        return objectAuthor;
    }

    public String getObjectDate() {
        return objectDate;
    }

    public int getUserListId() {
        return userListId;
    }

    public void setListObjectId(String objectId) {
        this.objectId = objectId;
    }

    public void setObjectTitle(String objectTitle) {
        this.objectTitle = objectTitle;
    }

    public void setObjectAuthor(String objectAuthor) {
        this.objectAuthor = objectAuthor;
    }

    public void setObjectDate(String objectDate) {
        this.objectDate = objectDate;
    }

    public void setUserListId(int userListId) {
        this.userListId = userListId;
    }

}