package com.customer_alliance.sdk.network;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.customer_alliance.sdk.constant.CommonConstants;
import com.customer_alliance.sdk.constant.RequestConstant;
import com.customer_alliance.sdk.model.CAResponse;
import com.customer_alliance.sdk.repository.CAResponseRepository;
import com.customer_alliance.sdk.view.Loader;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class APIManager {
    private final Context context;
    private String API_REQUEST_METHOD;
    private String requestData;

    public APIManager(Context context, String getDataURL, String API_REQUEST_METHOD, String requestData) {
        this.context = context;
        this.API_REQUEST_METHOD = API_REQUEST_METHOD;
        MyAsyncTasks myAsyncTasks = new MyAsyncTasks(getDataURL);
        myAsyncTasks.execute();
        this.requestData = requestData;
    }

    @SuppressLint("StaticFieldLeak")
    private class MyAsyncTasks extends AsyncTask<String, String, String> {

        private Loader loader;
        private final String getDataURL;
        private static final String TAG = "MyAsyncTasks";

        public MyAsyncTasks(String getDataURL) {
            this.getDataURL = getDataURL;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loader = new Loader((Activity) context);
            loader.startLoader();
        }

        @Override
        protected String doInBackground(String... params) {
            StringBuilder result = new StringBuilder();
            URL url;
            HttpURLConnection httpURLConnection = null;
            try {
                url = new URL(getDataURL);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod(API_REQUEST_METHOD);
                if (API_REQUEST_METHOD.contains("POST")) {
                    httpURLConnection.setRequestProperty("Content-Type", "application/json");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setChunkedStreamingMode(0);
                    DataOutputStream printout = new DataOutputStream(httpURLConnection.getOutputStream());
                    printout.writeBytes(URLEncoder.encode(requestData, "UTF-8"));
                    printout.flush();
                    printout.close();
                    Log.d(TAG, "MyHttpRequestTask doInBackground : " + httpURLConnection.getResponseCode());
                    Log.d(TAG, "MyHttpRequestTask doInBackground : " + httpURLConnection.getResponseMessage());
                }
                InputStream in = httpURLConnection.getInputStream();
                InputStreamReader isw = new InputStreamReader(in);
                int data = isw.read();
                while (data != -1) {
                    result.append((char) data);
                    data = isw.read();
                }
                return result.toString();
            } catch (IOException e) {
                e.printStackTrace();
                return "Exception: " + e.getMessage();
            } finally {
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
            }
        }

        @Override
        protected void onPostExecute(String s) {
            if (!s.contains("Exception: ")) {
                CAResponseRepository caResponseRepository = new CAResponseRepository(context);
                caResponseRepository.setJSON(s);
                CAResponse caResponse = caResponseRepository.getJSON();
                setRequestConstant(caResponse);
                new DownloadImageFromInternet(context, loader, caResponse).execute(caResponse.getAssets().getIconCaretDown()
                        , caResponse.getAssets().getIconCircle()
                        , caResponse.getAssets().getIconError()
                        , caResponse.getAssets().getIconLoading()
                        , caResponse.getAssets().getIconCustomerAlliance()
                        , caResponse.getAssets().getIconStar());
            } else {
                loader.dismissLoader();
            }
        }
    }

    private void setRequestConstant(CAResponse caResponse) {
        RequestConstant.hash = caResponse.getHash();
        RequestConstant.version = caResponse.getVersion();
        RequestConstant.token = caResponse.getHash();
        CommonConstants.CUSTOMER_ALLIANCE_URL = caResponse.getTranslations().getWebsite_url();
    }
}
