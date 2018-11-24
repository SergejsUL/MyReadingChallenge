package ie.ul.serge.myreadingchallenge;

import android.content.Context;
import android.content.Intent;
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
import java.util.PriorityQueue;

public class ChallengeViewAdapter extends RecyclerView.Adapter<ChallengeViewAdapter.ChallengeViewHolder> {


    private List<DocumentSnapshot> mUserSnapshots = new ArrayList<>();


        public ChallengeViewAdapter(){
        CollectionReference mUsersRef = FirebaseFirestore.getInstance()
                .collection(Constants.USERS_COLLECTION);
        mUsersRef.orderBy(Constants.KEY_BOOK_PAGES,Query.Direction.DESCENDING).limit(50)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                if(e!=null){
                    Log.w(Constants.TAG,"Listening failed");
                    return;
                }

                mUserSnapshots = documentSnapshots.getDocuments();

                notifyDataSetChanged();
            }
        });

    }



    @NonNull
    @Override
    public ChallengeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate
                (R.layout.user_item_view,viewGroup,false);
        return new ChallengeViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ChallengeViewHolder challengeViewHolder, int i) {
        DocumentSnapshot ds = mUserSnapshots.get(i);
        String user = (String)ds.get(Constants.KEY_USERNAME);
        Long numberPages = (Long)ds.get(Constants.KEY_BOOK_PAGES);
        challengeViewHolder.mUserTextView.setText(user);
        challengeViewHolder.mNumPageTextView.setText(numberPages+"");

    }

    @Override
    public int getItemCount() {return mUserSnapshots.size();
    }

    //VIEWHOLDER
    class ChallengeViewHolder extends RecyclerView.ViewHolder{
        private TextView mUserTextView;
        private TextView mNumPageTextView;

        public ChallengeViewHolder(@NonNull final View itemView) {
            super(itemView);
            mUserTextView = itemView.findViewById(R.id.itemview_Username);
            mNumPageTextView= itemView.findViewById(R.id.itemview_NrPages);

        }
    }
}
