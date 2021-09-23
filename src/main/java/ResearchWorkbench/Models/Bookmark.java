package ResearchWorkbench.Models;

import java.awt.print.Book;

public class Bookmark {
    int bookmarkId;
    String objectId;
    String objectTitle;
    String objectAuthor;
    String objectDate;
    int userId;

    /**
     * Base constructor
     */
    public Bookmark(){
        bookmarkId = 0;
        objectId = "null";
        objectTitle = "null";
        objectAuthor = "null";
        objectDate = "null";
        userId = 0;
    }

    /**
     * Constructor that takes no bookmark id
     * @param objectId the id of the resource
     * @param objectTitle the title of the resource
     * @param objectAuthor the author/creator of the resource
     * @param objectDate the date of the resource
     * @param userId the user id of the user creating the bookmark
     */
    public Bookmark(String objectId, String objectTitle, String objectAuthor, String objectDate, int userId){
        this(0, objectId, objectTitle, objectAuthor, objectDate, userId);
    }

    /**
     * Constructor that in the bookmark id
     * @param bookmarkId id of bookmark used for MySQL
     * @param objectId the id of the resource
     * @param objectTitle the title of the resource
     * @param objectAuthor the author/creator of the resource
     * @param objectDate the date of the resource
     * @param userId the user id of the user creating the bookmark
     */
    public Bookmark(int bookmarkId, String objectId, String objectTitle, String objectAuthor, String objectDate, int userId){
        this.bookmarkId = bookmarkId;
        this.objectId = objectId;
        this.objectTitle = objectTitle;
        this.objectAuthor = objectAuthor;
        this.objectDate = objectDate;
        this.userId = userId;
    }

    /**
     * Read the id of the bookmark
     * @return the id of the bookmark
     */
    public int getBookmarkId() {
        return bookmarkId;
    }

    /**
     * Read the id of the resource
     * @return returns the id of the resource
     */
    public String getObjectId() {
        return objectId;
    }

    /**
     * Read the title of the resource
     * @return returns title of the resource
     */
    public String getObjectTitle() {
        return objectTitle;
    }

    /**
     * Read the author/creator of the resource
     * @return returns the author/creator of the resource
     */
    public String getObjectAuthor() {
        return objectAuthor;
    }

    /**
     * Read the date of the resource
     * @return returns the date of the resource
     */
    public String getObjectDate() {
        return objectDate;
    }

    /**
     * Read the id of the user who created the bookmark
     * @return the id of the user
     */
    public int getUserId() {
        return userId;
    }

    /**
     * Set the id of the bookmark
     * @param bookmarkId the id to set the bookmarkId to
     */
    public void setBookmarkId(int bookmarkId) {
        this.bookmarkId = bookmarkId;
    }

    /**
     * Set the id of the bookmarked resource
     * @param objectId the id to set the object id to
     */
    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    /**
     * Set the title of the bookmarked resource
     * @param objectTitle
     */
    public void setObjectTitle(String objectTitle) {
        this.objectTitle = objectTitle;
    }

    /**
     * Set the author/creator of the bookmarked resource
     * @param objectAuthor the author of the bookmarked resource
     */
    public void setObjectAuthor(String objectAuthor) {
        this.objectAuthor = objectAuthor;
    }

    /**
     * Set the date of the bookmarked resource
     * @param objectDate the date of the bookmarked resource
     */
    public void setObjectDate(String objectDate) {
        this.objectDate = objectDate;
    }

    /**
     * Set the user id of the bookmarked resource
     * @param userId the user id of the user that created the bookmark
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }
}
