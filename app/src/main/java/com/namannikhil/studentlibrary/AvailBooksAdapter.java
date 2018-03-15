package com.namannikhil.studentlibrary;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by namansaini on 15-03-2018.
 */

public class AvailBooksAdapter extends RecyclerView.Adapter<AvailBooksAdapter.ViewHolder> {

    private Cursor mCursor;
    private Context mContext;
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(mContext);
        View view=inflater.inflate(R.layout.book_list_item,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (!mCursor.moveToPosition(position))
            return;
        String title=mCursor.getString(mCursor.getColumnIndex(BookContract.BookEntry.COLUMN_NAME));
        String author=mCursor.getString(mCursor.getColumnIndex(BookContract.BookEntry.COLUMN_AUTHOR));
        int quantity=mCursor.getInt(mCursor.getColumnIndex(BookContract.BookEntry.COLUMN_QTY));
        holder.mAvailable.setText(quantity);
        holder.mTitle.setText(title);
        holder.mAuthor.setText(author);
    }

    @Override
    public int getItemCount()
    {
        return mCursor.getCount();
    }

    public AvailBooksAdapter(Context context,Cursor cursor)
    {
        mContext=context;
        mCursor=cursor;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView mAvailable;
        TextView mTitle;
        TextView mAuthor;
        public ViewHolder(View itemView) {
            super(itemView);
            mAvailable=(TextView) itemView.findViewById(R.id.booksAvailable);
            mTitle=(TextView) itemView.findViewById(R.id.book_title);
            mAuthor=(TextView) itemView.findViewById(R.id.book_author);
        }
    }
}
