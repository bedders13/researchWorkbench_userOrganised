package ResearchWorkbench.Controllers;

import ResearchWorkbench.Data.DataLayer;
import ResearchWorkbench.Models.Bookmark;
import ResearchWorkbench.Models.ListItem;
import ResearchWorkbench.Models.User;
import ResearchWorkbench.Models.UserList;

import java.util.ArrayList;

public class BusinessLayer {
    ResearchWorkbench.Data.DataLayer dataLayer;
    String connectionString = "jdbc:mysql://localhost/user_organised";
    String databaseUser = "user";
    String databasePassword = "password";

//    public BusinessLayer() {
//        dataLayer = new DataLayer();
//    }

    /**
     * Constructor which creates a new DataLayer object with the connectionString, user and password
     * instance variables used to connect to the MySQL database
     */
    public BusinessLayer() {
        dataLayer = new DataLayer(connectionString, databaseUser, databasePassword);
    }

    //create methods region

    /**
     * Calls the createUser method in the DataLayer
     * @param user user object of the user that needs to be created in the database
     * @return returns the userId of the newly created row of User in the database
     */
    public int createUser(User user){
        return dataLayer.createUser(user);
    }

    /**
     * Calls the createUserList method in the DataLayer
     * @param userList userList object of the user list that needs to be created in the database
     * @return returns the userListId of the newly created row of UserList in the database
     */
    public int createUserList(UserList userList) {
        return dataLayer.createUserList(userList);
    }

    /**
     * Calls the createListItem method in the DataLayer
     * @param listItem listItem object that needs to be created in the database
     * @return returns the listItemId of the newly created row of ListItem in the database
     */
    public int createListItem(ListItem listItem) {
        return dataLayer.createListItem(listItem);
    }

    /**
     * Calls the createBookmark method in the DataLayer
     * @param bookmark bookmark object that needs to be saved in the database
     * @return returns the bookmarkId of the newly created row of Bookmark in the database
     */
    public int createBookmark(Bookmark bookmark){
        return dataLayer.createBookmark(bookmark);
    }

    //read methods region

    /**
     * Calls the getUser method in the DataLayer
     * @param userEmail the email address of the User that needs to be fetched from the database
     * @return returns a User object
     */
    public User getUser(String userEmail){
        return dataLayer.getUser(userEmail);
    }

    /**
     * Calls the getUser method in the DataLayer
     * @param userId the userId of the User that needs to be fetched from the database
     * @return returns a User object
     */
    public User getUser(int userId){
        return dataLayer.getUser(userId);
    }

    /**
     * Calls the getUserList method in the DataLayer
     * @param userListId the userListId of the UserList that needs to be fetched from the database
     * @return returns a UserList object
     */
    public UserList getUserList(int userListId) {
        return dataLayer.getUserList(userListId);
    }

    /**
     * Calls the getUserLists method in the DataLayer
     * @param userId the userId of all the UserLists
     * @return returns an ArrayList of all the UserList objects
     */
    public ArrayList<UserList> getUserLists(int userId) {
        return dataLayer.getUserLists(userId);
    }

    /**
     * Calls the getListItem method in the DataLayer
     * @param objectId the objectId of the listItem that needs to be fetched
     * @param userListId the userListId of the listItem that needs to be fetched
     * @return returns a ListItem object
     */
    public ListItem getListItem(String objectId, int userListId ) {
        return dataLayer.getListItem(objectId, userListId);
    }

    /**
     * Calls the getListItems method in the DataLayer
     * @param userListId the userListId of the list that contains all the items
     * @return returns an ArrayList of all the ListItem objects
     */
    public ArrayList<ListItem> getListItems(int userListId) {
        return dataLayer.getListItems(userListId);
    }

    /**
     * Calls the getBookmark method in the DataLayer
     * @param objectId the objectId of the bookmark that needs to be fetched
     * @param userId the userId of the bookmark
     * @return returns a Bookmark object
     */
    public Bookmark getBookmark(String objectId, int userId){
        return dataLayer.getBookmark(objectId, userId);
    }

    /**
     * Calls the getUserListsContainingListItem method in the DataLayer
     * @param objectId objectId of the ListItem that is searched for on other user lists
     * @return returns an ArrayList of UserList objects that contain the list item with the given objectId
     */
    public ArrayList<UserList> getUserListsContainingListItem(String objectId) {
        return dataLayer.getUserListsContainingListItem(objectId);
    }

    /**
     * Calls the getBookmarks method in the DataLayer
     * @param userId the userId of User of the bookmarks that need to be fetched
     * @return returns an ArrayList of bookmark objects
     */
    public ArrayList<Bookmark> getBookmarks(int userId){
        return dataLayer.getBookmarks(userId);
    }

    //update methods region

    /**
     * Calls the updateUserList method in the DataLayer
     * @param userList userList object that needs to be updated in the database
     * @return returns 1 if successful
     */
    public int updateUserList(UserList userList) {
        return dataLayer.updateUserList(userList);
    }

    //delete methods region

    /**
     * Calls the deleteUser method in the DataLayer
     * @param userId the userId of the User that needs to be deleted
     * @return returns true/false depending on delete success
     */
    public boolean deleteUser(int userId){
        return dataLayer.deleteUser(userId);
    }

    /**
     * Calls the deleteUserList method in the DataLayer
     * @param userListId the userListId of the user list that needs to be deleted
     * @param userId the userId of the user that is deleting the list
     * @return returns true/false depending on delete success
     */
    public boolean deleteUserList(int userListId, int userId) {
        return dataLayer.deleteUserList(userListId, userId);
    }

    /**
     * Calls the deleteListItem method in the DataLayer
     * @param objectId the objectId of the list item that needs to be deleted
     * @param userListId the userListId of the user list the item needs to be deleted from
     * @return returns true/false depending on delete success
     */
    public boolean deleteListItem(String objectId, int userListId){
        return dataLayer.deleteListItem(objectId, userListId);
    }

    /**
     * Calls the deleteBookmark method in the DataLayer
     * @param objectId the objectId of the item that needs to be removed from bookmarks
     * @param userId the userId of the user that is removing the bookmark
     * @return returns true/false depending on delete success
     */
    public boolean deleteBookmark(String objectId, int userId){
        return dataLayer.deleteBookmark(objectId, userId);
    }

    //general methods region

    /**
     * Calls getBookmark method from DataLayer to check if the bookmark exists
     * @param objectId the objectId of the item that needs to be checked
     * @param userId the userId of the user that is logged in
     * @return returns true/false if resource is bookmarked
     */
    public boolean isObjectBookmarked(String objectId, int userId){
        Bookmark bookmark = dataLayer.getBookmark(objectId, userId);
        return (bookmark.getBookmarkId() != 0);
    }

}
