package com.albat.mobachir.util;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static android.content.ContentValues.TAG;

/**
 * Created by Mohey on 9/21/2017.
 */

public class CLog {
    private static final boolean LOG_ENABLED = true;
    private static final boolean LOG_FILE_ENABLED = false;

    public static void e(String tag, Object value) {
        if (LOG_ENABLED)
            Log.e(tag, value + "");
    }

    public static void e(String tag, Object value, Exception e) {
        if (LOG_ENABLED)
            Log.e(tag, value + "", e);
    }

    public static void e_long(String TAG, String message) {
        if (!LOG_ENABLED)
            return;

        if (message == null) {
            e(TAG, message);
            return;
        }

        int maxLogSize = 2000;
        for (int i = 0; i <= message.length() / maxLogSize; i++) {
            int start = i * maxLogSize;
            int end = (i + 1) * maxLogSize;
            end = end > message.length() ? message.length() : end;
            Log.e(TAG, message.substring(start, end));
        }
    }

    public static void file(String data) {
        if (!LOG_FILE_ENABLED)
            return;
        // Get the directory for the user's public directory.
        final File path = new File(Environment.getExternalStorageDirectory().getPath() + "/fr/");


        // Make sure the path directory exists.
        if (!path.exists()) {
            // Make it, if it doesn't exit
            path.mkdirs();
        }

        final File file = new File(path, "VnuMngr.txt");

        // Save your stream, don't forget to flush() it before closing it.

        try {
            Calendar c = Calendar.getInstance();
            System.out.println("Current time => " + c.getTime());

            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String formattedDate = df.format(c.getTime());

            file.createNewFile();
            FileOutputStream fOut = new FileOutputStream(file, true);
            OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
            myOutWriter.append(formattedDate + ": " + data);

            myOutWriter.close();

            fOut.flush();
            fOut.close();
        } catch (IOException e) {
            CLog.e(TAG, "File write failed: " + e.toString());
        }
    }
}
