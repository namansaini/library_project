package com.namannikhil.studentlibrary;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class TotalFine {
    SQLiteDatabase mDb;

    public TotalFine()
    {
    }
    public int totalFine(DbHelper helper,int sId) {
        mDb = helper.getReadableDatabase();
        int cost = 0;
        String projection[] = {IssuesContract.IssuesEntry.COLUMN_FINE};
        Cursor cursor = mDb.query(IssuesContract.IssuesEntry.TABLE_NAME, projection, IssuesContract.IssuesEntry.COLUMN_STUDENT_ID+"="+sId, null, null, null, null);
        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);
            int fine = cursor.getInt(cursor.getColumnIndex(IssuesContract.IssuesEntry.COLUMN_FINE));
            cost += fine;
        }
        cursor.close();
        return cost;
    }

}
