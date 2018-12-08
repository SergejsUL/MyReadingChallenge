package ie.ul.serge.myreadingchallenge;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {
    private EditText mEmailEditText;
    private EditText mPasswordEditText;
    private FirebaseAuth mAuth;
    private TextView mForgotPass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    mEmailEditText = findViewById(R.id.email_edittext);
    mPasswordEditText=findViewById(R.id.password_edittext);
    mForgotPass = findViewById(R.id.reset_password_Textview);


    //set action to reset password option.
    mForgotPass.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Context context = LoginActivity.this;
            Intent intent = new Intent(context,ResetPassword.class);
            context.startActivity(intent);

        }
    });



    mAuth=FirebaseAuth.getInstance();

    // CHECK IF USER IS ALREADY LOGGED IN AND SKIP LOGIN.
        if (mAuth.getCurrentUser() !=null){
            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
            startActivity(intent);
        }

    }//END OF ON CREATE


    //set action to register new user

    public void handleRegister (View view){
        Context context = LoginActivity.this;
        Intent intent = new Intent(context,Register.class);
        context.startActivity(intent);

    }

    public void handleSignIn(View view){
        loginAction(true);
    }
    public void handleSignUp(View view){
        loginAction(false);
    }

    public void loginAction(boolean isSignIn)
    {

        String email = mEmailEditText.getText().toString();
        String password = mPasswordEditText.getText().toString();
        if(email.length()<5||!email.contains("@")){
            mPasswordEditText.setError(getString(R.string.invalid_email));
        }else if (password.length()<6){
            mPasswordEditText.setError(getString(R.string.invalid_password));
        } else if (isSignIn){
            mAuth.signInWithEmailAndPassword(email,password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                                startActivity(intent);

                            }else{
                                Toast.makeText(LoginActivity.this,"Sign in failed!",Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
        }else {
        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                           String uid= mAuth.getUid();

                            CollectionReference userColRef = FirebaseFirestore.getInstance()
                                    .collection(Constants.USERS_COLLECTION);
                            HashMap<String, Object> user = new HashMap<>();
                            String name = mAuth.getCurrentUser().getEmail();
                            user.put(Constants.KEY_USERNAME,name);
                            user.put(Constants.KEY_BOOK_PAGES,0);
                            user.put(Constants.KEY_USERID,uid);
                            userColRef.document(uid).set(user);


                            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                            startActivity(intent);

                        }else{
                            Toast.makeText(LoginActivity.this,"Sign up failed!",Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    }

}
