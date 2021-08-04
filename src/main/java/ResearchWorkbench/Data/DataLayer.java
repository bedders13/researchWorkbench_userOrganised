package ResearchWorkbench.Data;

import ResearchWorkbench.Models.ListItem;
import ResearchWorkbench.Models.UserList;

import java.sql.*;
import java.util.ArrayList;

public class DataLayer {
    private String connectionString;
    protected java.sql.Connection databaseConnection;

    public DataLayer(){
        connectionString = "";
        databaseConnection = null;
    }

    public DataLayer(String connectionString){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.initConnection();
        }
        catch (Exception e){
            System.out.println("JDBC driver not found");
        }

        this.connectionString = connectionString;
    }

    public void initConnection() {
        try {
            databaseConnection = java.sql.DriverManager.getConnection(connectionString);
        } catch (Exception e) {
            System.out.println("Error. Counldn't open database");
        }
    }

    //create methods
    public int createUserlist(UserList userList) {
        int userListId = -1;
        ResultSet resultSet = null;
        try {
            //prepare the sql statement
            PreparedStatement pStatement = databaseConnection.prepareStatement("INSERT INTO UserList (ListName, IsPrivate, DateCreated, DateModified, UserId) VALUES(?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            //set parameters for the statement
            pStatement.setString(1, userList.getUserListName());
            pStatement.setBoolean(2, userList.getIsPrivate());
            pStatement.setDate(3, (Date)userList.getDateCreated());
            pStatement.setDate(4, (Date)userList.getDateModified());
            pStatement.setInt(5, userList.getUserId());
            //execute the query
            boolean result = pStatement.execute();
            if (result == true){
                //get the userListId
                 resultSet = pStatement.getGeneratedKeys();
                if (resultSet.next()){
                    userListId = resultSet.getInt(1);
                }
            }
        } catch(SQLException e){
            System.out.println("Error inserting into UserList: " + e.getMessage());
        } finally {
            try{
                if(resultSet != null){resultSet.close();}
            } catch (SQLException e){
                System.out.println("Couldn't close connection: " + e.getMessage());
            }

        }
        return userListId;
    }

    //read methods
    public UserList getUserList(int userListId){
        UserList userList = new UserList();

        try{

            Statement statement = databaseConnection.createStatement();
            //execute the query
            ResultSet resultSet = statement.executeQuery("SELECT * FROM UserList WHERE UserListId =" + userListId);
            resultSet.next();

            //set the UserList variables
            userList.setUserListId(resultSet.getInt("UserListId"));
            userList.setUserListName(resultSet.getString("name"));
            userList.setIsPrivate(resultSet.getBoolean("IsPrivate"));
            userList.setDateCreated(resultSet.getDate("DateCreated"));
            userList.setDateModified(resultSet.getDate("DateModified"));
            userList.setUserId(resultSet.getInt("UserId"));

        } catch (SQLException e){
            System.out.println("Sql Error occured: " + e);
        }
        return userList;
    }

    public ArrayList<UserList> getUserLists(int userId){
        ArrayList<UserList> userLists = new ArrayList<UserList>();

        try{
            //create the sql statement
            Statement statement = databaseConnection.createStatement();
            //execute the query
            ResultSet resultSet = statement.executeQuery("SELECT * FROM UserList WHERE UserId =" + userId);

            //set the UserList variables
            while(resultSet.next()){
                UserList userList = new UserList();
                userList.setUserListId(resultSet.getInt("UserListId"));
                userList.setUserListName(resultSet.getString("name"));
                userList.setIsPrivate(resultSet.getBoolean("IsPrivate"));
                userList.setDateCreated(resultSet.getDate("DateCreated"));
                userList.setDateModified(resultSet.getDate("DateModified"));
                userList.setUserId(resultSet.getInt("UserId"));
                //add the user list to the list of user lists
                userLists.add(userList);
            }

        } catch (SQLException e){
            System.out.println("Sql Error occured: " + e);
        }
        return userLists;
    }

    public ListItem getListItem(int listItemId){
        ListItem listItem = new ListItem();

        try{
            //create the sql statement
            Statement statement = databaseConnection.createStatement();
            //execute the query
            ResultSet resultSet = statement.executeQuery("SELECT * FROM ListItem WHERE ListItemId =" + listItemId);
            resultSet.next();

            //set the ListItem variables
            listItem.setListItemId(resultSet.getInt("ListItemId"));
            listItem.setListObject(resultSet.getString("ListObject"));
            listItem.setUserListId(resultSet.getInt("UserListId"));

        } catch (SQLException e){
            System.out.println("Sql Error occured: " + e);
        }
        return listItem;
    }

    public ArrayList<ListItem> getListItems(int userListId){
        ArrayList<ListItem> listItems = new ArrayList<ListItem>();

        try{
            //create the sql statement
            Statement statement = databaseConnection.createStatement();
            //execute the query
            ResultSet resultSet = statement.executeQuery("SELECT * FROM ListItem WHERE UserListId =" + userListId);

            //set the ListItem variables
            while(resultSet.next()){
                ListItem listItem = new ListItem();
                listItem.setListItemId(resultSet.getInt("ListItemId"));
                listItem.setListObject(resultSet.getString("ListObject"));
                listItem.setUserListId(resultSet.getInt("UserListId"));
                //add the user list to the list of user lists
                listItems.add(listItem);
            }

        } catch (SQLException e){
            System.out.println("Sql Error occured: " + e);
        }
        return listItems;
    }

    public ArrayList<UserList> getUserListsContainingListItem(int listItemId){
        ArrayList<UserList> userLists = new ArrayList<UserList>();
        try{
            //create the sql statement
            Statement statement = databaseConnection.createStatement();
            //execute the query
            ResultSet resultSet = statement.executeQuery("SELECT * FROM UserList INNER JOIN ListItem ON UserList.UserListId = ListItem.UserListId WHERE ListItem.ListItemId =" + listItemId + "AND UserList.IsPrivate = false");

            //set the UserList variables
            while (resultSet.next()){
                UserList userList = new UserList();
                userList.setUserListId(resultSet.getInt("UserListId"));
                userList.setUserListName(resultSet.getString("name"));
                userList.setIsPrivate(resultSet.getBoolean("IsPrivate"));
                userList.setDateCreated(resultSet.getDate("DateCreated"));
                userList.setDateModified(resultSet.getDate("DateModified"));
                userList.setUserId(resultSet.getInt("UserId"));
                //add the user list to the list of user lists
                userLists.add(userList);
            }
        } catch(SQLException e){
            System.out.println("There was an error searching the lists: " + e);
        }
        return userLists;
    }


}
