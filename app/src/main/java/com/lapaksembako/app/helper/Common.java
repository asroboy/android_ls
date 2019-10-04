package com.lapaksembako.app.helper;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;
import android.util.Log;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okio.Buffer;

public class Common {
    public static final String TAG = "LS";
    public static final String IS_WELLCOME = "is_wellcome";
    public static final String IS_AGREE_TOS = "is_agree_tos";
    public static final String IS_LOGIN = "is_login";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";

    public static final String URL_INSTAGRAM = "https://www.instagram.com/wanteknologi/";


            public static final String HOST = "wandevelopment.com";
//    public static final String HOST = "192.168.43.168";
    //    public static final String HOST = "192.168.100.133";
    //    public static final String HOST = "192.168.100.133";
    public static final String BASE_URL = "http://" + HOST + "/lapaksembako-api/public/";
    public static final String keyApi = "q38ji7y3nosppygep984yghr";

    public static final String BASE_URL_PROFILE = "http://" + HOST + "/lapaksembako/upload/member/";
    public static final String BASE_URL_SLIDER = "http://" + HOST + "/lapaksembako/upload/slider/";
    public static final String BASE_URL_PRODUCT = "http://" + HOST + "/lapaksembako/upload/product/";
    public static final String BASE_URL_KATEGORY = "http://" + HOST + "/lapaksembako/upload/category/";

    public static final int REQUEST_UPDATE_PROFILE = 200;
    public static final int REQUEST_ADD_ADDRESS = 201;
    public static final int REQUEST_ADD_BANK = 202;
    public static final int REQUEST_ITEM_DETAIL = 203;
    public static final int REQUEST_PESANAN = 204;


    public static final int ACTION_CART = 100;
    public static final int ACTION_DISLIKE = 101;
    public static final int ACTION_LIKE = 102;

    public static void showWarningDialog(Context context, String message, final OnDialogActionSelected onDialogActionSelected) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Perhatian!");
        builder.setMessage(message);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                onDialogActionSelected.onPositiveClicked(dialog, id);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public static ProgressDialog initProgresDialog(Context context, String title, String message) {
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setTitle(title);
        dialog.setMessage(message);
        return dialog;
    }

    public interface OnDialogActionSelected {
        void onPositiveClicked(DialogInterface dialog, int id);

        void onNegativeClicked(DialogInterface dialog, int id);
    }

    public static String encryp(String password) {
        String credentials = password;
        String pwd = Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
        return pwd;
    }


    public static String bodyToString(final RequestBody request) {
        try {
            final RequestBody copy = request;
            final Buffer buffer = new Buffer();
            if (copy != null)
                copy.writeTo(buffer);
            else
                return "";
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "did not work";
        }
    }


    public static void logD(String message) {
        Log.d(TAG, message);
    }

    public static void logE(String message) {
        Log.d(TAG, "ERROR : " + message);
    }
}
