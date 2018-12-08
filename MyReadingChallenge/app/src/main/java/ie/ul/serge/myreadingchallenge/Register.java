package ie.ul.serge.myreadingchallenge;

import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.GregorianCalendar;
import java.util.HashMap;

public class Register extends AppCompatActivity {

    CalendarView mExpandableCallendar;
    TextView mShowCallendar,mNameTextView,mEmailTextview,mPasswordTextview;
    ImageView mUserImageview;
    FirebaseFirestore db;
    GregorianCalendar callendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        mExpandableCallendar=findViewById(R.id.expanded_callendar_view);
        mShowCallendar=findViewById(R.id.expand_callendar_view);
        mNameTextView=findViewById(R.id.name_edittext);
        mEmailTextview=findViewById(R.id.email_edittext);
        mPasswordTextview=findViewById(R.id.password_edittext);


//code to show or hide callendar when date is picked
        mExpandableCallendar.setVisibility(View.GONE);
        mExpandableCallendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                mShowCallendar.setText((String.valueOf(dayOfMonth)+"/"+(String.valueOf(month)+"/"+(String.valueOf(year)))));
                mExpandableCallendar.setVisibility(View.GONE);
            }
        });

        mShowCallendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mExpandableCallendar.setVisibility(mExpandableCallendar.isShown()
                ?View.GONE
                :View.VISIBLE);
            }
        });
//END of _ code to show or hide callendar when date is picked

    }

    public void handleRegister(View view){
        HashMap<String,Object> user = new HashMap<>();


    }
}
