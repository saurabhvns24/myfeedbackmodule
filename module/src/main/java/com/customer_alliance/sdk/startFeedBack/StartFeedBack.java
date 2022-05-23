package com.customer_alliance.sdk.startFeedBack;

import android.content.Context;

import com.customer_alliance.sdk.constant.CommonConstants;
import com.customer_alliance.sdk.constant.DEBUGURLConstant;
import com.customer_alliance.sdk.network.APIManager;

public class StartFeedBack {
    private final Context context;

    public StartFeedBack(Context context, String hash) {
        CommonConstants.HASHCODE = hash;
        this.context = context;
    }

    public void showFeedback() {
        new APIManager(context, setUpGET_URL(), "GET", "");
    }

    private String setUpGET_URL() {
        return DEBUGURLConstant.BASE_URL + CommonConstants.HASHCODE + "/fetch?local=" + context.getResources().getConfiguration().locale;
    }

}
