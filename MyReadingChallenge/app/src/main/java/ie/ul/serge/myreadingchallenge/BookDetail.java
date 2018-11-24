package ie.ul.serge.myreadingchallenge;

import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.constraint.solver.widgets.Snapshot;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class BookDetail extends AppCompatActivity {

    private TextView mTitleTextView,mAuthorTextView,mPagesTextView;
    private Button addBtn;
    private long mBookPages,mPagestoAdd;
    private long mUserPages;
    private DocumentSnapshot mDocSnap,mUserDocSnap;
    private DocumentReference mDocRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mTitleTextView = findViewById(R.id.TextView_title);
        mAuthorTextView = findViewById(R.id.TextView_author);
        mPagesTextView= findViewById(R.id.TextView_pages_read);

        Intent receivedIntent = getIntent();
        String docID = receivedIntent.getStringExtra(Constants.EXTRA_DOC_ID);

        mDocRef = FirebaseFirestore.getInstance()
                .collection(Constants.BOOK_COLLECTION).document(docID);
        mDocRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
              if(e!=null){
                  Log.w(Constants.TAG,"Listening failed");
                  return;
              }
              if(documentSnapshot.exists()){
                  mDocSnap = documentSnapshot;
                  mTitleTextView.setText((String)mDocSnap.get(Constants.KEY_BOOK_TITLE));
                  mAuthorTextView.setText((String)mDocSnap.get(Constants.KEY_BOOK_AUTHOR));

                  mBookPages = (Long) mDocSnap.get(Constants.KEY_BOOK_PAGES);
                  mPagesTextView.setText(mBookPages +"");

              }
            }
        });

       numberPicker();
    }

    private void numberPicker() {
        final NumberPicker picker = findViewById(R.id.input_number_picker);
        picker.setMinValue(1);
        picker.setMaxValue(100);

        addBtn = findViewById(R.id.btn_add_pages);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPagestoAdd = picker.getValue();
                Date mDateRead = new Date();
                mBookPages +=mPagestoAdd;

                addPagestoBook();
                addPagesToChallenge();

            }
        });
    }

    private void addPagestoBook() {
        Map<String, Object> bk = new HashMap<>();
        bk.put(Constants.KEY_BOOK_PAGES, mBookPages);
        bk.put(Constants.KEY_CREATED,new Date());
        mDocRef.update(bk);

    }

    private void addPagesToChallenge() {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DocumentReference userDocRef = FirebaseFirestore.getInstance()
                .collection(Constants.USERS_COLLECTION).document(uid);
        userDocRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
                if(e!=null){
                    Toast.makeText(BookDetail.this,"ListeningFailed",Toast.LENGTH_LONG).show();
                    Log.w(Constants.TAG,"Listening failed");
                    return;
                }
                if(documentSnapshot.exists()){
                    mUserDocSnap = documentSnapshot;
                    mUserPages = (Long)mUserDocSnap.get(Constants.KEY_BOOK_PAGES);
                }
            }
        });

        Map<String, Object> up = new HashMap<>();
        up.put(Constants.KEY_BOOK_PAGES,mUserPages+mPagestoAdd);
        userDocRef.update(up);
    }

}
