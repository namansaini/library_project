package com.namannikhil.studentlibrary;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Nikhil on 14-03-2018.
 */

public class BookDbHelper extends SQLiteOpenHelper{

    private static final String DATABASE_NAME ="library2.db";
    private static final int DATABASE_VERSION = 1;
    public BookDbHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    };
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String createBook=
                "CREATE TABLE "+ BookContract.BookEntry.TABLE_NAME+
                        " ("+
                       BookContract.BookEntry._ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
                        BookContract.BookEntry.COLUMN_NAME+" VARCHAR(30) NOT NULL, "+
                        BookContract.BookEntry.COLUMN_AUTHOR+" VARCHAR(30) NOT NULL, "+
                        BookContract.BookEntry.COLUMN_FLAG+" INTEGER NOT NULL, "+
                        BookContract.BookEntry.COLUMN_QTY+" INTEGER NOT NULL, " +
                        BookContract.BookEntry.COLUMN_PURCHASE_DT+" VARCHAR(10) NOT NULL);";
        sqLiteDatabase.execSQL(createBook);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + BookContract.BookEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
