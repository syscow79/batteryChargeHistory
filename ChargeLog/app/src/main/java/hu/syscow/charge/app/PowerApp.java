package hu.syscow.charge.app;

import android.app.Application;
import android.content.Context;

import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

/**
 * Created by progj5 on 2017. 04. 20..
 */

public class PowerApp extends Application{

    private static Context CONTEXT;
    private boolean startedBatteryReceiver = false;
    private LineGraphSeries<DataPoint> lineGraphSeries;

    private int lineGraphSeriesSize = 0;

    public static Context getContext() {
        return CONTEXT;
    }

    public PowerApp() {
        CONTEXT = this;
    }

    public boolean isStartedBatteryReceiver() {
        return startedBatteryReceiver;
    }
    public void setStartedBatteryReceiver(boolean startedBatteryReceiver) {
        startedBatteryReceiver = startedBatteryReceiver;
    }

    public LineGraphSeries<DataPoint> getLineGraphSeries() {
        return lineGraphSeries;
    }

    public void setLineGraphSeries(LineGraphSeries<DataPoint> lineGraphSeries) {
        this.lineGraphSeries = lineGraphSeries;
    }

    public int getLineGraphSeriesSize() {
        return lineGraphSeriesSize;
    }

    public void setLineGraphSeriesSize(int lineGraphSeriesSize) {
        this.lineGraphSeriesSize = lineGraphSeriesSize;
    }

}
