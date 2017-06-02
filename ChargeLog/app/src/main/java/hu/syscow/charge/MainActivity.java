package hu.syscow.charge;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.jjoe64.graphview.GraphView;

import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.List;

import hu.syscow.charge.app.PowerApp;
import hu.syscow.charge.db.MyLogDb;
import hu.syscow.charge.model.PowerLogItem;
import hu.syscow.charge.service.BatteryCheckService;

public class MainActivity extends AppCompatActivity {

    private BroadcastReceiver batteryReceiver = new BatteryReceiver();
    private PowerApp powerApp = (PowerApp) PowerApp.getContext();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();
        readLogOnMainViewInit();

        Intent monitorIntent = new Intent(this, BatteryCheckService.class);
        startService(monitorIntent);

        if (!powerApp.isStartedBatteryReceiver()) {
            powerApp.setStartedBatteryReceiver(true);
            IntentFilter batteryFilter = new IntentFilter(BatteryCheckService.BATTERY_UPDATED);
            registerReceiver(batteryReceiver, batteryFilter);
        }
    }

    public void readLogOnMainViewInit() {

        int stop = powerApp.getLineGraphSeriesSize();
        GraphView graphView = (GraphView) findViewById(R.id.graph);
        graphView.removeAllSeries();

        graphView.getViewport().setYAxisBoundsManual(true);
        graphView.getViewport().setMinY(0);
        graphView.getViewport().setMaxY(100);

        graphView.getViewport().setXAxisBoundsManual(true);
        graphView.getViewport().setMinX(0);
        graphView.getViewport().setMaxX(stop);

        graphView.getViewport().setScalable(true);
        graphView.getViewport().setScrollable(true);

        graphView.addSeries(powerApp.getLineGraphSeries());
    }

    private void readLogOnMainViewAdd(int batteryLevel) {
        LineGraphSeries<DataPoint> lineGraphSeries = powerApp.getLineGraphSeries();
        lineGraphSeries.appendData(new DataPoint(lineGraphSeries.getHighestValueX() + 1, batteryLevel), true, powerApp.getLineGraphSeriesSize() + 1);
        powerApp.setLineGraphSeries(lineGraphSeries);
        powerApp.setLineGraphSeriesSize(powerApp.getLineGraphSeriesSize() + 1);
    }

    private class BatteryReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i("Main.BatteryReceiver", "onReceive");
            int batteryLevel = intent.getIntExtra(BatteryCheckService.EXTRA_BATTERY_LEVEL, -1);
            readLogOnMainViewAdd(batteryLevel);
        }

    }

}
