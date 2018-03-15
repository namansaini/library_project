package com.namannikhil.studentlibrary;

import android.content.ContentValues;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by namansaini on 19-02-2018.
 */

public class Utility {
    public static void insertFakeData(SQLiteDatabase db) {
        if (db == null) {
            return;
        }
        //create a list of fake guests
        List<ContentValues> list = new ArrayList<ContentValues>();

        ContentValues cv = new ContentValues();
        cv.put(StudentContract.StudentEntry.COLUMN_USERNAME, "naman");
        cv.put(StudentContract.StudentEntry.COLUMN_PASSWORD, "abc12345");
        cv.put(StudentContract.StudentEntry.COLUMN_FNAME, "Naman");
        cv.put(StudentContract.StudentEntry.COLUMN_LNAME, "Saini");
        cv.put(StudentContract.StudentEntry.COLUMN_ADDRESS, "E-2/149B,Shastri Nagar, Delhi-52");
        cv.put(StudentContract.StudentEntry.COLUMN_PHONE, "7838780990");
        cv.put(StudentContract.StudentEntry.COLUMN_FINE, 0);
        cv.put(StudentContract.StudentEntry.COLUMN_NO_OF_BOOKS_ISSUED, 0);
        list.add(cv);

        cv = new ContentValues();
        cv.put(StudentContract.StudentEntry.COLUMN_USERNAME, "nikhil");
        cv.put(StudentContract.StudentEntry.COLUMN_PASSWORD, "abc12345");
        cv.put(StudentContract.StudentEntry.COLUMN_FNAME, "Nikhil");
        cv.put(StudentContract.StudentEntry.COLUMN_LNAME, "Jain");
        cv.put(StudentContract.StudentEntry.COLUMN_ADDRESS, "Rohini West");
        cv.put(StudentContract.StudentEntry.COLUMN_PHONE, "1234567890");
        cv.put(StudentContract.StudentEntry.COLUMN_FINE, 0);
        cv.put(StudentContract.StudentEntry.COLUMN_NO_OF_BOOKS_ISSUED, 0);
        list.add(cv);
        try {
            db.beginTransaction();
            db.delete(StudentContract.StudentEntry.TABLE_NAME, null, null);
            for (ContentValues c : list) {
                db.insert(StudentContract.StudentEntry.TABLE_NAME, null, c);
            }
            db.setTransactionSuccessful();
        } catch (SQLException e) {

        } finally {
            db.endTransaction();
        }
    }
}
