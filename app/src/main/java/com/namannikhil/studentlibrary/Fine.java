package com.namannikhil.studentlibrary;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;


public class Fine extends AppCompatActivity {
    FineAdapter mAdapter;
    SQLiteDatabase mDb;
    TextView finePrice;
    int sId;
    boolean fineUpdated;
    DbHelper helper = new DbHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fine);
        sId = getIntent().getIntExtra("sId", 1);
        Button payFine = findViewById(R.id.payFine);
        finePrice = findViewById(R.id.finePrice);
        Cursor cursor = queryReturn();
        payFine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FinePayment();
            }
        });

        RecyclerView recyclerView = findViewById(R.id.fineRecyclerView);
        RecyclerView.LayoutManager layout = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layout);
        recyclerView.setHasFixedSize(false);
        mAdapter = new FineAdapter(this, cursor);
        recyclerView.setAdapter(mAdapter);
    }

    private void FinePayment() {
        mDb = helper.getWritableDatabase();
        ContentValues cv=new ContentValues();
        Calendar ca = Calendar.getInstance();
        Date d2 = ca.getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String curr_dt=df.format(d2);
        cv.put(IssuesContract.IssuesEntry.COLUMN_EXPIRY_DATE,curr_dt);
        mDb.update(IssuesContract.IssuesEntry.TABLE_NAME,cv, IssuesContract.IssuesEntry.COLUMN_FLAG + "=1",null);

        cv.put(IssuesContract.IssuesEntry.COLUMN_FINE,0);
        mDb.update(IssuesContract.IssuesEntry.TABLE_NAME,cv,IssuesContract.IssuesEntry.COLUMN_FINE + ">0" +
                " AND "+ IssuesContract.IssuesEntry.COLUMN_STUDENT_ID+"="+sId,null);
        mAdapter.swapCursor(queryReturn());
    }

    private void calculateAndInsertFine(String expiryDate, int issueId, int flag) {
        Calendar ca = Calendar.getInstance();
        Date d2 = ca.getTime();

        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        Date d1 = new Date();
        try {
            d1 = df.parse(expiryDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //d1 ---> Expiry date
        //d2 ---> Current Date
        if(d2.after(d1) && flag==1) {
            int daysDiff = daysFinder(d1, d2);
            mDb = helper.getReadableDatabase();
            //Toast.makeText(this, daysDiff+"", Toast.LENGTH_LONG).show();
            /*String fineProjection[]={IssuesContract.IssuesEntry.COLUMN_FINE};
            Cursor c=mDb.query(IssuesContract.IssuesEntry.TABLE_NAME,fineProjection,IssuesContract.IssuesEntry._ID + "=" + issueId,
                    null,null,null,null);
            c.moveToPosition(0);
            int oldFine=c.getInt(c.getColumnIndex(IssuesContract.IssuesEntry.COLUMN_FINE));
            c.close();*/
            int fine = daysDiff * 2;
            ContentValues cv = new ContentValues();
            cv.put(IssuesContract.IssuesEntry.COLUMN_FINE, fine);
            mDb.update(IssuesContract.IssuesEntry.TABLE_NAME, cv, IssuesContract.IssuesEntry._ID + "=" + issueId, null);
        }

    }

    private int daysFinder(Date d1,Date d2)
    {
        int no_of_days=0;
        int days=365;

        Log.v("asd",d2.getDate()+" "+d2.getMonth()+" "+d2.getYear());
        if((d2.getYear() % 4 == 0 && d2.getYear() % 100 != 0) || (d2.getYear() % 400 == 0))
        {
            days=366;
        }
        if(d1.getYear()<d2.getYear())
        {
            no_of_days+=days*(d2.getYear()-d1.getYear());
            if(d1.getMonth()<=d2.getMonth())
            {
                no_of_days+=30*(d2.getMonth()-d1.getMonth());
            }
            else
            {
                no_of_days-=30*(d1.getMonth()-d2.getMonth());
            }
            if (d1.getDate() <= d2.getDate())
            {
                no_of_days+=d2.getDate()-d1.getDate();
            }
            else
            {
                no_of_days-=d1.getDate()-d2.getDate();
            }
        }
        else
        if(d1.getMonth()<=d2.getMonth())
        {
            no_of_days+=30*Math.abs(d1.getMonth()-d2.getMonth());
            if (d1.getDate() <= d2.getDate())
            {
                no_of_days+=d2.getDate()-d1.getDate();
            }
            else
            {
                no_of_days-=(d1.getDate()-d2.getDate());
            }
        }
        return no_of_days;
    }

    private Cursor queryReturn() {
        mDb = helper.getReadableDatabase();
        String query = "SELECT " + "B." + BookContract.BookEntry.COLUMN_NAME + ", "
                + "I." + IssuesContract.IssuesEntry._ID + ", "
                + "B." + BookContract.BookEntry.COLUMN_AUTHOR + ", "
                + "I." + IssuesContract.IssuesEntry.COLUMN_FLAG + ", "
                + "I." + IssuesContract.IssuesEntry.COLUMN_EXPIRY_DATE + ", "
                + "I." + IssuesContract.IssuesEntry.COLUMN_FINE + " FROM " +
                IssuesContract.IssuesEntry.TABLE_NAME + " I, "
                + BookContract.BookEntry.TABLE_NAME + " B "
                + "WHERE I." + IssuesContract.IssuesEntry.COLUMN_STUDENT_ID + "=" + sId + " AND I."
                + IssuesContract.IssuesEntry.COLUMN_BOOK_ID + "=B." + BookContract.BookEntry._ID
               // +" AND I."+ IssuesContract.IssuesEntry.COLUMN_FINE+">0"
                + ";";
        //Log.v("asd", query);

        Cursor cursor = mDb.rawQuery(query, null);

        for(int i=0;i<cursor.getCount();i++)
        {

            cursor.moveToPosition(i);
                String expiryDate = cursor.getString(cursor.getColumnIndex(IssuesContract.IssuesEntry.COLUMN_EXPIRY_DATE));
                int issueId = cursor.getInt(cursor.getColumnIndex(IssuesContract.IssuesEntry._ID));
                int flag=cursor.getInt(cursor.getColumnIndex(IssuesContract.IssuesEntry.COLUMN_FLAG));
                calculateAndInsertFine(expiryDate, issueId, flag);
        }
        String query2="SELECT " + "B." + BookContract.BookEntry.COLUMN_NAME + ", "
                + "I." + IssuesContract.IssuesEntry._ID + ", "
                + "B." + BookContract.BookEntry.COLUMN_AUTHOR + ", "
                + "I." + IssuesContract.IssuesEntry.COLUMN_EXPIRY_DATE + ", "
                + "I." + IssuesContract.IssuesEntry.COLUMN_FINE + " FROM " +
                IssuesContract.IssuesEntry.TABLE_NAME + " I, "
                + BookContract.BookEntry.TABLE_NAME + " B "
                + "WHERE I." + IssuesContract.IssuesEntry.COLUMN_STUDENT_ID + "=" + sId + " AND I."
                + IssuesContract.IssuesEntry.COLUMN_BOOK_ID + "=B." + BookContract.BookEntry._ID
                +" AND I."+ IssuesContract.IssuesEntry.COLUMN_FINE+">0"
                + ";";
            cursor = mDb.rawQuery(query2, null);
        TotalFine t=new TotalFine();
            int cost=t.totalFine(helper,sId);
        finePrice.setText(String.valueOf(cost) + " ₹");
            return cursor;
    }
}
