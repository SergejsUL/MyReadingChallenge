package ie.ul.serge.myreadingchallenge;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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
import com.squareup.okhttp.Challenge;

public class MainActivity extends AppCompatActivity {

   BookShelf mBookShelf = new BookShelf();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


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

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Context context = view.getContext();
                Intent intent = new Intent(context,ChellengeActivity.class);
                context.startActivity(intent);

                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
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

    //START of edit book method that is used to put new bookd to the shelf or edit them
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
