package ResearchWorkbench.Controllers;

import ResearchWorkbench.Data.DataLayer;
import ResearchWorkbench.Models.ListItem;
import ResearchWorkbench.Models.UserList;

import java.util.ArrayList;

public class BusinessLayer {
    ResearchWorkbench.Data.DataLayer dataLayer;

    public BusinessLayer(){
        dataLayer = new DataLayer();
    }

    public BusinessLayer(String connectionString){
        dataLayer = new DataLayer(connectionString);
    }

    public UserList getUserList(int userListId){
        return dataLayer.getUserList(userListId);
    }

    public ArrayList<UserList> getUserLists(int userId){
        return dataLayer.getUserLists(userId);
    }

    public ListItem getListItem(int listItemId){
        return dataLayer.getListItem(listItemId);
    }

    public ArrayList<ListItem> getListItems(int userListId){
        return dataLayer.getListItems(userListId);
    }
}
