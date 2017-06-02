package hu.syscow.charge;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.List;

import hu.syscow.charge.app.PowerApp;
import hu.syscow.charge.db.MyLogDb;
import hu.syscow.charge.model.PowerLogItem;

public class Splash extends AppCompatActivity {

    private ProgressBar progressBar;
    private MyLogDb myLogDb = MyLogDb.getInstance();
    private PowerApp powerApp = (PowerApp) PowerApp.getContext();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        progressBar = (ProgressBar) findViewById(R.id.splash_progressBar);
        progressBar.setProgress(0);


        new Thread(new Runnable() {
            public void run() {
                doWork();
                startApp();
                finish();
            }
        }).start();
    }

    private void doWork() {
        long startMillis = System.currentTimeMillis();
        List<PowerLogItem> powerLogList = null;
        try {
            powerLogList = myLogDb.readLog(this.progressBar);
        } catch (IllegalStateException e) {
            Log.e("DbReadError", e.getMessage());
            return;
        }
        startMillis = logMillis("Millis after db " + powerLogList.size(), startMillis);

        String resultLog = "";

        int start = 0;
        int stop = powerLogList.size() - 1;
        int x = 0, y;
        int min_series = 80;
        int max_series = 280;
        int count_added = 0;
        // find the first element
        for (int i = stop; i > start; i--) {
            PowerLogItem powerLogItem = powerLogList.get(i);
            y = powerLogItem.getLevel();
            if (count_added <= min_series || count_added > min_series && count_added <= max_series && y < 100) {
                count_added++;
            }
        }

        LineGraphSeries<DataPoint> series = new LineGraphSeries<>();
        for (int i = powerLogList.size() - count_added; i < powerLogList.size(); i++) {
            PowerLogItem powerLogItem = powerLogList.get(i);
            y = powerLogItem.getLevel();
            series.appendData(new DataPoint(x++, y), false, x);
            resultLog += powerLogItem + "\n";
            count_added++;
        }
        startMillis = logMillis("Millis after create dataPoints " + x, startMillis);
        powerApp.setLineGraphSeriesSize(x);
        series.setColor(Color.RED);
        series.setThickness(2);
        powerApp.setLineGraphSeries(series);
    }

    private long logMillis(String message, long startMillis) {
        long afterDbMillis = System.currentTimeMillis();
        Log.i(message, "" + (afterDbMillis - startMillis));
        return afterDbMillis;
    }

    private void startApp() {
        Intent intent = new Intent(Splash.this, MainActivity.class);
        startActivity(intent);
    }

}
