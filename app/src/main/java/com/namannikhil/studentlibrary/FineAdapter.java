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
 * Created by namansaini on 27-03-2018.
 */



public class FineAdapter extends RecyclerView.Adapter<FineAdapter.DataViewHolder> {

    private Context mContext;
    private Cursor mCursor;
    public FineAdapter(Context context,Cursor cursor)
    {
        mContext=context;
        mCursor=cursor;
    }
    @NonNull
    @Override
    public DataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(mContext);
        View view=inflater.inflate(R.layout.fine_list,parent,false);
        DataViewHolder viewHolder=new DataViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull DataViewHolder holder, int position) {
        if (!mCursor.moveToPosition(position))
        {
            return;
        }
        String title=mCursor.getString(mCursor.getColumnIndex(BookContract.BookEntry.COLUMN_NAME));
        String author=mCursor.getString(mCursor.getColumnIndex(BookContract.BookEntry.COLUMN_AUTHOR));
        int fine=mCursor.getInt(mCursor.getColumnIndex(IssuesContract.IssuesEntry.COLUMN_FINE));
        holder.mTitle.setText(title);
        holder.mAuthor.setText(author);
        holder.mFine.setText(String.valueOf(fine)+" ₹");
    }
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

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    public class DataViewHolder extends RecyclerView.ViewHolder {
        TextView mTitle;
        TextView mAuthor;
        TextView mFine;
        public DataViewHolder(View itemView) {
            super(itemView);
            mTitle=itemView.findViewById(R.id.fineTitle);
            mAuthor=itemView.findViewById(R.id.fineAuthor);
            mFine=itemView.findViewById(R.id.fine);
        }
    }
}
