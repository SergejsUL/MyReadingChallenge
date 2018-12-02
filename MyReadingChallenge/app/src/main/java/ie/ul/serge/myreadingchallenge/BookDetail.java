package ie.ul.serge.myreadingchallenge;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.constraint.solver.widgets.Snapshot;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.koushikdutta.ion.Ion;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class BookDetail extends AppCompatActivity {



    private TextView mTitleTextView,mAuthorTextView,mPagesTextView;
    private ImageView mBookImageView;
    private Button addBtn;
    private long mBookPages,mPagestoAdd;
    private long mUserPages;
    private DocumentSnapshot mDocSnap,mUserDocSnap;
    private DocumentReference mDocRef,mUserDocRef;
    private FirebaseFirestore db;

    private int GALLERY = 1, CAMERA = 2;
    private StorageReference mStorageRef;
    String mUid;
    String  mDocID;
    Uri mDownloadURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        mStorageRef = FirebaseStorage.getInstance().getReference();

        mTitleTextView = findViewById(R.id.TextView_title);
        mAuthorTextView = findViewById(R.id.TextView_author);
        mPagesTextView= findViewById(R.id.TextView_pages_read);
        mBookImageView= findViewById(R.id.book_image);



        mBookImageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(BookDetail.this,"Picture Image was pressed",Toast.LENGTH_SHORT).show();
                editBookImage();
                return true;
            }
        });

        db = FirebaseFirestore.getInstance();

        Intent receivedIntent = getIntent();
        mDocID = receivedIntent.getStringExtra(Constants.EXTRA_DOC_ID);

        mDocRef = db.collection(Constants.BOOK_COLLECTION).document(mDocID);
        mDocRef.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
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

                    //Load image picture from Firestore
                    Ion.with(mBookImageView)
                            .placeholder(R.drawable.book)
                            .error(R.drawable.book)
                            .load((String)mDocSnap.get(Constants.KEY_BOOK_IMAGE_URL));

                }
            }
        });

         mUid = FirebaseAuth.getInstance().getCurrentUser().getUid();


        mUserDocRef = db.collection(Constants.USERS_COLLECTION).document(mUid);

        mUserDocRef.addSnapshotListener(this,new EventListener<DocumentSnapshot>() {
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

        numberPicker();
    }

    private void editBookImage() {

        showPictureDialog();

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
                mUserPages+=mPagestoAdd;
                addPagestoBook();
                addPagesToChallenge();
                addPages();

            }
        });
    }

    private void addPages() {
        HashMap<String,Object>pg = new HashMap<>();
        pg.put(Constants.KEY_BOOK_PAGES,mPagestoAdd);
        pg.put(Constants.KEY_CREATED,new Date());
        mUserDocRef.collection(Constants.PAGES_COLLECTION).add(pg);


    }

    private void addPagestoBook() {
        Map<String, Object> bk = new HashMap<>();
        bk.put(Constants.KEY_BOOK_PAGES, mBookPages);
        bk.put(Constants.KEY_CREATED,new Date());
        mDocRef.update(bk);

    }

    private void addPagesToChallenge() {
        Map<String, Object> up = new HashMap<>();
        up.put(Constants.KEY_BOOK_PAGES,mUserPages);
        mUserDocRef.update(up);

    }


//  CODE RELATED TO IMAGE UPLOAD FOR TEH BOOK PICTURE


    private void showPictureDialog(){
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Select photo from gallery",
//                "Capture photo from camera"
        };
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallary();
                                break;
//                            case 1:
//                                takePhotoFromCamera();
//                                break;
                        }
                    }
                });
        pictureDialog.show();
    }

    public void choosePhotoFromGallary() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, GALLERY);
    }

    private void takePhotoFromCamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        StorageReference imageRef = mStorageRef.child("images/"+ mUid +"/"+mDocID+".jpg");




        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == this.RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                    imageRef.putFile(contentURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            mDownloadURL = taskSnapshot.getDownloadUrl();
                           updateBookImageURL();
                        }
                    });

                    Toast.makeText(BookDetail.this, "Image Set!", Toast.LENGTH_SHORT).show();
                    mBookImageView.setImageBitmap(bitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(BookDetail.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (requestCode == CAMERA) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");

            mBookImageView.setImageBitmap(thumbnail);

//            saveImage(thumbnail);
            Toast.makeText(BookDetail.this, "Image Saved!", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateBookImageURL() {
        HashMap<String,Object> bk = new HashMap<>();
        bk.put(Constants.KEY_BOOK_IMAGE_URL,mDownloadURL.toString());
        mDocRef.update(bk);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();}
        return super.onOptionsItemSelected(item);
    }

}