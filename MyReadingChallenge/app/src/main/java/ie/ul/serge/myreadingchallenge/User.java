package ie.ul.serge.myreadingchallenge;
import java.util.GregorianCalendar;
import java.util.HashMap;

public class User {

    private String mName,mUID,mImageURL;
    private long mPages;
    private GregorianCalendar mDOB;
    private HashMap<String,Object> mUser =new HashMap<>();

    public String getmName() {
        return mName;
    }

    public String getmUID() {
        return mUID;
    }

    public String getmImageURL() {
        return mImageURL;
    }

    public GregorianCalendar getmDOB() {
        return mDOB;
    }

    public HashMap<String, Object> getmUser() {

        mUser.put(Constants.KEY_USERNAME,mName);
        mUser.put(Constants.KEY_DOB,mDOB.getTime());
        mUser.put(Constants.KEY_USERID,mUID);
        mUser.put(Constants.KEY_USERPIC_URL,mImageURL);
        mUser.put(Constants.KEY_BOOK_PAGES,mPages);

        return mUser;
    }

    public User(String mName, GregorianCalendar mDOB, String mUID, String mImageURL) {
        this.mName = mName;
        this.mDOB = mDOB;
        this.mUID = mUID;
        this.mImageURL = mImageURL;
        this.mPages = 0;


    }





}
