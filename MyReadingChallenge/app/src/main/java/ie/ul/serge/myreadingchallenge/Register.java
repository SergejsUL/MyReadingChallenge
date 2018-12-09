package ie.ul.serge.myreadingchallenge;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.GregorianCalendar;

public class Register extends AppCompatActivity {

    CalendarView mExpandableCallendar;
    TextView mShowCallendar,mNameTextView,mEmailTextview,mPasswordTextview;
    ImageView mUserImageview;
    FirebaseFirestore mDB;
    FirebaseAuth mAuth;
    StorageReference mStorageRef;
//    Task<Uri> mDownloadURL;
//    Uri mBitmapURI;
    GregorianCalendar mDOB;
    User mNewUser;
//    private int GALLERY = 1, CAMERA = 2;
    String mUid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //mDownloadURL = Uri.parse("no uri");

        mAuth=FirebaseAuth.getInstance();
        mDB = FirebaseFirestore.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();

        mExpandableCallendar=findViewById(R.id.expanded_callendar_view);
        mShowCallendar=findViewById(R.id.expand_callendar_view);
        mNameTextView=findViewById(R.id.name_edittext);
        mEmailTextview=findViewById(R.id.email_edittext);
        mPasswordTextview=findViewById(R.id.password_edittext);
        mUserImageview = findViewById(R.id.user_picture_imageview);
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
                            mUid= mAuth.getUid();

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


//        final StorageReference imageRef = mStorageRef.child("images/" + mUid + "/" +"profile_pic.jpg");
//
//        imageRef.putFile(mBitmapURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
////                mDownloadURL = taskSnapshot.getDownloadUrl();
////                mDownloadURL=taskSnapshot.getMetadata().getReference().getDownloadUrl();
//
//
//            }
//        });
//        mDownloadURL=imageRef.getDownloadUrl();
        String name = mNameTextView.getText().toString();
        String picURL = "URL";
        GregorianCalendar dob = mDOB;

        mNewUser= new User(name,dob,mUid,picURL);

        //Create new user and return user id



        CollectionReference userColRef = mDB
                .collection(Constants.USERS_COLLECTION);
        userColRef.document(mUid).set(mNewUser.getmUser());

    }


//    //CODE RELATED TO IMAGE UPLOAD
//
//    public void setUserPic(View view){
//        showPictureDialog();
//    }
//    private void showPictureDialog(){
//        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
//        pictureDialog.setTitle("Select Action");
//        String[] pictureDialogItems = {
//                "Select photo from gallery",
////                "Capture photo from camera"
//        };
//        pictureDialog.setItems(pictureDialogItems,
//                new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        switch (which) {
//                            case 0:
//                                choosePhotoFromGallary();
//                                break;
////                            case 1:
////                                takePhotoFromCamera();
////                                break;
//                        }
//                    }
//                });
//        pictureDialog.show();
//    }
//
//    private void choosePhotoFromGallary() {
//        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
//                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//
//        startActivityForResult(galleryIntent, GALLERY);
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//
//
//
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == this.RESULT_CANCELED) {
//            return;
//        }
//        if (requestCode == GALLERY) {
//            if (data != null) {
//                Uri contentURI = data.getData();
//                try {
//                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
//                    mBitmapURI = contentURI;
//
//                    Toast.makeText(Register.this, "Image Set!", Toast.LENGTH_SHORT).show();
//                    mUserImageview.setImageBitmap(bitmap);
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    Toast.makeText(Register.this, "Failed!", Toast.LENGTH_SHORT).show();
//                }
//            }
//        }
//    }

}
