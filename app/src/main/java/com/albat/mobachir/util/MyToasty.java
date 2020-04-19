package com.albat.mobachir.util;

import android.content.Context;
import android.widget.Toast;

import es.dmoral.toasty.Toasty;

public class MyToasty {
    private static Toast t;

    public enum TOASTY_TYPE {
        INFO,
        WARNING,
        ERROR,
        SUCCESS,
        NORMAL;
    }

    public MyToasty(Context ctx, String message, int length, TOASTY_TYPE type) {
        if (t != null) {
            t.cancel();
            t = null;
        }
        switch (type) {
            case INFO:
                t = Toasty.info(ctx, message, length);
                break;
            case WARNING:
                t = Toasty.warning(ctx, message, length);
                break;
            case ERROR:
                t = Toasty.error(ctx, message, length);
                break;
            case SUCCESS:
                t = Toasty.success(ctx, message, length);
                break;
            case NORMAL:
                t = Toasty.normal(ctx, message, length);
                break;
            default:
                t = Toasty.normal(ctx, message, length);
        }
    }

    public void show() {
        t.show();
    }

    public static void info(Context context, String text) {
        info(context, text, Toast.LENGTH_SHORT);
    }

    public static void info(Context context, String text, int length) {
        new MyToasty(context, text, length, TOASTY_TYPE.INFO).show();
    }

    public static void error(Context context, String text) {
        error(context, text, Toast.LENGTH_SHORT);
    }

    public static void error(Context context, String text, int length) {
        new MyToasty(context, text, length, TOASTY_TYPE.ERROR).show();
    }

    public static void warning(Context context, String text) {
        warning(context, text, Toast.LENGTH_SHORT);
    }

    public static void warning(Context context, String text, int length) {
        new MyToasty(context, text, length, TOASTY_TYPE.WARNING).show();
    }

    public static void success(Context context, String text) {
        success(context, text, Toast.LENGTH_SHORT);
    }

    public static void success(Context context, String text, int length) {
        new MyToasty(context, text, length, TOASTY_TYPE.SUCCESS).show();
    }

    public static void normal(Context context, String text, int length) {
        new MyToasty(context, text, length, TOASTY_TYPE.NORMAL).show();
    }

    public static void cancel() {
        if (t != null) {
            t.cancel();
            t = null;
        }
    }
}