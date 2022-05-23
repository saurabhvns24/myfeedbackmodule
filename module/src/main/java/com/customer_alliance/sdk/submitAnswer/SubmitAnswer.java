package com.customer_alliance.sdk.submitAnswer;

import android.content.Context;

import com.customer_alliance.sdk.constant.CommonConstants;
import com.customer_alliance.sdk.constant.DEBUGURLConstant;
import com.customer_alliance.sdk.network.APIManager;

public class SubmitAnswer {
    private final Context context;

    public SubmitAnswer(Context context) {
        this.context = context;
    }

    public void submitFeedback(String requestJSON) {
        new APIManager(context, setUpGET_URL(CommonConstants.HASHCODE), "POST", requestJSON);
    }

    private String setUpGET_URL(String hashCode) {
        return DEBUGURLConstant.BASE_URL + hashCode + "/submit?local=" + context.getResources().getConfiguration().locale;
    }
}
