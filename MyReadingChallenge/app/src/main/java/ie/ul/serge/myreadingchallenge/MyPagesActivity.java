package ie.ul.serge.myreadingchallenge;

import android.renderscript.Allocation;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MyPagesActivity extends AppCompatActivity {
    FirebaseFirestore db;
    FirebaseAuth mAuth;
    List<DocumentSnapshot> mUserPageList = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_pages);
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        mAuth = FirebaseAuth.getInstance();
        db=FirebaseFirestore.getInstance();
        String uid = mAuth.getCurrentUser().getUid();

        CollectionReference userPagesCollectionRef = db.collection(Constants.USERS_COLLECTION)
                .document(uid).collection(Constants.PAGES_COLLECTION);

        userPagesCollectionRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                if(e!=null){
                    Toast.makeText(MyPagesActivity.this,"Could not connect",Toast.LENGTH_LONG).show();
                    return;
                }
                mUserPageList =  documentSnapshots.getDocuments();
                getdoc();
            }
        });



//        Calendar monthStart = Calendar.getInstance();
//        Calendar weekStart = Calendar.getInstance();
//        monthStart.set(Calendar.DAY_OF_MONTH,1);
//        weekStart.set(Calendar.DAY_OF_WEEK, weekStart.getFirstDayOfWeek());



       //long i = (Long) docSnap.get(Constants.KEY_BOOK_PAGES);

        //Date docDate = (Date)docSnap.get(Constants.KEY_CREATED);
//        if(docDate.before(weekStart.getTime())){
//            Toast.makeText(this,docDate+""+weekStart.getTime()+"",Toast.LENGTH_LONG).show();
//        }

           // Toast.makeText(this,docDate+""+weekStart.getTime()+"",Toast.LENGTH_LONG).show();


    }

    private void getdoc() {
        long docPage = (Long) mUserPageList.get(2).get(Constants.KEY_BOOK_PAGES);
        Date docDate = (Date)mUserPageList.get(2).get(Constants.KEY_CREATED);
        Toast.makeText(this,docPage+""+docDate,Toast.LENGTH_LONG).show();

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();}
        return super.onOptionsItemSelected(item);
    }
}
