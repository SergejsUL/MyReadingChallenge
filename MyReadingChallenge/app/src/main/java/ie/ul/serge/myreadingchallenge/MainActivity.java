package ie.ul.serge.myreadingchallenge;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.solver.widgets.Snapshot;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.okhttp.Challenge;

import java.util.ArrayList;
import java.util.FormatFlagsConversionMismatchException;
import java.util.List;

public class MainActivity extends AppCompatActivity {

   BookShelf mBookShelf = new BookShelf();
    List<DocumentSnapshot> mUserPageList = new ArrayList<>();

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_challenge:
                    Context context = MainActivity.this;
                    Intent intent = new Intent(context,ChellengeActivity.class);
                    context.startActivity(intent);

                    return true;
                case R.id.navigation_pages:
                    Context context1 = MainActivity.this;
                    Intent intent1 = new Intent(context1,MyPagesActivity.class);
                    context1.startActivity(intent1);

                    return true;
                case R.id.navigation_me:
                    FirebaseAuth mAuth = FirebaseAuth.getInstance();
                    String uid = mAuth.getCurrentUser().getUid();
                    FirebaseFirestore db = FirebaseFirestore.getInstance();


                    CollectionReference userPagesCollectionRef = db.collection(Constants.USERS_COLLECTION)
                            .document(uid).collection(Constants.PAGES_COLLECTION);

                    userPagesCollectionRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                            if(e!=null){
                                Toast.makeText(MainActivity.this,"Could not connect",Toast.LENGTH_SHORT).show();
                                return;
                            }
                            mUserPageList =  documentSnapshots.getDocuments();
                            long totalPages=0;
                            for(DocumentSnapshot doc : mUserPageList){

                                totalPages+= (Long)doc.get(Constants.KEY_BOOK_PAGES);

                            }
                            Toast.makeText(MainActivity.this,"Nr Pages: "+ totalPages,Toast.LENGTH_SHORT).show();
//                            DocumentSnapshot doc2snap = mUserPageList.get(2);
//                            long text = (long)doc2snap.get(Constants.KEY_BOOK_PAGES);
//                            Toast.makeText(MainActivity.this,"Document2 number of pages: "+ text,Toast.LENGTH_SHORT).show();
                        }
                    });

                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        //Use recyclerview to get list of the books
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this,3));
        recyclerView.setHasFixedSize(true);

        BookViewAdapter bookViewAdapter = new BookViewAdapter();
        recyclerView.setAdapter(bookViewAdapter);
        //END of recycler with list of books


        // Authentication tutorial
        // FirebaseAuth auth = FirebaseAuth.getInstance();
//        FirebaseUser currentUser = auth.getCurrentUser();
//        auth.signInAnonymously();
//        if(currentUser == null){
//            Log.d(Constants.TAG,"There is no user.");
//        }else{
//            Log.d(Constants.TAG,"There is user logged in.");
//        }

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                Context context = view.getContext();
//                Intent intent = new Intent(context,Main2Activity.class);
//                context.startActivity(intent);
//
//            }
//        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_add_new_book:
                editBook();

                return true;

            case R.id.action_settings:

                Toast.makeText(this,"settingsppressed",Toast.LENGTH_LONG).show();
                return true;
            case R.id.signout:
                FirebaseAuth.getInstance().signOut();
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }





    //START of edit book method that is used to put new books to the shelf or edit them
    private void editBook() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.edit_book,null, false);
        builder.setView(view);
        final EditText input_title = view.findViewById(R.id.input_title);
        final EditText input_author = view.findViewById(R.id.input_author);

                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String bookAuthor = input_author.getText().toString();
                        String bookTitle = input_title.getText().toString();
                        //TODO : get these ids from Firebase.
                        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        BookItem mBook = new BookItem(userID,bookTitle,bookAuthor);
                        mBookShelf.addBookToLibrary(mBook);


                    }
                });
        builder.create().show();


    }


}
