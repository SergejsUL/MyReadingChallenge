package ie.ul.serge.myreadingchallenge;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class BookViewAdapter extends RecyclerView.Adapter<BookViewAdapter.BookViewHolder>{

    private List<DocumentSnapshot> mBooksSnapshots = new ArrayList<>();
    public BookViewAdapter(){
        CollectionReference booklistRef = FirebaseFirestore.getInstance()
        .collection(Constants.BOOK_COLLECTION);
        booklistRef.orderBy(Constants.CREATED_KEY, Query.Direction.DESCENDING).limit(50)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(QuerySnapshot documentSnapshots,
                                        FirebaseFirestoreException e) {
                        if(e!=null){
                            Log.w(Constants.TAG,"Listening failed");
                            return;
                        }
                        mBooksSnapshots = documentSnapshots.getDocuments();
                        notifyDataSetChanged();
                    }

                });


    }
    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate
                (R.layout.bookitem_view,viewGroup,false);
        return new BookViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder bookViewHolder, int i) {
        DocumentSnapshot ds = mBooksSnapshots.get(i);
        String title = (String)ds.get(Constants.BOOK_TITLE);
        String author=(String)ds.get(Constants.BOOK_AUTHOR);

        bookViewHolder.mTitleTextView.setText(title);
        bookViewHolder.mAuthorTextView.setText(author);

    }

    @Override
    public int getItemCount() {return mBooksSnapshots.size();}


    class BookViewHolder extends RecyclerView.ViewHolder{
        private TextView mTitleTextView;
        private TextView mAuthorTextView;

        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            mTitleTextView = itemView.findViewById(R.id.itemview_Title);
            mAuthorTextView= itemView.findViewById(R.id.itemview_Author);

        }
    }
}
