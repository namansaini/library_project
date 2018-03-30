package com.namannikhil.studentlibrary;

import android.provider.BaseColumns;

/**
 * Created by namansaini on 23-03-2018.
 */

public class IssuesContract
{
    public static final class IssuesEntry implements BaseColumns
    {
        public static final String TABLE_NAME="issues";
        public static final String COLUMN_BOOK_ID="book_id";
        public static final String COLUMN_STUDENT_ID="student_id";
        public static final String COLUMN_ISSUE_DATE="issue_date";
        public static final String COLUMN_EXPIRY_DATE="expiry_date";
        public static final String COLUMN_FINE="fine";
        public static final String COLUMN_FLAG="flag";
    }
}
