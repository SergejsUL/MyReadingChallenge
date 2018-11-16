package ie.ul.serge.myreadingchallenge;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Date;
import java.util.HashMap;

public class BookItem extends HashMap<String,Object> {


    //public static final String	PICTURE= "picture";
    //public static final String	STATUS = "status";
    //public static final String 	READING_NOW = "reading_now";
    //public static final String PAGES="pages";






public BookItem(String userID, String title, String author){

                this.put(Constants.KEY_USERID,userID);
                this.put(Constants.KEY_BOOK_TITLE,title);
                this.put(Constants.KEY_BOOK_AUTHOR,author);
                this.put(Constants.KEY_CREATED,new Date());

                }
    }
