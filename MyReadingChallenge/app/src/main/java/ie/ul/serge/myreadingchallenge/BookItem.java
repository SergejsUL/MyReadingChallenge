package ie.ul.serge.myreadingchallenge;

import java.util.Date;
import java.util.HashMap;

public class BookItem extends HashMap<String,Object> {




    //public static final String BOOK_ID ="bookID";
    public static final String	USERID = "userID";
    public static final String	TITLE ="title";
    public static final String	AUTHOR="author";
    //public static final String	PICTURE= "picture";
    //public static final String	STATUS = "status";
    //public static final String 	READING_NOW = "reading_now";
    //public static final String PAGES="pages";
    public static final String CREATED= "created";





public BookItem(String userID, String title, String author){

                this.put(USERID,userID);
                this.put(TITLE,title);
                this.put(AUTHOR,author);
                this.put(CREATED,new Date());

                }
    }
