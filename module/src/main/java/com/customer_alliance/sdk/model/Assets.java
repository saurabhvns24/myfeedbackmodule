package com.customer_alliance.sdk.model;

import java.io.Serializable;

public class Assets implements Serializable {
    private String icon_caret_down;
    private String icon_circle;
    private String icon_customer_alliance;
    private String icon_error;
    private String icon_loading;
    private String icon_star;

    public Assets(String icon_caret_down, String icon_circle, String icon_customer_alliance, String icon_error, String icon_loading, String icon_star) {
        this.icon_caret_down = icon_caret_down;
        this.icon_circle = icon_circle;
        this.icon_customer_alliance = icon_customer_alliance;
        this.icon_error = icon_error;
        this.icon_loading = icon_loading;
        this.icon_star = icon_star;
    }

    public String getIconCaretDown() {
        return icon_caret_down;
    }

    public void setIconCaretDown(String icon_caret_down) {
        this.icon_caret_down = icon_caret_down;
    }

    public String getIconCircle() {
        return icon_circle;
    }

    public void setIconCircle(String icon_circle) {
        this.icon_circle = icon_circle;
    }

    public String getIconCustomerAlliance() {
        return icon_customer_alliance;
    }

    public void setIconCustomerAlliance(String icon_customer_alliance) {
        this.icon_customer_alliance = icon_customer_alliance;
    }

    public String getIconError() {
        return icon_error;
    }

    public void setIconError(String icon_error) {
        this.icon_error = icon_error;
    }

    public String getIconLoading() {
        return icon_loading;
    }

    public void setIconLoading(String icon_loading) {
        this.icon_loading = icon_loading;
    }

    public String getIconStar() {
        return icon_star;
    }

    public void setIconStar(String icon_star) {
        this.icon_star = icon_star;
    }
}
