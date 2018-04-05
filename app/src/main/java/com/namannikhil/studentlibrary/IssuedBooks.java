package com.namannikhil.studentlibrary;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class IssuedBooks extends AppCompatActivity implements IssuedBooksAdapter.ListItemClickListener {

    private SQLiteDatabase mDb;
    DbHelper helper= new DbHelper(this);
    int sId;
    IssuedBooksAdapter mAdapter;
    View emptyView;
    RecyclerView issuedBooks;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issued_books);
        emptyView=findViewById(R.id.empty_view);
        sId=getIntent().getIntExtra("sId",1);
        mDb=helper.getReadableDatabase();
        Cursor cursor=queryReturn();
        issuedBooks=findViewById(R.id.second_recycler_view);
        issuedBooks.setHasFixedSize(false);
        RecyclerView.LayoutManager lM=new LinearLayoutManager(this);
        issuedBooks.setLayoutManager(lM);
        mAdapter=new IssuedBooksAdapter(this,cursor,this);
        issuedBooks.setAdapter(mAdapter);
        visibleView();
    }
    public void visibleView()
    {
        if(mAdapter.getItemCount()==0)
        {
            issuedBooks.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        }
        else
        {
            issuedBooks.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }
    }
    public Cursor queryReturn()
    {
        String query="SELECT "+ BookContract.BookEntry.COLUMN_NAME+", "
                + "B."+BookContract.BookEntry._ID+", "
                + BookContract.BookEntry.COLUMN_AUTHOR+", "
                + IssuesContract.IssuesEntry.COLUMN_EXPIRY_DATE+" FROM "+ IssuesContract.IssuesEntry.TABLE_NAME+" I, "+ BookContract.BookEntry.TABLE_NAME+" B "
                + "WHERE I."+ IssuesContract.IssuesEntry.COLUMN_STUDENT_ID+"="+sId+" AND I."+ IssuesContract.IssuesEntry.COLUMN_BOOK_ID+"=B."+ BookContract.BookEntry._ID
                +" AND "+ IssuesContract.IssuesEntry.COLUMN_FLAG+"=1"
                +";";
        return mDb.rawQuery(query,null);
    }
/*
    @Override
    protected void onResume() {
        super.onResume();
        mAdapter.swapCursor(queryReturn());
        visibleView();
    }
    */


    @Override
    public void OnListItemClick(int bookId,String expDate)
    {
            mDb = helper.getWritableDatabase();
            //updating flag value to 0 in issues table
            String args[] = {String.valueOf(sId), String.valueOf(bookId)};
        ContentValues cv = new ContentValues();
        cv.put(IssuesContract.IssuesEntry.COLUMN_FLAG,0);
            mDb.update(IssuesContract.IssuesEntry.TABLE_NAME,cv,
                    IssuesContract.IssuesEntry.COLUMN_STUDENT_ID + "=? AND " + IssuesContract.IssuesEntry.COLUMN_BOOK_ID + "=?", args);

            mDb = helper.getReadableDatabase();
            String projection[] = {BookContract.BookEntry.COLUMN_QTY};
            String selection = BookContract.BookEntry._ID + "=" + bookId;
            Cursor c = mDb.query(BookContract.BookEntry.TABLE_NAME, projection, selection, null, null, null, null);
            c.moveToPosition(0);
            int quantity = c.getInt(c.getColumnIndex(BookContract.BookEntry.COLUMN_QTY));
            cv = new ContentValues();
            cv.put(BookContract.BookEntry.COLUMN_QTY, quantity + 1);
            mDb = helper.getWritableDatabase();
            mDb.update(BookContract.BookEntry.TABLE_NAME, cv, selection, null);
            Toast.makeText(this, "Book Successfully returned", Toast.LENGTH_LONG).show();
            c.close();
            cv=new ContentValues();
        String pro[]={StudentContract.StudentEntry.COLUMN_NO_OF_BOOKS_ISSUED};
        Cursor cursor2=mDb.query(StudentContract.StudentEntry.TABLE_NAME,pro, StudentContract.StudentEntry._ID+"="+sId,
                null,null,null,null);
        cursor2.moveToPosition(0);
        int noIssuedBoks=cursor2.getInt(cursor2.getColumnIndex(StudentContract.StudentEntry.COLUMN_NO_OF_BOOKS_ISSUED));
            cv.put(StudentContract.StudentEntry.COLUMN_NO_OF_BOOKS_ISSUED,noIssuedBoks-1);
            mDb.update(StudentContract.StudentEntry.TABLE_NAME,cv, StudentContract.StudentEntry._ID+"="+sId,null);
            cursor2.close();
            mAdapter.swapCursor(queryReturn());
        visibleView();
    }
}
