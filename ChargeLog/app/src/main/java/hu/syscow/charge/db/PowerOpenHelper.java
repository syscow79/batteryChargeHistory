package hu.syscow.charge.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by progj5 on 2017. 04. 20..
 */

public class PowerOpenHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "power_log";
    private static final int DB_VERSION = 4;

    public PowerOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery =
                "CREATE TABLE " + PowerLogContract.LogEntry.TABLE_NAME +
                        " (" + PowerLogContract.LogEntry._ID + " INTEGER PRIMARY KEY, " +
                        PowerLogContract.LogEntry.COLUMN_POWER + " TEXT, " +
                        PowerLogContract.LogEntry.COLUMN_POWER_LEVEL + " INTEGER, " +
                        PowerLogContract.LogEntry.COLUMN_POWER_PLUG_TYPE + " TEXT, " +
                        PowerLogContract.LogEntry.COLUMN_POWER_DATE + " INTEGER " +
        "  )";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String dropTableQuery = "DROP TABLE " + PowerLogContract.LogEntry.TABLE_NAME;
        db.execSQL(dropTableQuery);
        onCreate(db);
    }
}
