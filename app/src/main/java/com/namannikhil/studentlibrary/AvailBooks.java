package com.namannikhil.studentlibrary;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

public class AvailBooks extends AppCompatActivity implements AvailBooksAdapter.ListItemClickListener {

    private RecyclerView mRecyclerView;
    private AvailBooksAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private SQLiteDatabase mDb;

    private int sId;
    private int buttonNo;
    private Cursor cursor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avail_books);

        sId=getIntent().getIntExtra("sId",1);

        DbHelper helper=new DbHelper(this);
        mDb=helper.getReadableDatabase();
        cursor=queryReturn();

        mRecyclerView=findViewById(R.id.my_recycler_view);
        mRecyclerView.addOnItemTouchListener(new RecyclerView.SimpleOnItemTouchListener());
        mRecyclerView.setHasFixedSize(false);

        mLayoutManager= new LinearLayoutManager(this);
        mAdapter=new AvailBooksAdapter(this,cursor,this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

    }

    private Cursor queryReturn()
    {

            String projection[] = {BookContract.BookEntry._ID,BookContract.BookEntry.COLUMN_NAME, BookContract.BookEntry.COLUMN_AUTHOR, BookContract.BookEntry.COLUMN_QTY};
            String selection = BookContract.BookEntry.COLUMN_FLAG + "=0";
            return mDb.query(BookContract.BookEntry.TABLE_NAME, projection, selection, null, null, null, null);

    }

    @Override
    public void OnListItemClick(int bookId) {
        Intent intent =new Intent(this,DetailActivity.class);
        intent.putExtra("bookId",bookId);
        intent.putExtra("sId",sId);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAdapter.swapCursor(queryReturn());
    }
}
