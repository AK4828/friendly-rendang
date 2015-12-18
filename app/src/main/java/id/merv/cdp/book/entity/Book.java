package id.merv.cdp.book.entity;

/**
 * Created by akm on 25/11/15.
 */
public class Book {

    private String bookTitle;
    private int bookThumbnail;
    private String bookDescription;



    public String getBookDescription() {
        return bookDescription;
    }

    public void setBookDescription(String bookDescription) {
        this.bookDescription = bookDescription;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public int getBookThumbnail() {
        return bookThumbnail;
    }

    public void setBookThumbnail(int bookThumbnail) {
        this.bookThumbnail = bookThumbnail;
    }




}
