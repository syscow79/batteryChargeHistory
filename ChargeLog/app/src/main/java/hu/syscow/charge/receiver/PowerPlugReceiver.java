package hu.syscow.charge.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import hu.syscow.charge.service.BatteryCheckService;

public class PowerPlugReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("PowerPlugReceiver", "onReceive");
        Intent monitorIntent = new Intent(context, BatteryCheckService.class);
        context.startService(monitorIntent);
    }

}