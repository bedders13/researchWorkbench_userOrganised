package ResearchWorkbench.Controllers;

import ResearchWorkbench.Data.DataLayer;
import ResearchWorkbench.Models.ListItem;
import ResearchWorkbench.Models.User;
import ResearchWorkbench.Models.UserList;

import java.util.ArrayList;

public class BusinessLayer {
    ResearchWorkbench.Data.DataLayer dataLayer;
    String connectionString = "jdbc:mysql://3.135.208.122/user_organised";
    String databaseUser = "hugh";
    String databasePassword = "AWS-mysql99";

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

    //read methods
    public User getUser(String userEmail){
        return dataLayer.getUser(userEmail);
    }

    public UserList getUserList(int userListId) {
        return dataLayer.getUserList(userListId);
    }

    public ArrayList<UserList> getUserLists(int userId) {
        return dataLayer.getUserLists(userId);
    }

    public ListItem getListItem(int listItemId) {
        return dataLayer.getListItem(listItemId);
    }

    public ArrayList<ListItem> getListItems(int userListId) {
        return dataLayer.getListItems(userListId);
    }

    public ArrayList<UserList> getUserListsContainingListItem(int listItemId) {
        return dataLayer.getUserListsContainingListItem(listItemId);
    }

    //update methods
    public int updateUserList(UserList userList) {
        return dataLayer.updateUserList(userList);
    }

    public int updateListItem(ListItem listItem) {
        return dataLayer.updateListItem(listItem);
    }

    //delete methods
    public boolean deleteUser(int userId){
        return dataLayer.deleteUser(userId);
    }

    public boolean deleteUserList(int userListId) {
        return dataLayer.deleteUserList(userListId);
    }

    public boolean deleteListItem(int listItemId){
        return dataLayer.deleteListItem(listItemId);
    }
}
