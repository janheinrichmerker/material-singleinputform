/*
 * Copyright 2015 Heinrich Reimer Software
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.heinrichreimersoftware.singleinputform.steps;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.FrameLayout;

import com.heinrichreimersoftware.singleinputform.R;

public class CheckBoxStep extends Step{
    public static final String DATA_CHECKED = "data_checked";

    private int mTextResId;
    private String mText;

    private StepCheckerAsync mChecker;

    private int mTextColor;

    public CheckBoxStep(Context context, String dataKey, int textResId, int titleResId, int errorResId, int detailsResId, StepCheckerAsync checker) {
        super(context, dataKey, titleResId, errorResId, detailsResId);
        mTextResId = textResId;
        mChecker = checker;
    }

    public CheckBoxStep(Context context, String dataKey, String text, String title, String error, String details, StepCheckerAsync checker) {
        super(context, dataKey, title, error, details);
        mText = text;
        mChecker = checker;
    }

    public CheckBoxStep(Context context, String dataKey, int textResId, int titleResId, int errorResId, int detailsResId, final StepChecker checker) {
        this(context, dataKey, textResId, titleResId, errorResId, detailsResId, new StepCheckerAsync() {
            @Override
            public void check(boolean checked, StepCheckerCallback stepCheckerCallback) {
                if(checker.check(checked))
                    stepCheckerCallback.onInputValid();
                else
                    stepCheckerCallback.onInputInvalid();
            }
        });
    }

    public CheckBoxStep(Context context, String dataKey, String text, String title, String error, String details, final StepChecker checker) {
        this(context, dataKey, text, title, error, details, new StepCheckerAsync() {
            @Override
            public void check(boolean checked, StepCheckerCallback stepCheckerCallback) {
                if(checker.check(checked))
                    stepCheckerCallback.onInputValid();
                else
                    stepCheckerCallback.onInputInvalid();
            }
        });
    }

    public CheckBoxStep(Context context, String dataKey, int textResId, int titleResId, int errorResId, int detailsResId) {
        this(context, dataKey, textResId, titleResId, errorResId, detailsResId, new StepCheckerAsync() {
            @Override
            public void check(boolean checked, StepCheckerCallback stepCheckerCallback) {
                stepCheckerCallback.onInputValid();
            }
        });
    }

    public CheckBoxStep(Context context, String dataKey, String text, String title, String error, String details) {
        this(context, dataKey, text, title, error, details, new StepCheckerAsync() {
            @Override
            public void check(boolean checked, StepCheckerCallback stepCheckerCallback) {
                stepCheckerCallback.onInputValid();
            }
        });
    }

    public static boolean checked(Bundle data, String dataKey){
        boolean checked = false;
        if(data != null && data.containsKey(dataKey)){
            Bundle bundleChecked = data.getBundle(dataKey);
            if(bundleChecked != null){
                checked = bundleChecked.getBoolean(DATA_CHECKED);
            }
        }
        return checked;
    }

    @Override
    public FrameLayout onCreateView() {
        loadTheme();

        FrameLayout layout = (FrameLayout) View.inflate(getContext(), R.layout.view_check_box, null);
        CheckBox checkBox = (CheckBox) layout.getChildAt(0);
        checkBox.setTextColor(mTextColor);

        return layout;
    }

    @Override
    public void updateView(boolean lastStep) {
        if(mTextResId != 0){
            getCheckBox().setText(getContext().getString(mTextResId));
        }
        else{
            getCheckBox().setText(mText);
        }
    }

    @Override
    public FrameLayout getView(){
        if(super.getView() instanceof FrameLayout){
            return (FrameLayout) super.getView();
        }
        throw new ClassCastException("Input view must be FrameLayout");
    }

    private CheckBox getCheckBox(){
        return (CheckBox) getView().getChildAt(0);
    }

    @Override
    public void check(StepCheckerCallback stepCheckerCallback) {
        mChecker.check(getCheckBox().isChecked(), stepCheckerCallback);
    }

    @Override
    protected void onSave() {
        data().putBoolean(DATA_CHECKED, getCheckBox().isChecked());
    }

    @Override
    protected void onRestore() {
        getCheckBox().setChecked(data().getBoolean(DATA_CHECKED, false));
    }

    private void loadTheme(){
		/* Custom values */
        int[] attrs = {android.R.attr.textColorPrimaryInverse};
        TypedArray array = getContext().obtainStyledAttributes(attrs);

        mTextColor = array.getColor(0, 0);

        array.recycle();
    }

    public interface StepCheckerAsync {
        void check(boolean checked, StepCheckerCallback stepCheckerCallback);
    }

    public interface StepChecker {
        boolean check(boolean checked);
    }
}
