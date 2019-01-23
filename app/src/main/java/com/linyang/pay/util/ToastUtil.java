package com.linyang.pay.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.Toast;

/**
 * 描述:自定义Toast
 * Created by fzJiang on 2017-12-20 17:04
 */
public class ToastUtil {

    private static Toast toast;

    @SuppressLint("ShowToast")
    public static synchronized void showToast(Context context, String message) {
        if (toast == null) {
            toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        } else {
            toast.setText(message);
        }
        toast.show();
    }
}

