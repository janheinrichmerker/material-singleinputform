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

import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import com.heinrichreimersoftware.singleinputform.R;

public class CheckBoxStep extends Step {
    public static final String DATA_CHECKED = "data_checked";

    private int mTextColor;

    public CheckBoxStep(CheckBoxStepBuilder checkBoxStepBuilder) {
        super(checkBoxStepBuilder);
    }

    private CheckBoxStepBuilder getBuilder() {
        return (CheckBoxStepBuilder) mBuilder;
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
    public View create() {
        loadTheme();

        FrameLayout layout = (FrameLayout) View.inflate(getContext(), R.layout.view_check_box, null);
        CheckBox checkBox = (CheckBox) layout.getChildAt(0);
        checkBox.setTextColor(mTextColor);

        return layout;
    }

    @Override
    public void updateView(boolean lastStep) {
        if(getBuilder().textResId != 0){
            getCheckBox().setText(getContext().getString(getBuilder().textResId));
        }
        else{
            getCheckBox().setText(getBuilder().text);
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
    public boolean check() {
        return getBuilder().checker.check(getCheckBox().isChecked());
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

    public static class CheckBoxStepBuilder extends StepBuilder<CheckBoxStepBuilder> {

        private int textResId;
        private String text;
        private StepChecker checker;

        public CheckBoxStepBuilder() {
        }

        @Override
        public CheckBoxStepBuilder self() {
            return this;
        }

        public CheckBoxStepBuilder setTextResId(int textResId) {
            this.textResId = textResId;
            return this;
        }

        public CheckBoxStepBuilder setText(String text) {
            this.text = text;
            return this;
        }

        public CheckBoxStepBuilder setChecker(StepChecker checker) {
            this.checker = checker;
            return this;
        }

        public CheckBoxStep createStep() {
            if (checker == null) {
                setChecker(new StepChecker() {
                    @Override
                    public boolean check(boolean checked) {
                        return true;
                    }
                });
            }
            return new CheckBoxStep(this);
        }
    }

    public interface StepChecker{
        boolean check(boolean checked);
    }
}
