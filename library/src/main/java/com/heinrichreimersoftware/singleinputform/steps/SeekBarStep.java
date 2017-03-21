/*
 * MIT License
 *
 * Copyright (c) 2017 Jan Heinrich Reimer
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.heinrichreimersoftware.singleinputform.steps;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.View;

import com.heinrichreimersoftware.singleinputform.R;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

public class SeekBarStep extends Step {

    public static final String DATA_PROGRESS = "data_progress";

    private Validator validator;

    private int min;
    private int max;

    private int textColorPrimaryInverse;
    private int textColorSecondaryInverse;
    private int colorPrimaryDark;

    protected SeekBarStep(Builder builder) {
        super(builder);

        validator = builder.validator;
        min = builder.min;
        max = builder.max;
        textColorPrimaryInverse = builder.textColorPrimaryInverse;
        textColorSecondaryInverse = builder.textColorSecondaryInverse;
        colorPrimaryDark = builder.colorPrimaryDark;
    }

    public static int progress(Bundle data, String dataKey){
        int progress = -1;
        if(data != null && data.containsKey(dataKey)){
            Bundle bundleRange = data.getBundle(dataKey);
            if(bundleRange != null){
                progress = bundleRange.getInt(DATA_PROGRESS, -1);
            }
        }
        return progress;
    }

    @Override
    public DiscreteSeekBar onCreateView(Context context) {
        return  (DiscreteSeekBar) View.inflate(context, R.layout.view_seek_bar, null);
    }

    @Override
    public void updateView(boolean lastStep) {
        getView().setMin(min);
        getView().setMax(max);
        getView().setThumbColor(textColorPrimaryInverse, colorPrimaryDark);
        getView().setScrubberColor(textColorPrimaryInverse);
        getView().setTrackColor(textColorSecondaryInverse);
    }

    @Override
    public DiscreteSeekBar getView(){
        if(super.getView() instanceof DiscreteSeekBar){
            return (DiscreteSeekBar) super.getView();
        }
        throw new ClassCastException("View view must be DiscreteSeekBar.");
    }

    @Override
    public boolean validate() {
        return validator.validate(getView().getProgress());
    }

    @Override
    protected void onSave() {
        data().putInt(DATA_PROGRESS, getView().getProgress());
    }

    @Override
    protected void onRestore() {
        getView().setProgress(data().getInt(DATA_PROGRESS));
    }

    public static class Validator {
        public boolean validate(int progress){
            return true;
        }
    }

    public static class Builder extends Step.Builder{

        protected Validator validator;
        protected int min;
        protected int max;
        protected int textColorPrimaryInverse;
        protected int textColorSecondaryInverse;
        protected int colorPrimaryDark;

        public Builder(Context context, String key) {
            super(context, key);
            loadTheme();
            validator = new Validator();
        }

        public Validator validator() {
            return validator;
        }
        public Builder validator(Validator validator) {
            this.validator = validator;
            return this;
        }

        public int min() {
            return min;
        }
        public Builder min(int min) {
            this.min = min;
            return this;
        }

        public int max() {
            return max;
        }
        public Builder max(int max) {
            this.max = max;
            return this;
        }

        public int textColorPrimaryInverse() {
            return textColorPrimaryInverse;
        }
        public Builder textColorPrimaryInverse(int textColorPrimaryInverse) {
            this.textColorPrimaryInverse = textColorPrimaryInverse;
            return this;
        }

        public int textColorSecondaryInverse() {
            return textColorSecondaryInverse;
        }
        public Builder textColorSecondaryInverse(int textColorSecondaryInverse) {
            this.textColorSecondaryInverse = textColorSecondaryInverse;
            return this;
        }

        public int colorPrimaryDark() {
            return colorPrimaryDark;
        }
        public Builder colorPrimaryDark(int colorPrimaryDark) {
            this.colorPrimaryDark = colorPrimaryDark;
            return this;
        }

        @SuppressWarnings("ResourceType")
        private void loadTheme(){
            int[] attrs = {android.R.attr.textColorPrimaryInverse, android.R.attr.textColorSecondaryInverse, R.attr.colorPrimaryDark};
            TypedArray array = context.obtainStyledAttributes(attrs);

            textColorPrimaryInverse = array.getColor(0, 0);
            textColorSecondaryInverse = array.getColor(1, 0);
            colorPrimaryDark = array.getColor(2, 0);

            array.recycle();
        }

        @Override
        public Step build() {
            return new SeekBarStep(this);
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
