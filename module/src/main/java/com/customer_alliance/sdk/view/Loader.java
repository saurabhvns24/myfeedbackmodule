package com.customer_alliance.sdk.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

import com.customer_alliance.sdk.R;

public class Loader {

    Activity activity;
    AlertDialog dialog;

    public Loader(Activity myActivity) {
        activity = myActivity;
    }

    @SuppressLint("InflateParams")
    public void startLoader() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.loader_layout, null));
        builder.setCancelable(false);

        dialog = builder.create();
        dialog.show();

    }

    public void dismissLoader() {
        dialog.dismiss();
    }
}
