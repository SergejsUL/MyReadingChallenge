package ie.ul.serge.myreadingchallenge;

import android.renderscript.Allocation;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
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
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

public class MyPagesActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private List<DocumentSnapshot> mUserPageList = new ArrayList<>();

    private Calendar mMonthStart;
    private Calendar mWeekStart;
    private Calendar mToday;

    long mTodayPages;
    long mWeekPages;
    long mMonthPages;



    private TextView mTextViewTodayPg;
    private TextView mTextViewWkPg;
    private TextView mTextViewMnthPg;
    private TextView mReadTopBanner;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_pages);
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        mMonthStart = new GregorianCalendar();
        mMonthStart.set(Calendar.DAY_OF_MONTH,0);

        mWeekStart= new GregorianCalendar();
        mWeekStart.set(Calendar.DAY_OF_WEEK, mWeekStart.getFirstDayOfWeek());

        mToday = new GregorianCalendar();
        mToday.add(mToday.DATE,0);




        mAuth = FirebaseAuth.getInstance();
        db=FirebaseFirestore.getInstance();
        String uid = mAuth.getCurrentUser().getUid();

        CollectionReference userPagesCollectionRef = db.collection(Constants.USERS_COLLECTION)
                .document(uid).collection(Constants.PAGES_COLLECTION);

        userPagesCollectionRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                if(e!=null){
                    Toast.makeText(MyPagesActivity.this,"Could not connect to database",Toast.LENGTH_LONG).show();
                    return;
                }
                mUserPageList =  documentSnapshots.getDocuments();
                getPages();
                updateView();
            }
        });

    }

    private void getPages() {


//        for (DocumentSnapshot doc : mUserPageList) {
//            Date docDate = (Date) doc.get(Constants.KEY_CREATED);
//
//            if(docDate.after(mToday.getTime())){
//                mTodayPages+= (Long) doc.get(Constants.KEY_BOOK_PAGES);
//            }
//            if(docDate.after(mWeekStart.getTime())){
//                mWeekPages+= (Long) doc.get(Constants.KEY_BOOK_PAGES);
//            }
//            if(docDate.after(mMonthStart.getTime())){
//                mMonthPages+= (Long) doc.get(Constants.KEY_BOOK_PAGES);
//
//            }
//        }

        for (DocumentSnapshot doc : mUserPageList) {
            Date docDate = (Date) doc.get(Constants.KEY_CREATED);

            if(docDate.after(getDateWithoutTimeUsingCalendar(mToday))){
                mTodayPages+= (Long) doc.get(Constants.KEY_BOOK_PAGES);
            }
            if(docDate.after(getDateWithoutTimeUsingCalendar(mWeekStart))){
                mWeekPages+= (Long) doc.get(Constants.KEY_BOOK_PAGES);
            }
            if(docDate.after(getDateWithoutTimeUsingCalendar(mMonthStart))){
                mMonthPages+= (Long) doc.get(Constants.KEY_BOOK_PAGES);

            }
        }

    }

    private void updateView() {
        mTextViewTodayPg = findViewById(R.id.read_day_textview);
        mTextViewWkPg = findViewById(R.id.read_week_textview);
        mTextViewMnthPg = findViewById(R.id.read_month_textview);
        mReadTopBanner=findViewById(R.id.read_top_banner);

        String bannerText = "Hello " + mAuth.getCurrentUser().getEmail()+". \nYou are mmaking great progress this year." +
                "\nSee your reading stats below";

        mReadTopBanner.setText(bannerText);
        mTextViewTodayPg.setText(mTodayPages+"");
        mTextViewWkPg.setText(mWeekPages+"");
        mTextViewMnthPg.setText(mMonthPages+"");
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();}
        return super.onOptionsItemSelected(item);
    }

    public  Date getDateWithoutTimeUsingCalendar(Calendar calendar) {

        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTime();
    }
}
