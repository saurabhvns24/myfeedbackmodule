package com.customer_alliance.sdk.view;

import static com.customer_alliance.sdk.constant.CommonConstants.CIRCLE_RATING_TYPE;
import static com.customer_alliance.sdk.constant.CommonConstants.STAR_RATING_TYPE;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.customer_alliance.sdk.constant.CommonConstants;
import com.customer_alliance.sdk.constant.RequestConstant;
import com.customer_alliance.sdk.constant.TextConstants;
import com.customer_alliance.sdk.listener.onItemClickListener;
import com.customer_alliance.sdk.adapter.MultipleChoiceAdapter;
import com.customer_alliance.sdk.R;
import com.customer_alliance.sdk.constant.BitMapImageConstant;
import com.customer_alliance.sdk.model.AnswerValue;
import com.customer_alliance.sdk.model.CheckedAndUncheckedOption;
import com.customer_alliance.sdk.model.Choice;
import com.customer_alliance.sdk.model.Element;
import com.customer_alliance.sdk.model.PreparedRequestBodyForAnswer;
import com.customer_alliance.sdk.model.SubElementCondition;
import com.customer_alliance.sdk.submitAnswer.SubmitAnswer;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ShowAlterDialog implements onItemClickListener {
    Activity activity;
    AlertDialog alertDialog;
    private AlertDialog.Builder builder;
    private View view;
    private static final String TAG = "ShowAlterDialog";
    private String ratingType;
    private List<CheckedAndUncheckedOption> optionLookUpList;
    private AutoCompleteTextView multipleChoiceAutoCompleteTextView;
    private StringBuilder subElementRequestJSON;
    private Map<String, AnswerValue> sub_question_answers = new HashMap<>();

    private List<CheckedAndUncheckedOption> userSelectedOptionList;

    public ShowAlterDialog(Activity activity) {
        this.activity = activity;
    }

    private void setUpAlertDialogView() {
        builder = new AlertDialog.Builder(activity);
        builder.setView(view);
    }

    private void setUpView(int layoutId) {
        LayoutInflater inflater = activity.getLayoutInflater();
        view = inflater.inflate(layoutId, null);
    }

    public void showRatingTypeAlert(int questionId, int numberOfButtons, String question, String zerothPlace, String lastPlace, boolean isRequired, String type, List<SubElementCondition> subElementConditions, boolean isFromSubElement) {
        requiredText(isRequired, view.findViewById(R.id.layout_header_required_textview));
        ((TextView) view.findViewById(R.id.layout_header_textview)).setText(question);
        ((TextView) view.findViewById(R.id.rating_worst)).setText(zerothPlace);
        ((TextView) view.findViewById(R.id.rating_best)).setText(lastPlace);
        (view.findViewById(R.id.customer_allience_text_btn)).setOnClickListener(v -> onClickCustomerAllience());
        removeTextQuestionView(view);
        removeMultipleChoiceTypeQuestion(view);
        showRatingView(view);
        LinearLayout dynamicLinearLayout = view.findViewById(R.id.dynamic_rating_linearlayout);
        dynamicLinearLayout.setWeightSum(numberOfButtons);
        LinearLayout.LayoutParams paramsButton = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f);
        if (ratingType != null && ratingType.equals(STAR_RATING_TYPE)) {
            starRatingType(questionId, paramsButton, dynamicLinearLayout, numberOfButtons, type, subElementConditions);
        } else if (ratingType != null && ratingType.equals(CIRCLE_RATING_TYPE)) {
            circleRatingType(questionId, paramsButton, dynamicLinearLayout, numberOfButtons, type, subElementConditions);
        } else {
            numberRatingType(questionId, paramsButton, dynamicLinearLayout, numberOfButtons, type, subElementConditions);
        }
        view.findViewById(R.id.customer_allience_text_btn).setOnClickListener(v -> onClickCall(v.getId()));
        view.findViewById(R.id.close_button).setOnClickListener(v -> onClickCall(v.getId()));
        if (!isFromSubElement) {
            createAndShowAlertDialog();
        }
    }

    private void circleRatingType(int questionId, LinearLayout.LayoutParams paramsButton, LinearLayout dynamicLinearLayout, int numberOfButtons, String questionType, List<SubElementCondition> subElementConditions) {
        ImageView[] dynamicImageButtons = new ImageView[numberOfButtons];

        for (int buttonNumber = 0; buttonNumber < numberOfButtons; buttonNumber++) {
            int clickedNumber = buttonNumber;
            dynamicImageButtons[buttonNumber] = new ImageView(activity);
            dynamicImageButtons[buttonNumber].setImageBitmap(BitMapImageConstant.iconCircle);
            dynamicImageButtons[buttonNumber].setLayoutParams(paramsButton);
            dynamicImageButtons[buttonNumber].setOnClickListener(v -> clickOnCircleRatingButton(questionId, String.valueOf(clickedNumber), questionType, subElementConditions));
            dynamicLinearLayout.addView(dynamicImageButtons[buttonNumber]);
        }
    }

    private void clickOnCircleRatingButton(int questionId, String answerNumber, String questionType, List<SubElementCondition> subElementConditions) {
        if (subElementConditions == null || subElementConditions.size() == 0) {
            submitAnswerJSon(sub_question_answers, questionId, answerNumber, questionType, null);
        } else {
            for (int i = 0; i < subElementConditions.size(); i++) {
                if (subElementConditions.get(i).getValues().contains(answerNumber)) {
                    setDataAndShowPopUp(subElementConditions.get(i).getElements().get(0));
                }
            }
        }
    }

    private void starRatingType(int questionId, LinearLayout.LayoutParams paramsButton, LinearLayout dynamicLinearLayout, int numberOfButtons, String type, List<SubElementCondition> subElementConditions) {
        ImageView[] dynamicImageButtons = new ImageView[numberOfButtons];
        for (int buttonNumber = 0; buttonNumber < numberOfButtons; buttonNumber++) {
            int clickedNumber = buttonNumber;
            dynamicImageButtons[buttonNumber] = new ImageView(activity);
            dynamicImageButtons[buttonNumber].setImageBitmap(BitMapImageConstant.iconStar);
            dynamicImageButtons[buttonNumber].setLayoutParams(paramsButton);
            dynamicImageButtons[buttonNumber].setOnClickListener(v -> clickOnStarRatingButton(questionId, String.valueOf(clickedNumber), type, subElementConditions));
            dynamicLinearLayout.addView(dynamicImageButtons[buttonNumber]);
        }
    }

    private void clickOnStarRatingButton(int questionId, String answerNumber, String questionType, List<SubElementCondition> subElementConditions) {
        if (subElementConditions == null || subElementConditions.size() == 0) {
            submitAnswerJSon(sub_question_answers, questionId, answerNumber, questionType, null);
        } else {
            for (int i = 0; i < subElementConditions.size(); i++) {
                if (subElementConditions.get(i).getValues().contains(answerNumber)) {
                    setDataAndShowPopUp(subElementConditions.get(i).getElements().get(0));
                }
            }
        }
    }

    private void numberRatingType(int questionId, LinearLayout.LayoutParams paramsButton, LinearLayout dynamicLinearLayout, int numberOfButtons, String type, List<SubElementCondition> subElementConditions) {
        Button[] dynamicButtons = new Button[numberOfButtons];
        for (int buttonNumber = 0; buttonNumber < numberOfButtons; buttonNumber++) {
            dynamicButtons[buttonNumber] = new Button(activity);
            String buttonText = String.valueOf(buttonNumber + 1);
            dynamicButtons[buttonNumber].setText(buttonText);
            dynamicButtons[buttonNumber].setTextColor(activity.getColor(R.color.text_color));
            dynamicButtons[buttonNumber].setTextSize(13);
            dynamicButtons[buttonNumber].setPadding(2, 2, 2, 2);
            dynamicButtons[buttonNumber].setLayoutParams(paramsButton);
            dynamicButtons[buttonNumber].setOnClickListener(v -> clickOnNumberRatingButton(questionId, buttonText, type, subElementConditions));
            dynamicLinearLayout.addView(dynamicButtons[buttonNumber]);
        }
    }

    private void clickOnNumberRatingButton(int questionId, String answerNumber, String questionType, List<SubElementCondition> subElementConditions) {
        if (subElementConditions == null || subElementConditions.size() == 0) {
            submitAnswerJSon(sub_question_answers, questionId, answerNumber, questionType, null);
        } else {
            for (int i = 0; i < subElementConditions.size(); i++) {
                if (subElementConditions.get(i).getValues().contains(answerNumber)) {
                    setDataAndShowPopUp(subElementConditions.get(i).getElements().get(0));
                    break;
                }
            }
        }
    }

    private AnswerValue createSubElement(Map<String, AnswerValue> sub_question_answers, int questionId, String answer, String questionType, String[] choiceList) {
        AnswerValue answerValue;
        HashMap<String, AnswerValue> hashMap = new HashMap<>();
        switch (questionType) {
            case CommonConstants.TEXT_TYPE_QUESTION:
                answerValue = new AnswerValue(sub_question_answers, answer, null, "");
                hashMap.put(String.valueOf(questionId), answerValue);
                return answerValue;
            case CommonConstants.NUMBER_RATING_TYPE_QUESTION:
            case CommonConstants.STAR_RATING_TYPE_QUESTION:
            case CommonConstants.CIRCLE_RATING_TYPE:
                answerValue = new AnswerValue(sub_question_answers, null, null, answer);
                hashMap.put(String.valueOf(questionId), answerValue);
                return answerValue;
            case CommonConstants.CHOICE_QUESTION:
                answerValue = new AnswerValue(sub_question_answers, null, choiceList, null);
                hashMap.put(String.valueOf(questionId), answerValue);
                return answerValue;
        }
        return null;
    }

    private void setDataAndShowPopUp(Element element) {
        String ratingType = element.getQuestion().getType();
        switch (ratingType) {
            case CommonConstants.NUMBER_RATING_TYPE_QUESTION:
                showRatingTypeAlert(element.getQuestion().getId(),
                        Integer.parseInt(element.getQuestion().getRatingScale().getMax().getValue()),
                        element.getQuestion().getLabel(),
                        element.getQuestion().getRatingScale().getMin().getLabel(),
                        element.getQuestion().getRatingScale().getMax().getLabel(),
                        element.getQuestion().getValidation().isRequired(),
                        element.getQuestion().getType(),
                        element.getSub_element_conditions(), true
                );
                break;
            case CommonConstants.TEXT_TYPE_QUESTION:
                showFeedbackMessageTypeAlert(element.getQuestion().getId(), element.getQuestion().getLabel(), element.getQuestion().getValidation().isRequired(), element.getSub_element_conditions(), true);
                break;
            case CommonConstants.CHOICE_QUESTION:
                if (element.getQuestion().getOptions().isMultiple()) {
                    startMultipleChoiceFragment(element.getQuestion().getId(), element.getQuestion().getLabel(), element.getQuestion().getChoices(), element.getQuestion().getValidation().isRequired(), element.getSub_element_conditions(), true);
                } else {
                    startChoiceFragment(element.getQuestion().getId(), element.getQuestion().getLabel(), element.getQuestion().getChoices(), element.getQuestion().getValidation().isRequired(), element.getSub_element_conditions(), true);
                }
                break;
        }
    }

    private void submitAnswerJSon(Map<String, AnswerValue> sub_question_answers, int questionId, String answer, String questionType, String[] choiceList) {
        AnswerValue answerValue;
        HashMap<String, AnswerValue> hashMap = new HashMap<>();
        switch (questionType) {
            case CommonConstants.TEXT_TYPE_QUESTION:
                answerValue = new AnswerValue(sub_question_answers, answer, null, "");
                hashMap.put(String.valueOf(questionId), answerValue);
                break;
            case CommonConstants.NUMBER_RATING_TYPE_QUESTION:
            case CommonConstants.STAR_RATING_TYPE_QUESTION:
            case CommonConstants.CIRCLE_RATING_TYPE:
                answerValue = new AnswerValue(sub_question_answers, null, null, answer);
                hashMap.put(String.valueOf(questionId), answerValue);
                break;
            case CommonConstants.CHOICE_QUESTION:
                answerValue = new AnswerValue(sub_question_answers, null, choiceList, null);
                hashMap.put(String.valueOf(questionId), answerValue);
                break;
        }
        PreparedRequestBodyForAnswer preparedRequestBodyForAnswer = new PreparedRequestBodyForAnswer(RequestConstant.hash,
                RequestConstant.token,
                RequestConstant.version,
                hashMap);
        String requestJSON = new Gson().toJson(preparedRequestBodyForAnswer);
        new SubmitAnswer(activity).submitFeedback(requestJSON);
    }

    private void onClickCall(int id) {
        if (id == R.id.back_button) {
            Log.d(TAG, "onClickCall: back");
        } else if (id == R.id.close_button) {
            Log.d(TAG, "onClickCall: close");
        }
    }

    private void tryToSubmitAnswer() {
    }


    private void removeTextQuestionView(View view) {
        view.findViewById(R.id.feedback_message).setVisibility(View.GONE);
        view.findViewById(R.id.back_button).setVisibility(View.GONE);
        view.findViewById(R.id.submit_button).setVisibility(View.GONE);
    }

    public void showFeedbackMessageTypeAlert(int questionId, String question, boolean isRequired, List<SubElementCondition> sub_element_conditions, boolean isFromSubElement) {
        removeRatingView(view);
        removeMultipleChoiceTypeQuestion(view);
        showTextQuestionView(view);
        requiredText(isRequired, view.findViewById(R.id.layout_header_required_textview));
        ((TextView) view.findViewById(R.id.layout_header_textview)).setText(question);
        (view.findViewById(R.id.customer_allience_text_btn)).setOnClickListener(v -> onClickCustomerAllience());
        view.findViewById(R.id.customer_allience_text_btn).setOnClickListener(v -> onClickCall(v.getId()));
        String feedbackAnswer = ((EditText) view.findViewById(R.id.feedback_message)).getText().toString().trim();
        view.findViewById(R.id.submit_button).setOnClickListener(v -> {
            if (sub_element_conditions == null || sub_element_conditions.size() == 0) {
                submitAnswerJSon(sub_question_answers, questionId, feedbackAnswer, CommonConstants.TEXT_TYPE_QUESTION, null);
            } else {
                for (int i = 0; i < sub_element_conditions.size(); i++) {
                    if (sub_element_conditions.get(i).getValues().contains(feedbackAnswer)) {
                        setDataAndShowPopUp(sub_element_conditions.get(i).getElements().get(0));
                        sub_question_answers.put(String.valueOf(questionId), createSubElement(sub_question_answers, questionId, feedbackAnswer, CommonConstants.CHOICE_QUESTION, null));
                    }
                }
            }
        });
        view.findViewById(R.id.back_button).setOnClickListener(v -> onClickCall(v.getId()));
        if (!isFromSubElement) {
            createAndShowAlertDialog();
        }
    }

    private void createAndShowAlertDialog() {
        alertDialog = builder.create();
        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();
    }

    private void showTextQuestionView(View view) {
        view.findViewById(R.id.feedback_message).setVisibility(View.VISIBLE);
        view.findViewById(R.id.back_button).setVisibility(View.VISIBLE);
        view.findViewById(R.id.submit_button).setVisibility(View.VISIBLE);
    }

    private void showRatingView(View view) {
        view.findViewById(R.id.dynamic_rating_linearlayout).setVisibility(View.VISIBLE);
        view.findViewById(R.id.rating_worst).setVisibility(View.VISIBLE);
        view.findViewById(R.id.rating_best).setVisibility(View.VISIBLE);
        view.findViewById(R.id.close_button).setVisibility(View.VISIBLE);
    }

    private void removeRatingView(View view) {
        view.findViewById(R.id.dynamic_rating_linearlayout).setVisibility(View.GONE);
        view.findViewById(R.id.rating_worst).setVisibility(View.GONE);
        view.findViewById(R.id.rating_best).setVisibility(View.GONE);
        view.findViewById(R.id.close_button).setVisibility(View.GONE);
    }

    public void showThankYou() {
        view.findViewById(R.id.customer_allience_text_btn).setOnClickListener(v -> onClickCall(v.getId()));
        view.findViewById(R.id.close_button).setOnClickListener(v -> onClickCall(v.getId()));
        createAndShowAlertDialog();
    }

    public void closeRatingFragment() {
        alertDialog.dismiss();
    }

    public void closeFeedbackFragment() {
        alertDialog.dismiss();
    }

    public void closeThankYou() {
        alertDialog.dismiss();
    }

    public void loadImageLogo() {
        subElementRequestJSON = new StringBuilder();
        setUpView(R.layout.initial_layout);
        setUpAlertDialogView();
        ImageView imageView = view.findViewById(R.id.logo_powered_by);
        imageView.setImageBitmap(BitMapImageConstant.iconCustomerAlliance);
    }

    public void setRatingType(String starRatingType) {
        ratingType = starRatingType;
    }

    public void setCircleType(String circleRatingType) {
        ratingType = circleRatingType;
    }

    @SuppressLint("ClickableViewAccessibility")
    public void startChoiceFragment(int questionId, String question, List<Choice> choices, boolean isRequired, List<SubElementCondition> sub_element_conditions, boolean isFromSubElement) {
        removeRatingView(view);
        removeTextQuestionView(view);
        showMultipleChoiceTypeQuestion(view);
        requiredText(isRequired, view.findViewById(R.id.layout_header_required_textview));
        ((TextView) view.findViewById(R.id.layout_header_textview)).setText(question);
        ((ImageView) view.findViewById(R.id.dropdown_icon_imageview)).setImageBitmap(BitMapImageConstant.iconCaretDown);
        (view.findViewById(R.id.customer_allience_text_btn)).setOnClickListener(v -> onClickCustomerAllience());
        String[] list = getStringListOfDropdown(choices);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(activity, R.layout.dropdown_item, list);
        AutoCompleteTextView autoCompleteTextView = view.findViewById(R.id.dropdown_auto_complete_textview);
        autoCompleteTextView.setAdapter(arrayAdapter);
        autoCompleteTextView.setOnClickListener(v -> clickedOnAutoCompleteTextView(autoCompleteTextView));
        autoCompleteTextView.setOnItemClickListener((parent, view, position, id) -> onItemSelectedBackground(autoCompleteTextView));
        autoCompleteTextView.setOnFocusChangeListener((v, hasFocus) -> clickedOnAutoCompleteTextView(autoCompleteTextView));
        view.findViewById(R.id.submit_button).setOnClickListener(v -> {
            for (int i = 0; i < choices.size(); i++) {
                if (choices.get(i).getLabel().equals(autoCompleteTextView.getText().toString())) {
                    String[] resultChoiceList = {choices.get(i).getValue()};
                    Map<String, AnswerValue> sub_question_answers = new HashMap<>();
                    if (sub_element_conditions == null || sub_element_conditions.size() == 0) {
                        submitAnswerJSon(sub_question_answers, questionId, null, CommonConstants.CHOICE_QUESTION, resultChoiceList);
                    } else {
                        for (int j = 0; j < sub_element_conditions.size(); j++) {
                            if (sub_element_conditions.get(j).getValues().contains(resultChoiceList[0])) {
                                setDataAndShowPopUp(sub_element_conditions.get(j).getElements().get(0));
                                sub_question_answers.put(String.valueOf(questionId), createSubElement(sub_question_answers, questionId, null, CommonConstants.CHOICE_QUESTION, resultChoiceList));
                                break;
                            }
                        }
                    }
                    break;
                }
            }
        });
        if (!isFromSubElement) {
            createAndShowAlertDialog();
        }

    }

    private void onClickCustomerAllience() {
        Uri uri = Uri.parse(CommonConstants.CUSTOMER_ALLIANCE_URL);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        activity.startActivity(intent);
    }

    public void startMultipleChoiceFragment(int questionId, String question, List<Choice> choices, boolean isRequired, List<SubElementCondition> sub_element_conditions, boolean isFromSubElement) {
        removeRatingView(view);
        removeTextQuestionView(view);
        showMultipleChoiceTypeQuestion(view);
        requiredText(isRequired, view.findViewById(R.id.layout_header_required_textview));
        ((TextView) view.findViewById(R.id.layout_header_textview)).setText(question);
        (view.findViewById(R.id.customer_allience_text_btn)).setOnClickListener(v -> onClickCustomerAllience());
        ((ImageView) view.findViewById(R.id.dropdown_icon_imageview)).setImageBitmap(BitMapImageConstant.iconCaretDown);
        optionLookUpList = getChoiceListOfDropdown(choices);
        userSelectedOptionList = new ArrayList<>();
        MultipleChoiceAdapter multipleChoiceAdapter = new MultipleChoiceAdapter(activity, optionLookUpList, this);
        multipleChoiceAutoCompleteTextView = view.findViewById(R.id.dropdown_auto_complete_textview);
        multipleChoiceAutoCompleteTextView.setAdapter(multipleChoiceAdapter);
        multipleChoiceAutoCompleteTextView.setOnClickListener(v -> clickedOnAutoCompleteTextView(multipleChoiceAutoCompleteTextView));
        multipleChoiceAutoCompleteTextView.setOnItemClickListener((parent, view, position, id) -> onItemSelectedBackground(multipleChoiceAutoCompleteTextView));
        multipleChoiceAutoCompleteTextView.setOnFocusChangeListener((v, hasFocus) -> {
            clickedOnAutoCompleteTextView(multipleChoiceAutoCompleteTextView);
        });
        multipleChoiceAutoCompleteTextView.setOnDismissListener(() -> {
            onDismissDropdown(multipleChoiceAutoCompleteTextView);
            multipleChoiceAutoCompleteTextView.setAdapter(new MultipleChoiceAdapter(activity, optionLookUpList, this));
        });
        view.findViewById(R.id.submit_button).setOnClickListener(v -> {
            String[] resultChoiceList = new String[userSelectedOptionList.size()];
            if (sub_element_conditions == null || sub_element_conditions.size() == 0) {
                for (int index = 0; index < userSelectedOptionList.size(); index++) {
                    resultChoiceList[index] = userSelectedOptionList.get(index).getValue();
                }
                submitAnswerJSon(sub_question_answers, questionId, null, CommonConstants.CHOICE_QUESTION, resultChoiceList);
            } else {
                for (int i = 0; i < sub_element_conditions.size(); i++) {
                    if (sub_element_conditions.get(i).getValues().contains(resultChoiceList[0])) {
                        setDataAndShowPopUp(sub_element_conditions.get(i).getElements().get(0));
                    }
                }
            }
        });
        if (!isFromSubElement) {
            createAndShowAlertDialog();
        }
    }

    private void onDismissDropdown(AutoCompleteTextView autoCompleteTextView) {
        if (userSelectedOptionList.size() == 1) {
            autoCompleteTextView.setText(userSelectedOptionList.get(0).getTitle());
        } else if (userSelectedOptionList.size() > 1) {
            autoCompleteTextView.setText(TextConstants.multiple_selected);
        } else {
            autoCompleteTextView.setText("");
            autoCompleteTextView.setHint(TextConstants.select);
            autoCompleteTextView.setBackgroundResource(0);
            autoCompleteTextView.setBackgroundColor(activity.getColor(R.color.default_color_for_auto_complete));
        }
    }

    @Override
    public void onItemClicked(int position, boolean selected) {
        if (selected) {
            userSelectedOptionList.add(optionLookUpList.get(position));
        } else {
            userSelectedOptionList.remove(optionLookUpList.get(position));
        }
//        onDismissDropdowns();
    }

    private void onDismissDropdowns() {
        if (userSelectedOptionList.size() == 1) {
            multipleChoiceAutoCompleteTextView.setText(userSelectedOptionList.get(0).getTitle());
        } else if (userSelectedOptionList.size() > 1) {
            Log.d(TAG, "onDismissDropdowns:  size>1");
            multipleChoiceAutoCompleteTextView.setText(TextConstants.multiple_selected);
        } else {
            Log.d(TAG, "onDismissDropdowns: else");
            multipleChoiceAutoCompleteTextView.setText("");
//            autoCompleteTextView.setText("");
//            autoCompleteTextView.setHint(TextConstants.select);
//            autoCompleteTextView.setBackgroundResource(0);
//            autoCompleteTextView.setBackgroundColor(activity.getColor(R.color.default_color_for_auto_complete));
        }
//        multipleChoiceAutoCompleteTextView.setAdapter(new MultipleChoiceAdapter(activity, list, this));
    }


    @SuppressLint("UseCompatLoadingForDrawables")
    private void clickedOnAutoCompleteTextView(AutoCompleteTextView autoCompleteTextView) {
        autoCompleteTextView.setBackground(activity.getDrawable(R.drawable.rectangle_edit_text));
        autoCompleteTextView.showDropDown();
    }

    private void onItemSelectedBackground(AutoCompleteTextView autoCompleteTextView) {
        autoCompleteTextView.setBackgroundResource(0);
        autoCompleteTextView.setBackgroundColor(activity.getColor(R.color.default_color_for_auto_complete));
    }

    private String[] getStringListOfDropdown(List<Choice> choiceList) {
        String[] list = new String[choiceList.size()];
        for (int index = 0; index < choiceList.size(); index++) {
            list[index] = choiceList.get(index).getLabel();
        }
        return list;
    }

    private List<CheckedAndUncheckedOption> getChoiceListOfDropdown(List<Choice> choices) {
        List<CheckedAndUncheckedOption> checkedAndUncheckedOptions = new ArrayList<>(choices.size());
        for (int index = 0; index < choices.size(); index++) {
            CheckedAndUncheckedOption checkedAndUncheckedOption = new CheckedAndUncheckedOption();
            checkedAndUncheckedOption.setTitle(choices.get(index).getLabel());
            checkedAndUncheckedOption.setSelected(false);
            checkedAndUncheckedOption.setValue(choices.get(index).getValue());
            checkedAndUncheckedOptions.add(checkedAndUncheckedOption);
        }
        return checkedAndUncheckedOptions;
    }

    private void requiredText(boolean required, TextView textView) {
        if (required) {
            textView.setText(TextConstants.required);
            textView.setVisibility(View.VISIBLE);
        } else {
            textView.setVisibility(View.GONE);
        }
    }

    private void removeMultipleChoiceTypeQuestion(View view) {
        view.findViewById(R.id.dropdown_auto_complete_textview).setVisibility(View.GONE);
        view.findViewById(R.id.submit_button).setVisibility(View.GONE);
        view.findViewById(R.id.back_button).setVisibility(View.GONE);
    }

    private void showMultipleChoiceTypeQuestion(View view) {
        view.findViewById(R.id.dropdown_auto_complete_textview).setVisibility(View.VISIBLE);
        view.findViewById(R.id.submit_button).setVisibility(View.VISIBLE);
        view.findViewById(R.id.back_button).setVisibility(View.VISIBLE);
    }
}
