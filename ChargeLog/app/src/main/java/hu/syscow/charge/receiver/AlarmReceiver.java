package hu.syscow.charge.receiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.format.DateUtils;
import android.util.Log;

import hu.syscow.charge.service.BatteryCheckService;

/**
 * Created by syscow on 2017. 04. 23..
 */

public class AlarmReceiver extends BroadcastReceiver {
    private static final int REQUEST_CODE = 777;
    public static final long ALARM_INTERVAL = DateUtils.MINUTE_IN_MILLIS;

    // Call this from your service
    public static void startAlarms(final Context context) {
        Log.i("AlarmReceiver", "startAlarms");
        final AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        // start alarm right away
        manager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, 0, ALARM_INTERVAL,
                getAlarmIntent(context));
    }

    /*
     * Creates the PendingIntent used for alarms of this receiver.
     */
    private static PendingIntent getAlarmIntent(final Context context) {
        return PendingIntent.getBroadcast(context, REQUEST_CODE,
                new Intent(context, AlarmReceiver.class),
                PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @Override
    public void onReceive(final Context context, final Intent intent) {
        Log.i("AlarmReceiver", "startAlarms");
        if (context == null) {
            Log.i("AlarmReceiver", "Context null");
            // Somehow you've lost your context; this really shouldn't happen
            return;
        }
        if (intent == null){
            Log.i("AlarmReceiver", "Intent null");
            // No intent was passed to your receiver; this also really shouldn't happen
            return;
        }
        if (intent.getAction() == null) {
            Log.i("AlarmReceiver", "intent.getAction() null");
            // If you called your Receiver explicitly, this is what you should expect to happen
            Intent monitorIntent = new Intent(context, BatteryCheckService.class);
            monitorIntent.putExtra(BatteryCheckService.BATTERY_UPDATE, true);
            context.startService(monitorIntent);
        }
    }
}
