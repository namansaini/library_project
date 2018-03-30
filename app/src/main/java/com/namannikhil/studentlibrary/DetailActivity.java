package com.namannikhil.studentlibrary;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DetailActivity extends AppCompatActivity {
    private SQLiteDatabase mDb;
    DbHelper helper=new DbHelper(this);
    int bookId;
    int sId;
    int quantity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        bookId=getIntent().getIntExtra("bookId",1);
        sId=getIntent().getIntExtra("sId",1);

        //getting details of book to show in activity
        final String projection[]={BookContract.BookEntry.COLUMN_NAME, BookContract.BookEntry.COLUMN_AUTHOR, BookContract.BookEntry.COLUMN_PURCHASE_DT};
        String selection= BookContract.BookEntry._ID+"="+bookId;
        mDb=helper.getReadableDatabase();
        Cursor cursor=mDb.query(BookContract.BookEntry.TABLE_NAME,projection,selection,null,null,null,null);
        cursor.moveToPosition(0);
        String title=cursor.getString(cursor.getColumnIndex(BookContract.BookEntry.COLUMN_NAME));
        String author=cursor.getString(cursor.getColumnIndex(BookContract.BookEntry.COLUMN_AUTHOR));
        String purchaseDate=cursor.getString(cursor.getColumnIndex(BookContract.BookEntry.COLUMN_PURCHASE_DT));


        TextView titleView= findViewById(R.id.titleView);
        TextView authorView= findViewById(R.id.authorView);
        TextView purchaseDateView= findViewById(R.id.purchaseDateView);
        Button issue=findViewById(R.id.issueButton);

        titleView.setText(title);
        authorView.setText(author);
        purchaseDateView.setText(purchaseDate);

        //getting no of issued books of a particular student
        String projection2[]={StudentContract.StudentEntry.COLUMN_NO_OF_BOOKS_ISSUED};
        String selection2= StudentContract.StudentEntry._ID+"="+sId;
        cursor=mDb.query(StudentContract.StudentEntry.TABLE_NAME,projection2,selection2,null,null,null,null);
        cursor.moveToPosition(0);
        final int noOfIssuedBooks=cursor.getInt(cursor.getColumnIndex(StudentContract.StudentEntry.COLUMN_NO_OF_BOOKS_ISSUED));
        issue.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v)
            {

                //Checking if same book is issued already
                mDb=helper.getReadableDatabase();
                String projection[]={IssuesContract.IssuesEntry.COLUMN_BOOK_ID};
                Cursor cursor=mDb.query(IssuesContract.IssuesEntry.TABLE_NAME,projection,
                        IssuesContract.IssuesEntry.COLUMN_BOOK_ID+"="+bookId+" AND "+ IssuesContract.IssuesEntry.COLUMN_STUDENT_ID+"="+sId+" AND "+ IssuesContract.IssuesEntry.COLUMN_FLAG+"=1",
                        null,null,null,null);
                TotalFine t=new TotalFine();
                int cost=t.totalFine(helper,sId);
                if(cursor.getCount()>0)
                {
                    toastNotIssue();
                }
                else //same book isn't there
                    if(cost>=200)
                    {
                        toastPayFine();
                    }
                    else {
                        //updating quantity of books
                        settingQuantity();
                        mDb = helper.getWritableDatabase();

                        ContentValues cv = new ContentValues();
                        cv.put(BookContract.BookEntry.COLUMN_QTY, quantity - 1);
                        String selection = BookContract.BookEntry._ID + "=" + bookId;
                        mDb.update(BookContract.BookEntry.TABLE_NAME, cv, selection, null);

                        //updating no of issued books
                        cv = new ContentValues();
                        cv.put(StudentContract.StudentEntry.COLUMN_NO_OF_BOOKS_ISSUED, noOfIssuedBooks + 1);
                        String selection2 = StudentContract.StudentEntry._ID + "=" + sId;
                        mDb.update(StudentContract.StudentEntry.TABLE_NAME, cv, selection2, null);

                        //insertion in relationship table "issues"
                        cv = new ContentValues();
                        Calendar c1 = Calendar.getInstance();
                        Date d1 = c1.getTime();
                        c1.add(Calendar.DATE, 25);
                        Date d2 = c1.getTime();
                        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                        String issueDate = df.format(d1);
                        String expiryDate = df.format(d2);

                        String projection2[] = {IssuesContract.IssuesEntry._ID};
                        String where = IssuesContract.IssuesEntry.COLUMN_STUDENT_ID + "=? AND " + IssuesContract.IssuesEntry.COLUMN_BOOK_ID + "=?";
                        String args[] = {String.valueOf(sId), String.valueOf(bookId)};
                        Cursor c2 = mDb.query(IssuesContract.IssuesEntry.TABLE_NAME, projection2, where, args, null, null, null);
                        if (c2.getCount() > 0)//same book with same student exists in issues table
                        {
                            cv.put(IssuesContract.IssuesEntry.COLUMN_ISSUE_DATE, issueDate);
                            cv.put(IssuesContract.IssuesEntry.COLUMN_EXPIRY_DATE, expiryDate);
                            cv.put(IssuesContract.IssuesEntry.COLUMN_FLAG, 1);
                            mDb.update(IssuesContract.IssuesEntry.TABLE_NAME, cv, where, args);
                            finish();
                        }
                        else
                            {
                            cv.put(IssuesContract.IssuesEntry.COLUMN_STUDENT_ID, sId);
                            cv.put(IssuesContract.IssuesEntry.COLUMN_BOOK_ID, bookId);
                            cv.put(IssuesContract.IssuesEntry.COLUMN_ISSUE_DATE, issueDate);
                            cv.put(IssuesContract.IssuesEntry.COLUMN_EXPIRY_DATE, expiryDate);
                            cv.put(IssuesContract.IssuesEntry.COLUMN_FINE, 0);
                            cv.put(IssuesContract.IssuesEntry.COLUMN_FLAG, 1);
                            mDb.insert(IssuesContract.IssuesEntry.TABLE_NAME, null, cv);
                            toastIsIssue();
                            finish();
                        }
                    }
            }
        });
    }

    private void toastPayFine() {
        Toast.makeText(this,"Fine is greater than or equal to 200, first pay the fine!!",Toast.LENGTH_LONG).show();
    }

    private void toastIsIssue() {
        Toast.makeText(this,"Book Successfully issued",Toast.LENGTH_LONG).show();
    }

    private void toastNotIssue() {
        Toast.makeText(this,"You cannot issue the same book more than once!!",Toast.LENGTH_LONG).show();
    }

    public void settingQuantity()
    {
        mDb=helper.getReadableDatabase();
        String projection[]={BookContract.BookEntry.COLUMN_QTY};
        String selection= BookContract.BookEntry._ID+"="+bookId;
        Cursor c=mDb.query(BookContract.BookEntry.TABLE_NAME,projection,selection,null,null,null,null);
        c.moveToPosition(0);
        quantity=c.getInt(c.getColumnIndex(BookContract.BookEntry.COLUMN_QTY));
    }
}
