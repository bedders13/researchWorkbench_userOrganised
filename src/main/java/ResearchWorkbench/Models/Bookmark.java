package ResearchWorkbench.Models;

import java.awt.print.Book;

public class Bookmark {
    int bookmarkId;
    String objectId;
    int userId;

    public Bookmark(){
        bookmarkId = 0;
        objectId = "null";
        userId = 0;
    }

    public Bookmark(String objectId, int userId){
        this(0, objectId, userId);
    }

    public Bookmark(int bookmarkId, String objectId, int userId){
        this.bookmarkId = bookmarkId;
        this.objectId = objectId;
        this.userId = userId;
    }

    public int getBookmarkId() {
        return bookmarkId;
    }

    public String getObjectId() {
        return objectId;
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

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
