package ie.ul.serge.myreadingchallenge;

import android.util.Log;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class PagesLog {

    private FirebaseFirestore db;
    private DocumentSnapshot mDocSnap;
    private DocumentReference mDocRef;
    private String error;

    private String mCeated;
    private long mPages;
    private String mBookName;

    private List<DocumentSnapshot> mUserPageSnaps = new ArrayList<>();




    PagesLog(){
        db = FirebaseFirestore.getInstance();

    }

    public void logPages(DocumentReference collectionRef, long numberPages,String bookTitle)
    {
        //TODO: finish this method for adding pages to user pages collection.
        HashMap<String , Object> pageLog= new HashMap<>();
        pageLog.put(Constants.KEY_CREATED,new Date());
        pageLog.put(Constants.KEY_BOOK_PAGES,numberPages);
        pageLog.put(Constants.KEY_BOOK_TITLE,bookTitle);
    }


    public FirebaseFirestore getDb() {
        return db;
    }

    public List<DocumentSnapshot> getUserPageSnaps(CollectionReference userPagesRef){

        userPagesRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                if(e!=null){
                   error = "Listening failed";
                    return;
                }
                mUserPageSnaps =  documentSnapshots.getDocuments();
            }
        });

       return mUserPageSnaps;
    }
}
