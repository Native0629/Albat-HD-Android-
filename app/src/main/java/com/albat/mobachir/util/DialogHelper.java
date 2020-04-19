package com.albat.mobachir.util;

import android.content.Context;
import android.graphics.Color;

import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;

public class DialogHelper {
    public static DialogHelper instance = null;

    public static DialogHelper getInstance() {
        if (instance == null)
            instance = new DialogHelper();

        return instance;
    }

    SweetAlertDialog dialog;
    ACProgressFlower loadingDialog;

    public void showLoadingDialog(Context context) {
        showLoadingDialog(context, "");
    }

    public void showLoadingDialog(Context context, String text) {
//        dialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
//        dialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
//        dialog.setTitleText(text);
//        dialog.setCancelable(false);
//        dialog.show();
        loadingDialog = new ACProgressFlower.Builder(context)
                .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                .themeColor(Color.WHITE)
                .text(text)
                .fadeColor(Color.DKGRAY).build();
        loadingDialog.show();
    }

    public void hideLoadingDialog() {
        if (loadingDialog != null)
            loadingDialog.dismiss();
    }

    public void hideLoadingDialogSuccess(Context context, String title) {
        hideLoadingDialogSuccess(context, title, "");
    }

    public void hideLoadingDialogSuccess(Context context, String title, String content) {
        if (loadingDialog != null)
            loadingDialog.dismiss();

        new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText(title)
                .setContentText(content)
                .show();
    }

    public void hideLoadingDialogError(Context context, String title, String content) {
        if (loadingDialog != null)
            loadingDialog.dismiss();

        new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                .setTitleText(title)
                .setContentText(content)
                .show();
    }
}
