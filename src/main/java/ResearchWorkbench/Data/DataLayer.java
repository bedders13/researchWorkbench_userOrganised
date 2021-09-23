package ResearchWorkbench.Data;

import ResearchWorkbench.Models.Bookmark;
import ResearchWorkbench.Models.ListItem;
import ResearchWorkbench.Models.User;
import ResearchWorkbench.Models.UserList;
import java.sql.*;
import java.util.ArrayList;

public class DataLayer {
    protected java.sql.Connection databaseConnection;

    /**
     * Simple constructor
     */
    public DataLayer(){
        databaseConnection = null;
    }

    /**
     * Constructor calls initConnection() to create database connection when an instance is created
     * @param connectionString the string that is used to connect to the MySQL database
     * @param databaseUser the user for the database
     * @param databaseUserPassword the user password for the database
     */
    public DataLayer(String connectionString, String databaseUser, String databaseUserPassword) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.initConnection(connectionString, databaseUser, databaseUserPassword);
        } catch (Exception e) {
            System.out.println("JDBC driver not found");
        }
    }

    /**
     * Initiates the connection with the database using JDBC
     * @param connectionString the string that is used to connect to the MySQL database
     * @param databaseUser the user for the database
     * @param databaseUserPassword the user password for the database
     */
    public void initConnection(String connectionString, String databaseUser, String databaseUserPassword) {
        try {
            databaseConnection = java.sql.DriverManager.getConnection(connectionString, databaseUser, databaseUserPassword);
        } catch (Exception e) {
            System.out.println("Error. Couldn't open database");
        }
    }

    //create methods region

    /**
     * Inserts a row into the User table
     * @param user user object of the user that needs to be created in the database
     * @return returns the userId of the newly created row of User in the database
     */
    public int createUser(User user){
        int userId = -1;
        try {
            userId = getUser(user.getUserEmail()).getUserId();
            if (userId != 0){
                return -1;
            }
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

    /**
     * Inserts data into the UserList table
     * @param userList userList object of the user list that needs to be created in the database
     * @return returns the userListId of the newly created row of UserList in the database
     */
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

    /**
     * Inserts data into the ListItem table
     * @param listItem listItem object that needs to be created in the database
     * @return returns the listItemId of the newly created row of ListItem in the database
     */
    public int createListItem(ListItem listItem) {
        int listItemId = -1;
        ResultSet resultSet = null;
        try {
            listItemId = getListItem(listItem.getListObjectId(), listItem.getUserListId()).getListItemId();
            if (listItemId != 0){
                return -1;
            }
            //prepare the sql statement
            PreparedStatement pStatement = databaseConnection.prepareStatement("INSERT INTO ListItem (object_id, object_title, " +
                    "object_author, object_date, user_list_id) VALUES(?, ?, ?, ?, ?);");
            //set parameters for the statement
            pStatement.setString(1, listItem.getListObjectId());
            pStatement.setString(2, listItem.getObjectTitle());
            pStatement.setString(3, listItem.getObjectAuthor());
            pStatement.setString(4, listItem.getObjectDate());
            pStatement.setInt(5, listItem.getUserListId());
            //execute the query
            boolean result = pStatement.execute();

            listItemId = getListItem(listItem.getListObjectId(), listItem.getUserListId()).getListItemId();

        } catch(SQLException e){
            System.out.println("Error inserting Bookmark: " + e.getMessage());
        } finally {
            try{
                if(resultSet != null){resultSet.close();}
            } catch (SQLException e){
                System.out.println("Couldn't close connection: " + e.getMessage());
            }

        }
        return listItemId;
    }

    /**
     * Inserts data into the Bookmark table
     * @param bookmark bookmark object that needs to be saved in the database
     * @return returns the bookmarkId of the newly created row of Bookmark in the database
     */
    public int createBookmark(Bookmark bookmark){
        int bookmarkId = -1;
        ResultSet resultSet = null;
        try {
            bookmarkId = getBookmark(bookmark.getObjectId(), bookmark.getUserId()).getBookmarkId();
            if (bookmarkId != 0){
                return -1;
            }
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

    //read methods region

    /**
     * Retrieve user info from the User table
     * @param userEmail the email address of the User that needs to be fetched from the database
     * @return returns a User object
     */
    public User getUser(String userEmail){
        User user = new User();

        try{
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

    /**
     * Retrieve user info from the User table
     * @param userId the userId of the User that needs to be fetched from the database
     * @return returns a User object
     */
    public User getUser(int userId){
        User user = new User();

        try{
            PreparedStatement pStatement = databaseConnection.prepareStatement("SELECT * FROM User WHERE user_id = ?;");
            //execute the query
            pStatement.setInt(1, userId);
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

    /**
     * Retrieve user list data from the UserList table
     * @param userListId the userListId of the UserList that needs to be fetched from the database
     * @return returns a UserList object
     */
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

    /**
     * Retrieve all user lists from the UserList table
     * @param userId the userId of the user that is retrieving all the lists
     * @return returns an ArrayList of all the UserList objects
     */
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

    /**
     * Retrieve the user's lists from the UserList table
     * @param objectId the objectId of the listItem that needs to be fetched
     * @param userListId the userListId of the listItem that needs to be fetched
     * @return returns a ListItem object
     */
    public ListItem getListItem(String objectId, int userListId){
        ListItem listItem = new ListItem();

        try{
            PreparedStatement pStatement = databaseConnection.prepareStatement("SELECT * FROM ListItem WHERE object_id = ? AND " +
                    "user_list_id = ?;");
            //execute the query
            pStatement.setString(1, objectId);
            pStatement.setInt(2, userListId);
            ResultSet resultSet = pStatement.executeQuery();
            if (!resultSet.next()){
                return listItem;
            }
            //set the bookmark variables if it exists
            listItem.setListItemId(resultSet.getInt("list_item_id"));
            listItem.setListObjectId(resultSet.getString("object_id"));
            listItem.setObjectTitle(resultSet.getString("object_title"));
            listItem.setObjectAuthor(resultSet.getString("object_author"));
            listItem.setObjectDate(resultSet.getString("object_date"));
            listItem.setUserListId(resultSet.getInt("user_list_id"));

        } catch (SQLException e){
            System.out.println("Sql Error occurred: " + e.getMessage());
        }
        return listItem;
    }

    /**
     * Retrieve all the items on a user list
     * @param userListId the userListId of the list that contains all the items
     * @return returns an ArrayList of all the ListItem objects
     */
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
                listItem.setListObjectId(resultSet.getString("object_id"));
                listItem.setObjectTitle(resultSet.getString("object_title"));
                listItem.setObjectAuthor(resultSet.getString("object_author"));
                listItem.setObjectDate(resultSet.getString("object_date"));
                listItem.setUserListId(resultSet.getInt("user_list_id"));
                //add the user list to the list of user lists
                listItems.add(listItem);
            }

        } catch (SQLException e){
            System.out.println("Sql Error occurred: " + e.getMessage());
        }
        return listItems;
    }

    /**
     * Retrieve all user lists that contain a given list item
     * @param objectId objectId of the ListItem that is searched for on other user lists
     * @return returns an ArrayList of UserList objects that contain the list item with the given objectId
     */
    public ArrayList<UserList> getUserListsContainingListItem(String objectId){
        ArrayList<UserList> userLists = new ArrayList<UserList>();
        try{
            //create the sql statement
            PreparedStatement preparedStatement = databaseConnection.prepareStatement("SELECT DISTINCT * FROM UserList INNER JOIN ListItem ON " +
                    "UserList.user_list_id = ListItem.user_list_id " +
                    "WHERE ListItem.object_id = ? AND UserList.is_private = false;");
            //execute the query
            preparedStatement.setString(1, objectId);
            ResultSet resultSet = preparedStatement.executeQuery();

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

    /**
     * Retrieve all bookmarks for a user
     * @param userId the userId of User of the bookmarks that need to be fetched
     * @return returns an ArrayList of bookmark objects
     */
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

    /**
     * Retrieve a bookmark from the Bookmark table
     * @param objectId the objectId of the bookmark that needs to be fetched
     * @param userId the userId of the bookmark
     * @return returns a Bookmark object
     */
    public Bookmark getBookmark(String objectId, int userId){
        Bookmark bookmark = new Bookmark();

        try{
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

    //update methods region

    /**
     * Update the user list in the UserList table
     * @param userList userList object that needs to be updated in the database
     * @return returns 1 if successful
     */
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

    //delete methods region

    /**
     * Delete a user from the User table
     * @param userId the userId of the User that needs to be deleted
     * @return returns true/false depending on delete success
     */
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

    /**
     * Delete a user list from the UserList table
     * @param userListId the userListId of the user list that needs to be deleted
     * @param userId the userId of the user that is deleting the list
     * @return returns true/false depending on delete success
     */
    public boolean deleteUserList(int userListId, int userId){
        boolean result = false;
        try{
            PreparedStatement pStatement = databaseConnection.prepareStatement("DELETE FROM UserList WHERE user_list_id = ? " +
                    "AND user_id = ?;");
            pStatement.setInt(1, userListId);
            pStatement.setInt(2, userId);
            //execute the query
            result = pStatement.execute();
        } catch (SQLException e) {
            System.out.println("Sql Error occurred: " + e);
        }
        return result;
    }

    /**
     * Delete a list item from the list item table
     * @param objectId the objectId of the list item that needs to be deleted
     * @param userListId the userListId of the user list the item needs to be deleted from
     * @return returns true/false depending on delete success
     */
    public boolean deleteListItem(String objectId, int userListId){
        boolean result = false;
        try{
            PreparedStatement pStatement = databaseConnection.prepareStatement("DELETE FROM ListItem WHERE object_id = ? " +
                    "AND user_list_id = ?;");
            pStatement.setString(1, objectId);
            pStatement.setInt(2, userListId);
            //execute the query
            result = pStatement.execute();
        } catch (SQLException e) {
            System.out.println("Sql Error occurred: " + e.getMessage());
        }
        return result;
    }

    /**
     * Delete a bookmark from the Bookmark table
     * @param objectId the objectId of the item that needs to be removed from bookmarks
     * @param userId the userId of the user that is removing the bookmark
     * @return returns true/false depending on delete success
     */
    public boolean deleteBookmark(String objectId, int userId){
        boolean result = false;
        try{
            PreparedStatement pStatement = databaseConnection.prepareStatement("DELETE FROM Bookmark WHERE object_id = ? " +
                    "AND user_id = ?;");
            pStatement.setString(1, objectId);
            pStatement.setInt(2, userId);
            //execute the query
            result = pStatement.execute();
        } catch (SQLException e) {
            System.out.println("Sql Error occurred: " + e.getMessage());
        }
        return result;
    }



}
