package util;

import android.app.Activity;
import android.os.Build;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;

/**
 * Created by Ajinkya on 9/25/2015.
 */
public class ExceptionHandler implements Thread.UncaughtExceptionHandler {

    File cacheDir;
    StringBuilder errorReport = new StringBuilder();
    StringWriter stackTrace = new StringWriter();
    Date todayDate = new Date();
    private Activity _context;
    private String LINE_SEPARATOR = "\n";

    public ExceptionHandler(Activity context) {
        _context = context;
    }

    @Override
    public void uncaughtException(Thread thread, Throwable exception) {
        errorReport.delete(0, errorReport.length());
        exception.printStackTrace(new PrintWriter(stackTrace));
        errorReport.append("************ CAUSE OF ERROR ************\n\n");
        errorReport.append(stackTrace.toString());

        errorReport.append("\n************ DEVICE INFORMATION ***********\n");
        errorReport.append("Brand: ");
        errorReport.append(Build.BRAND);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Device: ");
        errorReport.append(Build.DEVICE);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Model: ");
        errorReport.append(Build.MODEL);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Id: ");
        errorReport.append(Build.ID);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Product: ");
        errorReport.append(Build.PRODUCT);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("\n************ FIRMWARE ************\n");
        errorReport.append("SDK: ");
        errorReport.append(Build.VERSION.SDK);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Release: ");
        errorReport.append(Build.VERSION.RELEASE);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Incremental: ");
        errorReport.append(Build.VERSION.INCREMENTAL);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Time: ");
        errorReport.append("" + todayDate);
        errorReport.append(LINE_SEPARATOR);

      //  writeErrorToFile(errorReport.toString());

        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(10);
    }

    private void writeErrorToFile(String error) {

        File cacheDir;
        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
            cacheDir = new File(android.os.Environment.getExternalStorageDirectory(), "/SocietyApp/");
        } else {
            cacheDir = _context.getCacheDir();
        }
        if (!cacheDir.exists())
            cacheDir.mkdirs();

        try {
            if (cacheDir.exists()) {
                File noMedia = new File(cacheDir.getAbsolutePath() + "/errorlog.txt");
                if (!noMedia.exists())
                    noMedia.createNewFile();
                /*FileOutputStream fOut = new FileOutputStream(noMedia);
                OutputStreamWriter myOutWriter =
                        new OutputStreamWriter(fOut);
                myOutWriter.append(error.toString());
                myOutWriter.flush();
                myOutWriter.close();
                fOut.close();*/
                //true = append file
                FileWriter fileWritter = new FileWriter(noMedia, true);
                BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
                bufferWritter.write(error.toString());
                bufferWritter.close();
                errorReport.delete(0, errorReport.length());
            } else {
            }
        } catch (Exception e) {
        }
    }
}
