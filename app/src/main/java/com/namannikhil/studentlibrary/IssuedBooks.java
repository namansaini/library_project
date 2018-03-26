package com.namannikhil.studentlibrary;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

public class IssuedBooks extends AppCompatActivity {

    private SQLiteDatabase mDb;
    DbHelper helper= new DbHelper(this);
    int sId;
    IssuedBooksAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issued_books);

        sId=getIntent().getIntExtra("sId",1);
        mDb=helper.getReadableDatabase();
        Cursor cursor=queryReturn();
        RecyclerView issuedBooks=findViewById(R.id.second_recycler_view);
        issuedBooks.setHasFixedSize(false);
        RecyclerView.LayoutManager lM=new LinearLayoutManager(this);
        issuedBooks.setLayoutManager(lM);
        mAdapter=new IssuedBooksAdapter(this,cursor);
        issuedBooks.setAdapter(mAdapter);

    }
    public Cursor queryReturn()
    {
        String query="SELECT "+ BookContract.BookEntry.COLUMN_NAME+", "
                + BookContract.BookEntry.COLUMN_AUTHOR+", "
                + IssuesContract.IssuesEntry.COLUMN_EXPIRY_DATE+" FROM "+ IssuesContract.IssuesEntry.TABLE_NAME+" I, "+ BookContract.BookEntry.TABLE_NAME+" B "
                + "WHERE I."+ IssuesContract.IssuesEntry.COLUMN_STUDENT_ID+"="+sId+" AND I."+ IssuesContract.IssuesEntry.COLUMN_BOOK_ID+"=B."+ BookContract.BookEntry._ID
                +";";
        Log.e("asd",query);
        return mDb.rawQuery(query,null);
    }
    @Override
    protected void onResume() {
        super.onResume();
        mAdapter.swapCursor(queryReturn());
    }
}
