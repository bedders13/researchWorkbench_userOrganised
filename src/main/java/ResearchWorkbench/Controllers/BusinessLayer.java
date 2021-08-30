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
//    String connectionString = "jdbc:mysql://20.87.26.56/user_organised";
    String databaseUser = "hugh";
    String databasePassword = "AZURE-mysql99";

//    public BusinessLayer() {
//        dataLayer = new DataLayer();
//    }

    public BusinessLayer() {
        dataLayer = new DataLayer(connectionString, databaseUser, databasePassword);
    }

    //create methods
    public int createUser(User user){
        return dataLayer.createUser(user);
    }

    public int createUserList(UserList userList) {
        return dataLayer.createUserList(userList);
    }

    public int createListItem(ListItem listItem) {
        return dataLayer.createListItem(listItem);
    }

    public int createBookmark(Bookmark bookmark){
        return dataLayer.createBookmark(bookmark);
    }

    //read methods
    public User getUser(String userEmail){
        return dataLayer.getUser(userEmail);
    }

    public User getUser(int userId){
        return dataLayer.getUser(userId);
    }

    public UserList getUserList(int userListId) {
        return dataLayer.getUserList(userListId);
    }

    public ArrayList<UserList> getUserLists(int userId) {
        return dataLayer.getUserLists(userId);
    }

    public ListItem getListItem(String objectId, int userListId ) {
        return dataLayer.getListItem(objectId, userListId);
    }

    public ArrayList<ListItem> getListItems(int userListId) {
        return dataLayer.getListItems(userListId);
    }

    public Bookmark getBookmark(String objectId, int userId){
        return dataLayer.getBookmark(objectId, userId);
    }

    public ArrayList<UserList> getUserListsContainingListItem(String objectId, int userId) {
        return dataLayer.getUserListsContainingListItem(objectId, userId);
    }

    public ArrayList<Bookmark> getBookmarks(int userId){
        return dataLayer.getBookmarks(userId);
    }

    //update methods
    public int updateUserList(UserList userList) {
        return dataLayer.updateUserList(userList);
    }

//    public int updateListItem(ListItem listItem) {
//        return dataLayer.updateListItem(listItem);
//    }

    //delete methods
    public boolean deleteUser(int userId){
        return dataLayer.deleteUser(userId);
    }

    public boolean deleteUserList(int userListId, int userId) {
        return dataLayer.deleteUserList(userListId, userId);
    }

    public boolean deleteListItem(String objectId, int user_list_id){
        return dataLayer.deleteListItem(objectId, user_list_id);
    }

    public boolean deleteBookmark(String objectId, int userId){
        return dataLayer.deleteBookmark(objectId, userId);
    }

    //general methods
    public boolean isObjectBookmarked(String objectId, int userId){
        Bookmark bookmark = dataLayer.getBookmark(objectId, userId);
        return (bookmark.getBookmarkId() != 0);
    }

}
