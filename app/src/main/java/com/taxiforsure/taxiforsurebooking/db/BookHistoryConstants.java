package com.taxiforsure.taxiforsurebooking.db;

import android.provider.BaseColumns;

/**
 * Created by adarshpandey on 11/29/14.
 */
public class BookHistoryConstants {

    public static abstract class BookHistoryEntry implements BaseColumns {
        public static final String BOOK_HISTORY_TABLE = "book_history";
        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_ADDRESS = "address";

    }
}
