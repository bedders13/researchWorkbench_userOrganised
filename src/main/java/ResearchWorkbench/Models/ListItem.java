package ResearchWorkbench.Models;

public class ListItem {
    int listItemId;
    String listObject;
    int userListId;

    public ListItem(){
        this.listItemId = 0;
        this.listObject = "";
        this.userListId = 0;
    }

    public ListItem(int listItemId, String listObject, int userListId){
        this.listItemId = listItemId;
        this.listObject = listObject;
        this.userListId = userListId;
    }

    public int getListItemId() {
        return listItemId;
    }

    public void setListItemId(int listItemId) {
        this.listItemId = listItemId;
    }

    public String getListObject() {
        return listObject;
    }

    public void setListObject(String listObject) {
        this.listObject = listObject;
    }

    public int getUserListId() {
        return userListId;
    }

    public void setUserListId(int userListId) {
        this.userListId = userListId;
    }
}