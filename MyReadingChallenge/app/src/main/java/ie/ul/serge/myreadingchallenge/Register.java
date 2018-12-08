package ie.ul.serge.myreadingchallenge;

import android.content.Intent;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.GregorianCalendar;
import java.util.HashMap;

public class Register extends AppCompatActivity {

    CalendarView mExpandableCallendar;
    TextView mShowCallendar,mNameTextView,mEmailTextview,mPasswordTextview;
    ImageView mUserImageview;
    FirebaseFirestore mDB;
    FirebaseAuth mAuth;
    GregorianCalendar mDOB;
    User mNewUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth=FirebaseAuth.getInstance();
        mDB = FirebaseFirestore.getInstance();


        mExpandableCallendar=findViewById(R.id.expanded_callendar_view);
        mShowCallendar=findViewById(R.id.expand_callendar_view);
        mNameTextView=findViewById(R.id.name_edittext);
        mEmailTextview=findViewById(R.id.email_edittext);
        mPasswordTextview=findViewById(R.id.password_edittext);
        mDOB = new GregorianCalendar();


//code to show or hide callendar when date is picked
        mExpandableCallendar.setVisibility(View.GONE);
        mExpandableCallendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {

                mDOB.set(year, month, dayOfMonth);

                mShowCallendar.setText((String.valueOf(dayOfMonth)+"/"+(String.valueOf(month+1)+"/"+(String.valueOf(year)))));
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

       createNewUser();
    }

    private void createNewUser() {


        String email = mEmailTextview.getText().toString();
        String password = mPasswordTextview.getText().toString();
        String name = mNameTextView.getText().toString();

        if(email.length()<5||!email.contains("@")){
            mEmailTextview.setError(getString(R.string.invalid_email));
        }else if (password.length()<6) {
            mPasswordTextview.setError(getString(R.string.invalid_password));
        }else if (name.length()<2){
            mNameTextView.setError(getString(R.string.invalid_name));
        }




        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            updateUserProfile();


                            Intent intent = new Intent(Register.this,MainActivity.class);
                            startActivity(intent);

                        }else{
                            Toast.makeText(Register.this,"Unable to regiser new user",Toast.LENGTH_SHORT).show();
                        }

                    }
                });

    }

    private void updateUserProfile() {

        String name = mNameTextView.getText().toString();
        String uid= mAuth.getUid();
        String picURL = "Image URL";//TODO image URL
        GregorianCalendar dob = mDOB;

        mNewUser= new User(name,dob,uid,picURL);

        //Create new user and return user id



        CollectionReference userColRef = mDB
                .collection(Constants.USERS_COLLECTION);
        userColRef.document(uid).set(mNewUser.getmUser());

    }


}
