package ResearchWorkbench.Models;

public class ListItem {
    int listItemId;
    String objectId;
    String objectTitle;
    String objectAuthor;
    String objectDate;
    int userListId;

    /**
     * Base constructor
     */
    public ListItem(){
        listItemId = 0;
        objectId = "null";
        objectTitle = "null";
        objectAuthor = "null";
        objectDate = "null";
        userListId = 0;
    }

    /**
     * Constructor without listItemId
     * @param objectId the id of the resource
     * @param objectTitle the title of the resource
     * @param objectAuthor the author/creator of the resource
     * @param objectDate the date of the resource
     * @param userListId the user id of the user creating the list item
     */
    public ListItem(String objectId, String objectTitle, String objectAuthor, String objectDate, int userListId){
        this(0, objectId, objectTitle, objectAuthor, objectDate, userListId);
    }

    /**
     * Main constructor that sets all instance variables
     * @param listItemId id of list item used for MySQL
     * @param objectId the id of the resource
     * @param objectTitle the title of the resource
     * @param objectAuthor the author/creator of the resource
     * @param objectDate the date of the resource
     * @param userListId the user id of the user creating the list item
     */
    public ListItem(int listItemId, String objectId, String objectTitle, String objectAuthor, String objectDate, int userListId){
        this.listItemId = listItemId;
        this.objectId = objectId;
        this.objectTitle = objectTitle;
        this.objectAuthor = objectAuthor;
        this.objectDate = objectDate;
        this.userListId = userListId;
    }

    /**
     * Get the id of the list item
     * @return returns the id of the list item
     */
    public int getListItemId() {
        return listItemId;
    }

    /**
     * Get the id of the resource
     * @return returns the id of the resource
     */
    public String getListObjectId() {
        return objectId;
    }

    /**
     * Get the title of the resource
     * @return returns the title of the resource
     */
    public String getObjectTitle() {
        return objectTitle;
    }

    /**
     * Get the author/creator of the resource
     * @return returns the author/creator of the resource
     */
    public String getObjectAuthor() {
        return objectAuthor;
    }

    /**
     * Get the date of the resource
     * @return returns the date of the resource
     */
    public String getObjectDate() {
        return objectDate;
    }

    /**
     * Get the id of the user list the list item is on
     * @return returns the id of the list item
     */
    public int getUserListId() {
        return userListId;
    }

    /**
     * Set the id of the list item
     * @param listItemId the id of the list item
     */
    public void setListItemId(int listItemId) {
        this.listItemId = listItemId;
    }

    /**
     * Set the id of the resource
     * @param objectId he id of the resource
     */
    public void setListObjectId(String objectId) {
        this.objectId = objectId;
    }

    /**
     * Set the title of the resource
     * @param objectTitle the title of the resource
     */
    public void setObjectTitle(String objectTitle) {
        this.objectTitle = objectTitle;
    }

    /**
     * Set the author/creator of the resource
     * @param objectAuthor the author/creator of the resource
     */
    public void setObjectAuthor(String objectAuthor) {
        this.objectAuthor = objectAuthor;
    }

    /**
     * Set the date of the resource
     * @param objectDate the date of the resource
     */
    public void setObjectDate(String objectDate) {
        this.objectDate = objectDate;
    }

    /**
     * Set the id of the user list that the object is on
     * @param userListId the id of the user list
     */
    public void setUserListId(int userListId) {
        this.userListId = userListId;
    }

}