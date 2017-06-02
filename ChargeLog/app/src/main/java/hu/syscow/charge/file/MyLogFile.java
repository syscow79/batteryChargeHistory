package hu.syscow.charge.file;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import hu.syscow.charge.app.PowerApp;

/**
 * Created by syscow on 2017. 04. 20..
 */

public class MyLogFile {

    private static final MyLogFile INSTANCE = new MyLogFile();

    public static MyLogFile getInstance() {
        return INSTANCE;
    }

    public void log(String power) {
        PrintWriter pw = null;
        FileOutputStream fos;
        try {
            fos = PowerApp.getContext().openFileOutput("myLog.log", PowerApp.getContext().MODE_PRIVATE);
            pw = new PrintWriter(fos);
            DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
            String writeLog = dateFormat.format(new Date()) + " : " + power;
            pw.println(writeLog);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (pw != null) {
                pw.close();
            }
        }
    }
}
