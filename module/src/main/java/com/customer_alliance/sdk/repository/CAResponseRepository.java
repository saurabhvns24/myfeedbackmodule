package com.customer_alliance.sdk.repository;

import android.content.Context;
import android.util.Log;

import com.customer_alliance.sdk.model.CAResponse;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class CAResponseRepository {
    private static final String TAG = "CAResponseRepository";
    Context context;
    private CAResponse caResponse;

    public CAResponseRepository(Context context) {
        this.context = context;
    }

    public void setJSON(String json) {
        Gson gson = new Gson();
        caResponse = gson.fromJson(json, CAResponse.class);
    }

    public CAResponse getJSON() {
        try {
            InputStream inputStream = context.getAssets().open("ca_question.json");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            String json = new String(buffer, StandardCharsets.UTF_8);
            Gson gson = new Gson();
            CAResponse caResponse = gson.fromJson(json, CAResponse.class);
            Log.d(TAG, "getJSON: " + caResponse.toString());
            return caResponse;
        } catch (Exception e) {
            Log.d(TAG, "getJSON: ");
        }
        return null;
    }
}
