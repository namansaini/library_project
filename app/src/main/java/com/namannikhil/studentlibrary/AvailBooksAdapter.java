package com.namannikhil.studentlibrary;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by namansaini on 15-03-2018.
 */

public class AvailBooksAdapter extends RecyclerView.Adapter<AvailBooksAdapter.abcViewHolder> {

    private Cursor mCursor;
    private Context mContext;
    @NonNull
    @Override
    public abcViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(mContext);
        View view=inflater.inflate(R.layout.book_list_item,parent,false);
        abcViewHolder viewHolder=new abcViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull abcViewHolder holder, int position) {
        if (!mCursor.moveToPosition(position))
        {
            Toast.makeText(mContext,"Cursor Empty",Toast.LENGTH_LONG).show();
            return;
        }

        Toast.makeText(mContext,"Cursor Full",Toast.LENGTH_LONG).show();
        String title=mCursor.getString(mCursor.getColumnIndex(BookContract.BookEntry.COLUMN_NAME));
        String author=mCursor.getString(mCursor.getColumnIndex(BookContract.BookEntry.COLUMN_AUTHOR));
        int quantity=mCursor.getInt(mCursor.getColumnIndex(BookContract.BookEntry.COLUMN_QTY));
        holder.mAvailable.setText(quantity);
        holder.mTitle.setText(title);
        holder.mAuthor.setText(author);
        this.notifyDataSetChanged();
    }

    @Override
    public int getItemCount()
    {
        return 4;// mCursor.getCount();
    }

   /* public void swapCursor(Cursor newCursor) {
        // Always close the previous mCursor first
        if (mCursor != null)
            mCursor.close();
        mCursor = newCursor;
        if (newCursor != null) {
            // Force the RecyclerView to refresh
            this.notifyDataSetChanged();
        }
    }
*/
    public AvailBooksAdapter(Context context,Cursor cursor)
    {
        mContext=context;
        mCursor=cursor;
    }

    public static class abcViewHolder extends RecyclerView.ViewHolder
    {
        TextView mAvailable;
        TextView mTitle;
        TextView mAuthor;
        public abcViewHolder(View itemView) {
            super(itemView);
            mAvailable=(TextView) itemView.findViewById(R.id.booksAvailable);
            mTitle=(TextView) itemView.findViewById(R.id.book_title);
            mAuthor=(TextView) itemView.findViewById(R.id.book_author);
        }

    }
}
