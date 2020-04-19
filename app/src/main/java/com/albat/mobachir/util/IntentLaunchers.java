package com.albat.mobachir.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.util.Log;

public class IntentLaunchers {
    private static final String TAG = "IntentLaunchers";

    public static void startEmailActivity(Context context, String toEmail, String subject, String message) {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        emailIntent.setType("vnd.android.cursor.item/email");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{toEmail});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, message);
        context.startActivity(Intent.createChooser(emailIntent, "Send mail using..."));
    }

    public static void startShareTextActivity(Context context, String title, String text) {
        // populate the share intent with data
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.putExtra(Intent.EXTRA_TEXT, text);
        context.startActivity(Intent.createChooser(share, title));
    }

    public static void openBrowserActivity(Context context, String link) {
        if (!link.startsWith("http://") && !link.startsWith("https://"))
            link = "http://" + link;

        // populate the share intent with data
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
        context.startActivity(browserIntent);
    }

    public static void openGooglePlayPage(Context context) {
        String appPackageName = context.getPackageName();
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }


    public static void openGooglePlayPage(Context context, String appPackageName) {
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }

    public static void openFacebookLink(Context context, String link) {
        Intent intent;
        try {
            context.getPackageManager().getPackageInfo("com.facebook.katana", 0); //Checks if FB is even installed.
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(link)); //Trys to make intent with FB's URI
            context.startActivity(intent);
        } catch (Exception e) {
            openBrowserActivity(context, link);
        }
    }

    public static void openInstagramProfile(Context context, String url) {
        final Intent intent = new Intent(Intent.ACTION_VIEW);
        try {
            PackageManager pm = context.getPackageManager();
            if (pm.getPackageInfo("com.instagram.android", 0) != null) {
                if (url.endsWith("/")) {
                    url = url.substring(0, url.length() - 1);
                }
                final String username = url.substring(url.lastIndexOf("/") + 1);
                // http://stackoverflow.com/questions/21505941/intent-to-open-instagram-user-profile-on-android
                intent.setData(Uri.parse("http://instagram.com/_u/" + username));
                intent.setPackage("com.instagram.android");
                context.startActivity(intent);
                return;
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
        intent.setData(Uri.parse(url));
        context.startActivity(intent);
    }

    public static void openAppNotificationsSettings(Context context) {
        try {
            Intent intent = new Intent();
//            if (android.os.Build.VERSION.SDK_INT > Build.VERSION_CODES.N_MR1) {
//                intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
//                intent.putExtra("android.provider.extra.APP_PACKAGE", context.getPackageName());
//            } else if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
//                intent.putExtra("app_package", context.getPackageName());
//                intent.putExtra("app_uid", context.getApplicationInfo().uid);
//            } else {
//                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//                intent.addCategory(Intent.CATEGORY_DEFAULT);
//                intent.setData(Uri.parse("package:" + context.getPackageName()));
//            }
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.setData(Uri.parse("package:" + context.getPackageName()));

            context.startActivity(intent);
        } catch (Exception e) {
            CLog.e(TAG, e.getMessage(), e);
        }
    }
}
