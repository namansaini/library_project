package com.namannikhil.studentlibrary;

import android.provider.BaseColumns;

/**
 * Created by Nikhil on 14-03-2018.
 */

public class BookContract {
    public static final class BookEntry implements BaseColumns{
        public static final String TABLE_NAME="books";
        public static final String COLUMN_NAME="title";
        public static final String COLUMN_AUTHOR="author";
        public static final String COLUMN_FLAG="flag";
        public static final String COLUMN_PURCHASE_DT="purchase_date";
        public static final String COLUMN_QTY="quantity";
    }
}
