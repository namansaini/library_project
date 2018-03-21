package com.namannikhil.studentlibrary;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public class AvailBooks extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private SQLiteDatabase mDb;
    DbHelper helper=new DbHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avail_books);


        Cursor cursor=queryReturn();

        mRecyclerView=(RecyclerView) findViewById(R.id.my_recycler_view);
       // mRecyclerView.setHasFixedSize(true);

        mLayoutManager= new LinearLayoutManager(this);
        mAdapter=new AvailBooksAdapter(this,cursor);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
       // mAdapter.swapCursor(queryReturn());
    }

    private Cursor queryReturn()
    {
        String projection[]={BookContract.BookEntry.COLUMN_NAME, BookContract.BookEntry.COLUMN_AUTHOR, BookContract.BookEntry.COLUMN_QTY};
        String selection= BookContract.BookEntry.COLUMN_FLAG+"=0";
        mDb=helper.getReadableDatabase();
        return mDb.query(BookContract.BookEntry.TABLE_NAME,projection,selection,null,null,null,null);
    }
}
