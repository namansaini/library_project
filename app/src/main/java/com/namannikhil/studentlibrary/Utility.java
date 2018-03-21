package com.namannikhil.studentlibrary;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

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
        List<ContentValues> list2 = new ArrayList<ContentValues>();

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


        cv = new ContentValues();
        cv.put(BookContract.BookEntry.COLUMN_NAME, "Databse Management System");
        cv.put(BookContract.BookEntry.COLUMN_AUTHOR, "Henry F. Korth");
        cv.put(BookContract.BookEntry.COLUMN_FLAG, 0);
        cv.put(BookContract.BookEntry.COLUMN_QTY, 5);
        cv.put(BookContract.BookEntry.COLUMN_NAME, "14-10-2005");
        list2.add(cv);

        cv = new ContentValues();
        cv.put(BookContract.BookEntry.COLUMN_NAME, "Databse Management System");
        cv.put(BookContract.BookEntry.COLUMN_AUTHOR, "Almasri Navathe");
        cv.put(BookContract.BookEntry.COLUMN_FLAG, 0);
        cv.put(BookContract.BookEntry.COLUMN_QTY, 5);
        cv.put(BookContract.BookEntry.COLUMN_NAME, "08-07-2007");
        list2.add(cv);

        cv = new ContentValues();
        cv.put(BookContract.BookEntry.COLUMN_NAME, "Operating System Design");
        cv.put(BookContract.BookEntry.COLUMN_AUTHOR, "Galvin");
        cv.put(BookContract.BookEntry.COLUMN_FLAG, 0);
        cv.put(BookContract.BookEntry.COLUMN_QTY, 5);
        cv.put(BookContract.BookEntry.COLUMN_NAME, "02-04-2012");
        list2.add(cv);

        cv = new ContentValues();
        cv.put(BookContract.BookEntry.COLUMN_NAME, "Digital Design");
        cv.put(BookContract.BookEntry.COLUMN_AUTHOR, "M. Morris Mano");
        cv.put(BookContract.BookEntry.COLUMN_FLAG, 0);
        cv.put(BookContract.BookEntry.COLUMN_QTY, 5);
        cv.put(BookContract.BookEntry.COLUMN_NAME, "26-10-2008");
        list2.add(cv);
        try {
            db.beginTransaction();
            db.delete(StudentContract.StudentEntry.TABLE_NAME, null, null);
            db.delete(BookContract.BookEntry.TABLE_NAME,null,null);
            for(ContentValues c: list2)
            {
                db.insert(BookContract.BookEntry.TABLE_NAME,null,c);
            }
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
