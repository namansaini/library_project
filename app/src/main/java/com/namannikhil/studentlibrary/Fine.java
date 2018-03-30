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
        cv.put(IssuesContract.IssuesEntry.COLUMN_FINE,0);
        mDb.update(IssuesContract.IssuesEntry.TABLE_NAME,cv,IssuesContract.IssuesEntry.COLUMN_FINE + ">0" +
                " AND "+ IssuesContract.IssuesEntry.COLUMN_STUDENT_ID+"="+sId,null);
        mAdapter.swapCursor(queryReturn());
    }

    private void calculateAndInsertFine(String expiryDate, int issueId) {
        fineUpdated=false;
        Calendar ca = Calendar.getInstance();
        Date d1 = ca.getTime();

        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        Date d2 = new Date();
        try {
            d2 = df.parse(expiryDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
       if (d1.after(d2)) {
            mDb = helper.getReadableDatabase();
            int difference = (int) (d1.getTime() - d2.getTime());
            int daysDiff= (int)TimeUnit.MILLISECONDS.toDays(difference);

            Toast.makeText(this,daysDiff+" hahaha",Toast.LENGTH_LONG).show();
            int fine = daysDiff*2;
            ContentValues cv = new ContentValues();
            cv.put(IssuesContract.IssuesEntry.COLUMN_FINE, fine);
            mDb.update(IssuesContract.IssuesEntry.TABLE_NAME, cv, IssuesContract.IssuesEntry._ID + "=" + issueId, null);
            fineUpdated=true;
        }
    }

    private Cursor queryReturn() {
        mDb = helper.getReadableDatabase();
        String query = "SELECT " + "B." + BookContract.BookEntry.COLUMN_NAME + ", "
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
        //Log.v("asd", query);

        Cursor cursor = mDb.rawQuery(query, null);


        for(int i=0;i<cursor.getCount();i++)
        {

            cursor.moveToPosition(i);
                String expiryDate = cursor.getString(cursor.getColumnIndex(IssuesContract.IssuesEntry.COLUMN_EXPIRY_DATE));
                int issueId = cursor.getInt(cursor.getColumnIndex(IssuesContract.IssuesEntry._ID));
                calculateAndInsertFine(expiryDate, issueId);
        }
        if(fineUpdated)
            cursor = mDb.rawQuery(query, null);
        TotalFine t=new TotalFine();
            int cost=t.totalFine(helper,sId);
        finePrice.setText(String.valueOf(cost) + " ₹");
            return cursor;
    }
}
