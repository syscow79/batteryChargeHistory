package hu.syscow.charge.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import hu.syscow.charge.app.PowerApp;
import hu.syscow.charge.model.PowerLogItem;

/**
 * Created by progj5 on 2017. 04. 20..
 */

public class MyLogDb {

    private static final MyLogDb INSTANCE = new MyLogDb();
    private PowerOpenHelper openHelper = new PowerOpenHelper(PowerApp.getContext());

    public static MyLogDb getInstance() {
        return INSTANCE;
    }

    public void log(PowerLogItem powerLogItem) throws IllegalStateException {
        SQLiteDatabase db = openHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(PowerLogContract.LogEntry.COLUMN_POWER, powerLogItem.getPlugAction());
        values.put(PowerLogContract.LogEntry.COLUMN_POWER_LEVEL, powerLogItem.getLevel());
        values.put(PowerLogContract.LogEntry.COLUMN_POWER_PLUG_TYPE, powerLogItem.getPlugType());
        values.put(PowerLogContract.LogEntry.COLUMN_POWER_DATE, powerLogItem.getDate().getTime());

        db.insert(PowerLogContract.LogEntry.TABLE_NAME, null, values);

        db.close();
    }

    public List<PowerLogItem> readLog(ProgressBar progressBar) throws IllegalStateException {
        double count = getLogCount();
        int index = 0;

        SQLiteDatabase db = openHelper.getReadableDatabase();
        List<PowerLogItem> resultLog = new ArrayList<>();

        Cursor cursor = db.query(PowerLogContract.LogEntry.TABLE_NAME,
                new String[]{
                        PowerLogContract.LogEntry._ID,
                        PowerLogContract.LogEntry.COLUMN_POWER,
                        PowerLogContract.LogEntry.COLUMN_POWER_LEVEL,
                        PowerLogContract.LogEntry.COLUMN_POWER_PLUG_TYPE,
                        PowerLogContract.LogEntry.COLUMN_POWER_DATE
                },
                null, null,
                null, null,
                PowerLogContract.LogEntry.COLUMN_POWER_DATE + " ASC");

        int idIndex = cursor.getColumnIndex(PowerLogContract.LogEntry._ID);
        int plugActionIndex = cursor.getColumnIndex(PowerLogContract.LogEntry.COLUMN_POWER);
        int levelIndex = cursor.getColumnIndex(PowerLogContract.LogEntry.COLUMN_POWER_LEVEL);
        int plugTypeIndex = cursor.getColumnIndex(PowerLogContract.LogEntry.COLUMN_POWER_PLUG_TYPE);
        int powerDateIndex = cursor.getColumnIndex(PowerLogContract.LogEntry.COLUMN_POWER_DATE);

        while (cursor.moveToNext()) {
            PowerLogItem powerLogItem = new PowerLogItem();
            powerLogItem.setId(cursor.getLong(idIndex));
            powerLogItem.setPlugAction(cursor.getString(plugActionIndex));
            powerLogItem.setLevel(cursor.getInt(levelIndex));
            powerLogItem.setPlugType(cursor.getString(plugTypeIndex));
            powerLogItem.setDate(new Date(cursor.getLong(powerDateIndex)));
            resultLog.add(powerLogItem);
            index++;
        }

        Log.i("readLog indexes", "" + index);
        Log.i("readLog count", "" + count);

        cursor.close();
        db.close();

        return resultLog;
    }

    public long getLogCount() throws IllegalStateException {
        SQLiteDatabase db = openHelper.getReadableDatabase();
        long count = 0;

        Cursor cursor = db.rawQuery("SELECT COUNT(*) as cnt FROM " +
                PowerLogContract.LogEntry.TABLE_NAME,
                null);

        int cntIndex = cursor.getColumnIndex("cnt");
        while (cursor.moveToNext()) {
            count = cursor.getLong(cntIndex);
        }

        cursor.close();
        db.close();

        return count;
    }

}
