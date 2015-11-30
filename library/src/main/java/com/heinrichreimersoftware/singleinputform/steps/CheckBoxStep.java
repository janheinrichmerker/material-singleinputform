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

    private int textResId;
    private String text;
    private int textColor;
    private Validator validator;

    protected CheckBoxStep(Builder builder) {
        super(builder);
        textResId = builder.textResId;
        text = builder.text;
        textColor = builder.textColor;
        validator = builder.validator;
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
    public View onCreateView(Context context) {
        return View.inflate(context, R.layout.view_check_box, null);
    }

    @Override
    public void updateView(boolean lastStep) {
        if(textResId != 0){
            getCheckBox().setText(textResId);
        }
        else{
            getCheckBox().setText(text);
        }
        getCheckBox().setTextColor(textColor);
    }

    @Override
    public FrameLayout getView(){
        if(super.getView() instanceof FrameLayout){
            return (FrameLayout) super.getView();
        }
        throw new ClassCastException("View view must be FrameLayout.");
    }

    private CheckBox getCheckBox(){
        return (CheckBox) getView().getChildAt(0);
    }

    @Override
    public boolean validate() {
        return validator.validate(getCheckBox().isChecked());
    }

    @Override
    protected void onSave() {
        data().putBoolean(DATA_CHECKED, getCheckBox().isChecked());
    }

    @Override
    protected void onRestore() {
        getCheckBox().setChecked(data().getBoolean(DATA_CHECKED, false));
    }

    public static class Validator {
        public boolean validate(boolean checked){
            return true;
        }
    }

    public static class Builder extends Step.Builder{

        protected int textResId;
        protected String text;

        protected Validator validator;
        protected int textColor;

        public Builder(Context context, String key) {
            super(context, key);
            loadTheme();
            validator = new Validator();
        }

        public int textResId() {
            return textResId;
        }
        public Builder textResId(int textResId) {
            this.textResId = textResId;
            return this;
        }

        public String text() {
            return text;
        }
        public Builder text(String text) {
            this.text = text;
            return this;
        }

        public Validator validator() {
            return validator;
        }
        public Builder validator(Validator validator) {
            this.validator = validator;
            return this;
        }

        public int textColor() {
            return textColor;
        }
        public Builder textColor(int textColor) {
            this.textColor = textColor;
            return this;
        }

        private void loadTheme(){
            int[] attrs = {android.R.attr.textColorPrimaryInverse};
            TypedArray array = context.obtainStyledAttributes(attrs);

            textColor = array.getColor(0, 0);

            array.recycle();
        }

        @Override
        public Step build() {
            return new CheckBoxStep(this);
        }

        /* Casted parent methods */
        public Builder title(String title) {
            return (Builder) super.title(title);
        }
        public Builder titleResId(int titleResId) {
            return (Builder) super.titleResId(titleResId);
        }
        public Builder error(String error) {
            return (Builder) super.error(error);
        }
        public Builder errorResId(int errorResId) {
            return (Builder) super.errorResId(errorResId);
        }
        public Builder details(String details) {
            return (Builder) super.details(details);
        }
        public Builder detailsResId(int detailsResId) {
            return (Builder) super.detailsResId(detailsResId);
        }
    }
}
