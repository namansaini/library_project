package com.namannikhil.studentlibrary;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nikhil on 14-03-2018.
 */

public class BookUtility {
    public static void insertFakeData(SQLiteDatabase db, Context context) {
        if (db == null) {
            return;
        }


        List<ContentValues> list = new ArrayList<ContentValues>();

        ContentValues cv = new ContentValues();
        cv.put(BookContract.BookEntry.COLUMN_NAME, "Databse Management System");
        cv.put(BookContract.BookEntry.COLUMN_AUTHOR, "Henry F. Korth");
        cv.put(BookContract.BookEntry.COLUMN_FLAG, 0);
        cv.put(BookContract.BookEntry.COLUMN_QTY, 5);
        cv.put(BookContract.BookEntry.COLUMN_NAME, "14-10-2005");
        list.add(cv);

        cv = new ContentValues();
        cv.put(BookContract.BookEntry.COLUMN_NAME, "Databse Management System");
        cv.put(BookContract.BookEntry.COLUMN_AUTHOR, "Almasri Navathe");
        cv.put(BookContract.BookEntry.COLUMN_FLAG, 0);
        cv.put(BookContract.BookEntry.COLUMN_QTY, 5);
        cv.put(BookContract.BookEntry.COLUMN_NAME, "08-07-2007");
        list.add(cv);

        cv = new ContentValues();
        cv.put(BookContract.BookEntry.COLUMN_NAME, "Operating System Design");
        cv.put(BookContract.BookEntry.COLUMN_AUTHOR, "Galvin");
        cv.put(BookContract.BookEntry.COLUMN_FLAG, 0);
        cv.put(BookContract.BookEntry.COLUMN_QTY, 5);
        cv.put(BookContract.BookEntry.COLUMN_NAME, "02-04-2012");
        list.add(cv);

        cv = new ContentValues();
        cv.put(BookContract.BookEntry.COLUMN_NAME, "Digital Design");
        cv.put(BookContract.BookEntry.COLUMN_AUTHOR, "M. Morris Mano");
        cv.put(BookContract.BookEntry.COLUMN_FLAG, 0);
        cv.put(BookContract.BookEntry.COLUMN_QTY, 5);
        cv.put(BookContract.BookEntry.COLUMN_NAME, "26-10-2008");
        list.add(cv);

        try {
            db.beginTransaction();
            db.delete(BookContract.BookEntry.TABLE_NAME, null, null);
            for (ContentValues c : list) {
                db.insert(BookContract.BookEntry.TABLE_NAME, null, c);
            }
            db.setTransactionSuccessful();
        } catch (SQLException e) {

        } finally {
            db.endTransaction();
            Toast.makeText(context, "Write successful", Toast.LENGTH_LONG).show();
        }
    }
}
