package com.namannikhil.studentlibrary;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by namansaini on 15-02-2018.
 */

public class StudentDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME ="library.db";
    private static final int DATABASE_VERSION = 1;
    public StudentDbHelper(Context context)
    {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String createStudent=
                "CREATE TABLE "+ StudentContract.StudentEntry.TABLE_NAME+
                        " ("+
                        StudentContract.StudentEntry._ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
                        StudentContract.StudentEntry.COLUMN_USERNAME+" VARCHAR(30) NOT NULL, "+
                        StudentContract.StudentEntry.COLUMN_PASSWORD+" VARCHAR(30) NOT NULL, "+
                        StudentContract.StudentEntry.COLUMN_FNAME+" VARCHAR(20) NOT NULL, "+
                        StudentContract.StudentEntry.COLUMN_LNAME+" VARCHAR(20) NOT NULL, "+
                        StudentContract.StudentEntry.COLUMN_ADDRESS+" VARCHAR(100) NOT NULL, "+
                        StudentContract.StudentEntry.COLUMN_PHONE+" VARCHAR(10) NOT NULL, "+
                        StudentContract.StudentEntry.COLUMN_FINE+" INT NOT NULL, "+
                        StudentContract.StudentEntry.COLUMN_NO_OF_BOOKS_ISSUED+" INT NOT NULL);";
        sqLiteDatabase.execSQL(createStudent);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + StudentContract.StudentEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
