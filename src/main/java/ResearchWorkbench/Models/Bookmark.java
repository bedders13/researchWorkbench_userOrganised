package ResearchWorkbench.Models;

import java.awt.print.Book;

public class Bookmark {
    int bookmarkId;
    String objectId;
    String objectTitle;
    String objectAuthor;
    String objectDate;
    int userId;

    public Bookmark(){
        bookmarkId = 0;
        objectId = "null";
        objectTitle = "null";
        objectAuthor = "null";
        objectDate = "null";
        userId = 0;
    }

    public Bookmark(String objectId, String objectTitle, String objectAuthor, String objectDate, int userId){
        this(0, objectId, objectTitle, objectAuthor, objectDate, userId);
    }

    public Bookmark(int bookmarkId, String objectId, String objectTitle, String objectAuthor, String objectDate, int userId){
        this.bookmarkId = bookmarkId;
        this.objectId = objectId;
        this.objectTitle = objectTitle;
        this.objectAuthor = objectAuthor;
        this.objectDate = objectDate;
        this.userId = userId;
    }

    public int getBookmarkId() {
        return bookmarkId;
    }

    public String getObjectId() {
        return objectId;
    }

    public String getObjectTitle() {
        return objectTitle;
    }

    public String getObjectAuthor() {
        return objectAuthor;
    }

    public String getObjectDate() {
        return objectDate;
    }

    public int getUserId() {
        return userId;
    }

    public void setBookmarkId(int bookmarkId) {
        this.bookmarkId = bookmarkId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public void setObjectTitle(String objectTitle) {
        this.objectTitle = objectTitle;
    }

    public void setObjectAuthor(String objectAuthor) {
        this.objectAuthor = objectAuthor;
    }

    public void setObjectDate(String objectDate) {
        this.objectDate = objectDate;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
