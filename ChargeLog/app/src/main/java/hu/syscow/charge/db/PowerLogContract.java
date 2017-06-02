package hu.syscow.charge.db;

import android.provider.BaseColumns;

/**
 * Created by progj5 on 2017. 04. 20..
 */

public class PowerLogContract {
    public static final class LogEntry implements BaseColumns {

        public static final String TABLE_NAME = "power_log";

        public static final String COLUMN_POWER = "power";
        public static final String COLUMN_POWER_LEVEL = "level";
        public static final String COLUMN_POWER_PLUG_TYPE = "type";
        public static final String COLUMN_POWER_DATE = "power_date";

    }
}

