package ie.ul.serge.myreadingchallenge;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class BookViewAdapter extends RecyclerView.Adapter<BookViewAdapter.BookViewHolder>{

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate
                (R.layout.bookitem_view,viewGroup,false);
        return new BookViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder bookViewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

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
