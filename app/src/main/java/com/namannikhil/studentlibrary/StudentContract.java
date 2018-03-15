package com.namannikhil.studentlibrary;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by namansaini on 15-02-2018.
 */

public class StudentContract {


    public static final class StudentEntry implements BaseColumns
    {
        public static final String TABLE_NAME="student";
        public static final String COLUMN_USERNAME="username";
        public static final String COLUMN_PASSWORD="password";
        public static final String COLUMN_FNAME="fname";
        public static final String COLUMN_LNAME="lname";
        public static final String COLUMN_ADDRESS="address";
        public static final String COLUMN_PHONE="phone";
        public static final String COLUMN_FINE="fine";
        public static final String COLUMN_NO_OF_BOOKS_ISSUED="booksIssued";
    }
}
