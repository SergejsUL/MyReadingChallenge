package ie.ul.serge.myreadingchallenge;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;
import java.util.GregorianCalendar;

public class UserDetails extends AppCompatActivity {

    private   TextView mName,mAge;
    private   ImageView mUserPic;
    private  RatingBar mRating;
    private   FirebaseStorage mStorage;
    private   FirebaseFirestore mDB;
    private   FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        mName = findViewById(R.id.cud_userNameTextview);
        mAge=findViewById(R.id.cud_userAgeTextview);
        mUserPic=findViewById(R.id.cud_user_imageView);

        mDB = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mStorage = FirebaseStorage.getInstance();

        //PULL user details from Firebase

        String uid= mAuth.getCurrentUser().getUid();
        DocumentReference userDoc = mDB.collection(Constants.USERS_COLLECTION).document(uid);
        userDoc.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
                if(e!=null){
                    makeMeToast("Failed to connect to Database. Working offline");
                    return;
                }

              String userName = (String) documentSnapshot.get(Constants.KEY_USERNAME);

                //Calculate and display age

                Date dob  = (Date) documentSnapshot.get(Constants.KEY_DOB);
                LocalDate dbDate = dob.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                LocalDate today = LocalDate.now();

                mAge.setText( Helper.calculateAge(dbDate,today)+"");
                mName.setText(userName);

                
            }
        });



    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();}
        return super.onOptionsItemSelected(item);
    }

    public void makeMeToast(String text){
        Toast.makeText(this,text,Toast.LENGTH_LONG).show();
    }
}
