package com.namannikhil.studentlibrary;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Nikhil on 26-03-2018.
 */

public class IssuedBooksAdapter extends RecyclerView.Adapter<IssuedBooksAdapter.bcaViewHolder> {
    private Cursor mCursor;
    private Context mContext;
    String expDate;
    final public ListItemClickListener mOnClickListener;
    public void swapCursor(Cursor newCursor) {
        // Always close the previous mCursor first
        if (mCursor != null)
            mCursor.close();
        mCursor = newCursor;
        if (newCursor != null) {
            // Force the RecyclerView to refresh
            this.notifyDataSetChanged();
        }
    }
    @NonNull
    @Override
    public IssuedBooksAdapter.bcaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(mContext);
        View view=inflater.inflate(R.layout.issued_books_list_item,parent,false);
        bcaViewHolder viewHolder=new bcaViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull IssuedBooksAdapter.bcaViewHolder holder, int position) {
        if (!mCursor.moveToPosition(position))
        {
            return;
        }
        String title=mCursor.getString(mCursor.getColumnIndex(BookContract.BookEntry.COLUMN_NAME));
        expDate=mCursor.getString(mCursor.getColumnIndex(IssuesContract.IssuesEntry.COLUMN_EXPIRY_DATE));
        String author=mCursor.getString(mCursor.getColumnIndex(BookContract.BookEntry.COLUMN_AUTHOR));
        holder.mTitle.setText(title);
        holder.mDate.setText(expDate);
        holder.mAuthor.setText(author);
    }


    @Override
    public int getItemCount()
    {
        return mCursor.getCount();
    }



    public IssuedBooksAdapter(Context context,Cursor cursor,ListItemClickListener listener)
    {
        mContext=context;
        mCursor=cursor;
        mOnClickListener=listener;
    }
    public interface ListItemClickListener
    {
        void OnListItemClick(int _id,String expDate);
    }

    public class bcaViewHolder extends RecyclerView.ViewHolder
    {
        TextView mTitle;
        TextView mAuthor;
        TextView mDate;
        Button mReturn;
        public bcaViewHolder(View itemView) {
            super(itemView);
            mDate= itemView.findViewById(R.id.date);
            mTitle= itemView.findViewById(R.id.title);
            mAuthor= itemView.findViewById(R.id.author);
            mReturn=itemView.findViewById(R.id.return_book);
            mReturn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int clickedItemIndex=getAdapterPosition();
                    mCursor.moveToPosition(clickedItemIndex);
                    int _id=mCursor.getInt(mCursor.getColumnIndex(BookContract.BookEntry._ID));
                    mOnClickListener.OnListItemClick(_id,expDate);
                }
            });
        }
    }
}
