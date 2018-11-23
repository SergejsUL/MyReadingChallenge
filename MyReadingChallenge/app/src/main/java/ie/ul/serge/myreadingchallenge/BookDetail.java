package ie.ul.serge.myreadingchallenge;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;

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
    private long mPages, mPagestoAdd;
    private DocumentSnapshot mDocSnap;
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

                  mPages = (Long) mDocSnap.get(Constants.KEY_BOOK_PAGES);
                  mPagesTextView.setText(mPages +"");

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
                mPages+=mPagestoAdd;

                addPagestoBook();
            }
        });
    }

    private void addPagestoBook() {
        Map<String, Object> bk = new HashMap<>();
        bk.put(Constants.KEY_BOOK_PAGES,mPages);
        bk.put(Constants.KEY_CREATED,new Date());
        mDocRef.update(bk);



    }


}
