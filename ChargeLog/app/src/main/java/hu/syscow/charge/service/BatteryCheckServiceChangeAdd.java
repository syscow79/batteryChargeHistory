package hu.syscow.charge.service;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.BatteryManager;
import android.os.IBinder;
import android.util.Log;

import java.util.Date;

import hu.syscow.charge.db.MyLogDb;
import hu.syscow.charge.model.PowerLogItem;
import hu.syscow.charge.receiver.AlarmReceiver;

/**
 * Created by syscow on 2017. 04. 23..
 */

public class BatteryCheckServiceChangeAdd extends Service{

    public static final String BATTERY_UPDATE = "BatteryCheckServiceChangeAdd.BATTERY_UPDATE";
    public static final String BATTERY_UPDATED = "BatteryCheckServiceChangeAdd.BATTERY_UPDATED";
    public static final String EXTRA_BATTERY_LEVEL = "BatteryCheckServiceChangeAdd.EXTRA_BATTERY_LEVEL";
    private MyLogDb myLogDb = MyLogDb.getInstance();
    private static boolean startedAlarm = false;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("BatteryCheckServiceChangeAdd", "onStartCommand");

        if (!startedAlarm){
            startedAlarm = true;
            AlarmReceiver.startAlarms(BatteryCheckService.this.getApplicationContext());
        }
        if (intent != null && intent.hasExtra(BATTERY_UPDATE)){
            new BatteryCheckAsync().execute();
        }

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    private class BatteryCheckAsync extends AsyncTask<Object, Object, Void> {

        @Override
        protected Void doInBackground(Object... arg0) {

            PowerLogItem powerLogItem = new PowerLogItem();

            Intent batteryIntent = BatteryCheckService.this.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

            int level = batteryIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            int scale = batteryIntent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

            int chargePlug = batteryIntent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
            String plugType = "N/A";
            if (chargePlug == BatteryManager.BATTERY_PLUGGED_USB) {
                plugType = "USB";
            } else if (chargePlug == BatteryManager.BATTERY_PLUGGED_AC) {
                plugType = "AC";
            }

            int status = batteryIntent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
            boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                    status == BatteryManager.BATTERY_STATUS_FULL;
            //Log.i("BatteryInfo", "Battery is charging: " + isCharging);
            String plugAction = isCharging ? "on" : "off";

            /*
            Log.i("Power", plugAction);
            Log.i("Status", String.valueOf(status) );
            Log.i("Plug", plugType );
            Log.i("level", String.valueOf(level) );
            Log.i("scale", String.valueOf(scale) );*/
            Log.i("level", String.valueOf(level) );

            powerLogItem.setPlugAction(plugAction);
            powerLogItem.setLevel(level);
            powerLogItem.setPlugType(plugType);
            powerLogItem.setDate(new Date());

            try {
                myLogDb.log(powerLogItem);
                Intent sendBatteryLevelIntent = new Intent(BatteryCheckService.BATTERY_UPDATED);
                sendBatteryLevelIntent.putExtra(BatteryCheckService.EXTRA_BATTERY_LEVEL, level);
                sendBroadcast(sendBatteryLevelIntent);
            } catch (IllegalStateException e) {
                Log.e("BatteryCheckServiceChangeAdd", e.getMessage());
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            BatteryCheckService.this.stopSelf();
        }

    }
}
