package com.customer_alliance.sdk.model;

import java.io.Serializable;

public class Translations implements Serializable {
    private String select;
    private String website_url;
    private String poweredBy;
    private String validationTextRequired;
    private String validationTextTooShort;
    private String validationChoiceRequired;
    private String multipleSelected;
    private String required;

    public Translations(String select, String website_url, String poweredBy, String validationTextRequired, String validationTextTooShort, String validationChoiceRequired, String multipleSelected, String required) {
        this.select = select;
        this.website_url = website_url;
        this.poweredBy = poweredBy;
        this.validationTextRequired = validationTextRequired;
        this.validationTextTooShort = validationTextTooShort;
        this.validationChoiceRequired = validationChoiceRequired;
        this.multipleSelected = multipleSelected;
        this.required = required;
    }

    public String getSelect() {
        return select;
    }

    public void setSelect(String select) {
        this.select = select;
    }

    public String getWebsite_url() {
        return website_url;
    }

    public void setWebsite_url(String website_url) {
        this.website_url = website_url;
    }

    public String getPoweredBy() {
        return poweredBy;
    }

    public void setPoweredBy(String poweredBy) {
        this.poweredBy = poweredBy;
    }

    public String getValidationTextRequired() {
        return validationTextRequired;
    }

    public void setValidationTextRequired(String validationTextRequired) {
        this.validationTextRequired = validationTextRequired;
    }

    public String getValidationTextTooShort() {
        return validationTextTooShort;
    }

    public void setValidationTextTooShort(String validationTextTooShort) {
        this.validationTextTooShort = validationTextTooShort;
    }

    public String getValidationChoiceRequired() {
        return validationChoiceRequired;
    }

    public void setValidationChoiceRequired(String validationChoiceRequired) {
        this.validationChoiceRequired = validationChoiceRequired;
    }

    public String getMultipleSelected() {
        return multipleSelected;
    }

    public void setMultipleSelected(String multipleSelected) {
        this.multipleSelected = multipleSelected;
    }

    public String getRequired() {
        return required;
    }

    public void setRequired(String required) {
        this.required = required;
    }
}
