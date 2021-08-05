package ResearchWorkbench.Controllers;

import ResearchWorkbench.Data.DataLayer;
import ResearchWorkbench.Models.ListItem;
import ResearchWorkbench.Models.UserList;

import java.util.ArrayList;

public class BusinessLayer {
    ResearchWorkbench.Data.DataLayer dataLayer;

    public BusinessLayer() {
        dataLayer = new DataLayer();
    }

    public BusinessLayer(String connectionString) {
        dataLayer = new DataLayer(connectionString);
    }

    //create methods
    public int createUserList(UserList userList) {
        return dataLayer.createUserlist(userList);
    }

    public int createListItem(ListItem listItem) {
        return dataLayer.createListItem(listItem);
    }

    //read methods
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
    public boolean deleteUserList(int userListId) {
        return dataLayer.deleteUserList(userListId);
    }

    public boolean deleteListItem(int listItemId){
        return dataLayer.deleteListItem(listItemId);
    }
}
