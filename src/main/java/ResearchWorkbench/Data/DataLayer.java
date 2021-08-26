package ResearchWorkbench.Data;

import ResearchWorkbench.Models.Bookmark;
import ResearchWorkbench.Models.ListItem;
import ResearchWorkbench.Models.User;
import ResearchWorkbench.Models.UserList;

import java.awt.print.Book;
import java.io.File;
import java.io.PrintStream;
import java.sql.*;
import java.util.ArrayList;

public class DataLayer {
    private String connectionString;
    private String databaseUser;
    private String databaseUserPassword;
    protected java.sql.Connection databaseConnection;

    public DataLayer(){
        connectionString = "";
        databaseUser = "";
        databaseUserPassword = "";
        databaseConnection = null;
    }

    public DataLayer(String connectionString, String databaseUser, String databaseUserPassword) {
        this.connectionString = connectionString;
        this.databaseUser = databaseUser;
        this.databaseUserPassword = databaseUserPassword;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.initConnection();
        } catch (Exception e) {
            System.out.println("JDBC driver not found");
        }
    }

    public void initConnection() {
        try {
//            PrintStream outStream = new PrintStream(new File("out.txt"));
//            System.setOut(outStream);
//            System.out.println("initConnection: connect: " + connectionString + " user: " + databaseUser + " pass: " + databaseUserPassword);
            databaseConnection = java.sql.DriverManager.getConnection(connectionString, databaseUser, databaseUserPassword);
//            outStream.close();
        } catch (Exception e) {
            System.out.println("Error. Couldn't open database");
        }
    }

    //create methods
    public int createUser(User user){
        int userId = -1;
        try {
            //prepare the sql statement
            PreparedStatement pStatement = databaseConnection.prepareStatement("INSERT INTO User " +
                    "(user_name, user_email) VALUES(?, ?);");
            //set parameters for the statement
            pStatement.setString(1, user.getUserName());
            pStatement.setString(2, user.getUserEmail());
            //execute the query
            boolean result = pStatement.execute();

            //get the new userId
            userId = getUser(user.getUserEmail()).getUserId();
        } catch(SQLException e){
            System.out.println("Error inserting UserList: " + e.getMessage());
        }
        return userId;

    }

    public int createUserList(UserList userList) {
        int userListId = -1;
        ResultSet resultSet = null;
        try {
            //prepare the sql statement
            PreparedStatement pStatement = databaseConnection.prepareStatement("INSERT INTO UserList " +
                    "(name, is_private, user_id) VALUES(?, ?, ?);", Statement.RETURN_GENERATED_KEYS);
            //set parameters for the statement
            pStatement.setString(1, userList.getUserListName());
            pStatement.setBoolean(2, userList.getIsPrivate());
//            pStatement.setDate(3, (new java.sql.Date(userList.getDateCreated())));
//            pStatement.setDate(4, (java.sql.Date)userList.getDateModified());
            pStatement.setInt(3, userList.getUserId());
            //execute the query
            boolean result = pStatement.execute();
            if (result){
                //get the userListId
                 resultSet = pStatement.getGeneratedKeys();
                if (resultSet.next()){
                    userListId = resultSet.getInt(1);
                }
            }
        } catch(SQLException e){
            System.out.println("Error inserting UserList: " + e.getMessage());
        } finally {
            try{
                if(resultSet != null){resultSet.close();}
            } catch (SQLException e){
                System.out.println("Couldn't close connection: " + e.getMessage());
            }

        }
        return userListId;
    }

    public int createListItem(ListItem listItem) {
        int listItemId = -1;
        ResultSet resultSet = null;
        try {
            //prepare the sql statement
            PreparedStatement pStatement = databaseConnection.prepareStatement("INSERT INTO ListItem (list_object_id, user_list_id);" +
                    "VALUES(?, ?)", Statement.RETURN_GENERATED_KEYS);
            //set parameters for the statement
            pStatement.setString(1, listItem.getListObject());
            pStatement.setInt(2, listItem.getUserListId());
            //execute the query
            boolean result = pStatement.execute();
            if (result == true){
                //get the userListId
                resultSet = pStatement.getGeneratedKeys();
                if (resultSet.next()){
                    listItemId = resultSet.getInt(1);
                }
            }
        } catch(SQLException e){
            System.out.println("Error inserting ListItem: " + e.getMessage());
        } finally {
            try{
                if(resultSet != null){resultSet.close();}
            } catch (SQLException e){
                System.out.println("Couldn't close connection: " + e.getMessage());
            }

        }
        return listItemId;
    }

    public int createBookmark(Bookmark bookmark){
        int bookmarkId = -1;
        ResultSet resultSet = null;
        try {
            //prepare the sql statement
            PreparedStatement pStatement = databaseConnection.prepareStatement("INSERT INTO Bookmark (object_id, object_title, " +
                    "object_author, object_date, user_id) VALUES(?, ?, ?, ?, ?);");
            //set parameters for the statement
            pStatement.setString(1, bookmark.getObjectId());
            pStatement.setString(2, bookmark.getObjectTitle());
            pStatement.setString(3, bookmark.getObjectAuthor());
            pStatement.setString(4, bookmark.getObjectDate());
            pStatement.setInt(5, bookmark.getUserId());
            //execute the query
            boolean result = pStatement.execute();

            bookmarkId = getBookmark(bookmark.getObjectId(), bookmark.getUserId()).getBookmarkId();

        } catch(SQLException e){
            System.out.println("Error inserting Bookmark: " + e.getMessage());
        } finally {
            try{
                if(resultSet != null){resultSet.close();}
            } catch (SQLException e){
                System.out.println("Couldn't close connection: " + e.getMessage());
            }

        }
        return bookmarkId;
    }

    //read methods
    public User getUser(String userEmail){
        User user = new User();

        try{
//            databaseConnection = java.sql.DriverManager.getConnection("jdbc:mysql://3.135.208.122/user_organised", "hugh", "AWS-mysql99");
            PreparedStatement pStatement = databaseConnection.prepareStatement("SELECT * FROM User WHERE user_email = ?;");
            //execute the query
            pStatement.setString(1, userEmail);
            ResultSet resultSet = pStatement.executeQuery();
            boolean hasNext = resultSet.next();
            if (!hasNext){
                return user;
            }
            //set the UserList variables
            user.setUserId(resultSet.getInt("user_id"));
            user.setUserEmail(resultSet.getString("user_email"));
            user.setUserName(resultSet.getString("user_name"));

        } catch (SQLException e){
            System.out.println("Sql Error occurred: " + e.getMessage());
        }
        return user;
    }

    public UserList getUserList(int userListId){
        UserList userList = new UserList();

        try{

            PreparedStatement pStatement = databaseConnection.prepareStatement("SELECT * FROM UserList WHERE user_list_id = ?;");
            //execute the query
            pStatement.setInt(1, userListId);
            ResultSet resultSet = pStatement.executeQuery();
            resultSet.next();

            //set the UserList variables
            userList.setUserListId(resultSet.getInt("user_list_id"));
            userList.setUserListName(resultSet.getString("name"));
            userList.setIsPrivate(resultSet.getBoolean("is_private"));
            userList.setDateCreated(resultSet.getDate("date_created"));
            userList.setDateModified(resultSet.getDate("date_modified"));
            userList.setUserId(resultSet.getInt("user_id"));

        } catch (SQLException e){
            System.out.println("Sql Error occurred: " + e.getMessage());
        }
        return userList;
    }

    public ArrayList<UserList> getUserLists(int userId){
        ArrayList<UserList> userLists = new ArrayList<UserList>();

        try{
            //create the sql statement
            PreparedStatement pStatement = databaseConnection.prepareStatement("SELECT * FROM UserList WHERE user_id = ?;");
            //execute the query
            pStatement.setInt(1, userId);
            ResultSet resultSet = pStatement.executeQuery();

            //set the UserList variables
            while(resultSet.next()){
                UserList userList = new UserList();
                userList.setUserListId(resultSet.getInt("user_list_id"));
                userList.setUserListName(resultSet.getString("name"));
                userList.setIsPrivate(resultSet.getBoolean("is_private"));
                userList.setDateCreated(resultSet.getDate("date_created"));
                userList.setDateModified(resultSet.getDate("date_modified"));
                userList.setUserId(resultSet.getInt("user_id"));
                //add the user list to the list of user lists
                userLists.add(userList);
            }

        } catch (SQLException e){
            System.out.println("Sql Error occurred: " + e.getMessage());
        }
        return userLists;
    }

    public ListItem getListItem(int listItemId){
        ListItem listItem = new ListItem();

        try{
            //create the sql statement
            PreparedStatement pStatement = databaseConnection.prepareStatement("SELECT * FROM ListItem WHERE list_item_id = ?;");
            //execute the query
            pStatement.setInt(1, listItemId);
            ResultSet resultSet = pStatement.executeQuery();
            resultSet.next();

            //set the ListItem variables
            listItem.setListItemId(resultSet.getInt("list_item_id"));
            listItem.setListObject(resultSet.getString("list_object"));
            listItem.setUserListId(resultSet.getInt("user_list_id"));

        } catch (SQLException e){
            System.out.println("Sql Error occurred: " + e.getMessage());
        }
        return listItem;
    }

    public ArrayList<ListItem> getListItems(int userListId){
        ArrayList<ListItem> listItems = new ArrayList<ListItem>();

        try{
            //create the sql statement
            PreparedStatement pStatement = databaseConnection.prepareStatement("SELECT * FROM ListItem WHERE user_list_id = ?;");
            //execute the query
            pStatement.setInt(1, userListId);
            ResultSet resultSet = pStatement.executeQuery();

            //set the ListItem variables
            while(resultSet.next()){
                ListItem listItem = new ListItem();
                listItem.setListItemId(resultSet.getInt("list_item_id"));
                listItem.setListObject(resultSet.getString("list_object"));
                listItem.setUserListId(resultSet.getInt("user_list_id"));
                //add the user list to the list of user lists
                listItems.add(listItem);
            }

        } catch (SQLException e){
            System.out.println("Sql Error occurred: " + e.getMessage());
        }
        return listItems;
    }

    public ArrayList<UserList> getUserListsContainingListItem(int listItemId){
        ArrayList<UserList> userLists = new ArrayList<UserList>();
        try{
            //create the sql statement
            Statement statement = databaseConnection.createStatement();
            //execute the query
            ResultSet resultSet = statement.executeQuery("SELECT * FROM UserList INNER JOIN ListItem ON " +
                    "UserList.UserListId = ListItem.UserListId " +
                    "WHERE ListItem.list_item_id =" + listItemId + "AND UserList.is_private = false;");

            //set the UserList variables
            while (resultSet.next()){
                UserList userList = new UserList();
                userList.setUserListId(resultSet.getInt("user_list_id"));
                userList.setUserListName(resultSet.getString("name"));
                userList.setIsPrivate(resultSet.getBoolean("is_private"));
                userList.setDateCreated(resultSet.getDate("date_created"));
                userList.setDateModified(resultSet.getDate("date_modified"));
                userList.setUserId(resultSet.getInt("user_id"));
                //add the user list to the list of user lists
                userLists.add(userList);

            }
        } catch(SQLException e){
            System.out.println("There was an error searching the lists: " + e);
        }
        return userLists;
    }

    public ArrayList<Bookmark> getBookmarks(int userId){
        ArrayList<Bookmark> bookmarks = new ArrayList<Bookmark>();

        try{
            //create the sql statement
            PreparedStatement pStatement = databaseConnection.prepareStatement("SELECT * FROM Bookmark WHERE user_id = ?;");
            //execute the query
            pStatement.setInt(1, userId);
            ResultSet resultSet = pStatement.executeQuery();

            //set the ListItem variables
            while(resultSet.next()){
                Bookmark bookmark = new Bookmark();
                bookmark.setBookmarkId(resultSet.getInt("bookmark_id"));
                bookmark.setObjectId(resultSet.getString("object_id"));
                bookmark.setObjectTitle(resultSet.getString("object_title"));
                bookmark.setObjectAuthor(resultSet.getString("object_author"));
                bookmark.setObjectDate(resultSet.getString("object_date"));
                bookmark.setUserId(resultSet.getInt("user_id"));
                //add the user list to the list of user lists
                bookmarks.add(bookmark);
            }

        } catch (SQLException e){
            System.out.println("Sql Error occurred: " + e.getMessage());
        }
        return bookmarks;
    }

    public Bookmark getBookmark(String objectId, int userId){
        Bookmark bookmark = new Bookmark();

        try{
//            databaseConnection = java.sql.DriverManager.getConnection("jdbc:mysql://3.135.208.122/user_organised", "hugh", "AWS-mysql99");
            PreparedStatement pStatement = databaseConnection.prepareStatement("SELECT * FROM Bookmark WHERE object_id = ? AND " +
                    "user_id = ?;");
            //execute the query
            pStatement.setString(1, objectId);
            pStatement.setInt(2, userId);
            ResultSet resultSet = pStatement.executeQuery();
            boolean hasNext = resultSet.next();
            if (!hasNext){
                return bookmark;
            }
            //set the bookmark variables if it exists
            bookmark.setBookmarkId(resultSet.getInt("bookmark_id"));
            bookmark.setObjectId(resultSet.getString("object_id"));
            bookmark.setObjectTitle(resultSet.getString("object_title"));
            bookmark.setObjectAuthor(resultSet.getString("object_author"));
            bookmark.setObjectDate(resultSet.getString("object_date"));
            bookmark.setUserId(resultSet.getInt("user_id"));

        } catch (SQLException e){
            System.out.println("Sql Error occurred: " + e.getMessage());
        }
        return bookmark;
    }
    //update methods
    public int updateUserList(UserList userList) {
        int userListId = -1;
        ResultSet resultSet = null;
        try {
            //prepare the sql statement
            PreparedStatement pStatement = databaseConnection.prepareStatement("UPDATE UserList SET name = ?, " +
                    "is_private = ?, date_created = ?, date_modified = ?, user_id = ? WHERE user_list_id = ?;", Statement.RETURN_GENERATED_KEYS);
            //set parameters for the statement
            pStatement.setString(1, userList.getUserListName());
            pStatement.setBoolean(2, userList.getIsPrivate());
            pStatement.setDate(3, (Date)userList.getDateCreated());
            pStatement.setDate(4, (Date)userList.getDateModified());
            pStatement.setInt(5, userList.getUserId());
            pStatement.setInt(6, userList.getUserListId());
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
            System.out.println("Error inserting UserList: " + e.getMessage());
        } finally {
            try{
                if(resultSet != null){resultSet.close();}
            } catch (SQLException e){
                System.out.println("Couldn't close connection: " + e.getMessage());
            }

        }
        return userListId;
    }

    public int updateListItem(ListItem listItem) {
        int listItemId = -1;
        ResultSet resultSet = null;
        try {
            //prepare the sql statement
            PreparedStatement pStatement = databaseConnection.prepareStatement("UPDATE ListItem " +
                    "SET list_object = ?, user_list_id = ? WHERE list_item_id = ?;", Statement.RETURN_GENERATED_KEYS);
            //set parameters for the statement
            pStatement.setString(1, listItem.getListObject());
            pStatement.setInt(2, listItem.getUserListId());
            pStatement.setInt(3, listItem.getListItemId());
            //execute the query
            boolean result = pStatement.execute();
            if (result == true){
                //get the userListId
                resultSet = pStatement.getGeneratedKeys();
                if (resultSet.next()){
                    listItemId = resultSet.getInt(1);
                }
            }
        } catch(SQLException e){
            System.out.println("Error inserting ListItem: " + e.getMessage());
        } finally {
            try{
                if(resultSet != null){resultSet.close();}
            } catch (SQLException e){
                System.out.println("Couldn't close connection: " + e.getMessage());
            }

        }
        return listItemId;
    }

    //delete methods
    public boolean deleteUser(int userId){
        boolean result = false;
        try{
            PreparedStatement pStatement = databaseConnection.prepareStatement("DELETE FROM User WHERE user_id = ?;");
            pStatement.setInt(1, userId);
            //execute the query
            result = pStatement.execute();
        } catch (SQLException e) {
            System.out.println("Sql Error occurred: " + e.getMessage());
        }
        return result;
    }

    public boolean deleteUserList(int userListId){
        boolean result = false;
        try{
            PreparedStatement pStatement = databaseConnection.prepareStatement("DELETE FROM UserList WHERE user_list_id = ?;");
            pStatement.setInt(1, userListId);
            //execute the query
            result = pStatement.execute();
        } catch (SQLException e) {
            System.out.println("Sql Error occurred: " + e);
        }
        return result;
    }

    public boolean deleteListItem(int listItemId){
        boolean result = false;
        try{
            PreparedStatement pStatement = databaseConnection.prepareStatement("DELETE FROM ListITem WHERE list_item_id = ?;");
            pStatement.setInt(1, listItemId);
            //execute the query
            result = pStatement.execute();
        } catch (SQLException e) {
            System.out.println("Sql Error occurred: " + e);
        }
        return result;
    }

    public boolean deleteBookmark(String objectId){
        boolean result = false;
        try{
            PreparedStatement pStatement = databaseConnection.prepareStatement("DELETE FROM Bookmark WHERE object_id = ?;");
            pStatement.setString(1, objectId);
            //execute the query
            result = pStatement.execute();
        } catch (SQLException e) {
            System.out.println("Sql Error occurred: " + e.getMessage());
        }
        return result;
    }



}
