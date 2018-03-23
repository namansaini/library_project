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

        String projection[]={BookContract.BookEntry.COLUMN_NAME, BookContract.BookEntry.COLUMN_AUTHOR, BookContract.BookEntry.COLUMN_PURCHASE_DT};
        String selection= BookContract.BookEntry._ID+"="+bookId;
        mDb=helper.getReadableDatabase();
        Cursor cursor=mDb.query(BookContract.BookEntry.TABLE_NAME,projection,selection,null,null,null,null);
        cursor.moveToPosition(0);
        String title=cursor.getString(cursor.getColumnIndex(BookContract.BookEntry.COLUMN_NAME));
        String author=cursor.getString(cursor.getColumnIndex(BookContract.BookEntry.COLUMN_AUTHOR));
        String purchaseDate=cursor.getString(cursor.getColumnIndex(BookContract.BookEntry.COLUMN_PURCHASE_DT));


        TextView titleView=(TextView) findViewById(R.id.titleView);
        TextView authorView=(TextView) findViewById(R.id.authorView);
        TextView purchaseDateView=(TextView) findViewById(R.id.purchaseDateView);
        Button issue=(Button) findViewById(R.id.issueButton);

        titleView.setText(title);
        authorView.setText(author);
        purchaseDateView.setText(purchaseDate);

        String projection2[]={StudentContract.StudentEntry.COLUMN_NO_OF_BOOKS_ISSUED};
        String selection2= StudentContract.StudentEntry._ID+"="+sId;
        cursor=mDb.query(StudentContract.StudentEntry.TABLE_NAME,projection2,selection2,null,null,null,null);
        cursor.moveToPosition(0);
        final int noOfIssuedBooks=cursor.getInt(cursor.getColumnIndex(StudentContract.StudentEntry.COLUMN_NO_OF_BOOKS_ISSUED));
        issue.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v)
            {

                boolean check=queryForZeroQuantity();
                mDb=helper.getWritableDatabase();

                    ContentValues cv = new ContentValues();
                    if(check) {
                        cv.put(BookContract.BookEntry.COLUMN_FLAG, 1);
                    }
                    cv.put(BookContract.BookEntry.COLUMN_QTY, quantity);
                    String selection = BookContract.BookEntry._ID + "=" + bookId;
                    mDb.update(BookContract.BookEntry.TABLE_NAME, cv, selection, null);

                cv=new ContentValues();
                cv.put(StudentContract.StudentEntry.COLUMN_NO_OF_BOOKS_ISSUED, noOfIssuedBooks+1);
                String selection2= StudentContract.StudentEntry._ID+"="+sId;
                mDb.update(StudentContract.StudentEntry.TABLE_NAME,cv,selection2,null);

                cv=new ContentValues();
                Calendar c1=Calendar.getInstance();
                Date d1 = c1.getTime();
                c1.add(Calendar.DATE,45);
                Date d2=c1.getTime();
                SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                String issueDate = df.format(d1);
                String expiryDate=df.format(d2);
                cv.put(IssuesContract.IssuesEntry.COLUMN_STUDENT_ID,sId);
                cv.put(IssuesContract.IssuesEntry.COLUMN_BOOK_ID,bookId);
                cv.put(IssuesContract.IssuesEntry.COLUMN_ISSUE_DATE,issueDate);
                cv.put(IssuesContract.IssuesEntry.COLUMN_EXPIRY_DATE,expiryDate);
                mDb.insert(IssuesContract.IssuesEntry.TABLE_NAME,null,cv);
                finish();
            }
        });
    }
    public boolean queryForZeroQuantity()
    {
        mDb=helper.getReadableDatabase();
        String projection[]={BookContract.BookEntry.COLUMN_QTY};
        String selection= BookContract.BookEntry._ID+"="+bookId;
        Cursor c=mDb.query(BookContract.BookEntry.TABLE_NAME,projection,selection,null,null,null,null);
        c.moveToPosition(0);
        quantity=c.getInt(c.getColumnIndex(BookContract.BookEntry.COLUMN_QTY));
        if(quantity--==0)
            return true;
        else
            return false;
    }
}
