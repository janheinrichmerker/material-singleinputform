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
import android.os.Bundle;
import android.view.View;

public abstract class Step{

	private String key;
	private View inputView;
	private Bundle data;

    private String title;
    private String error;
    private String details;

	private int titleResId;
	private int errorResId;
	private int detailsResId;

    protected Step(Builder builder){
        key = builder.key;
        data = new Bundle();
        title = builder.title;
        error = builder.error;
        details = builder.details;
        titleResId = builder.titleResId;
        errorResId = builder.errorResId;
        detailsResId = builder.detailsResId;
        inputView = onCreateView(builder.context);
	}

	public abstract View onCreateView(Context context);

	public String getTitle(Context context){
		if(title != null && !title.equals("")){
			return title;
		}
		return context.getString(titleResId);
	}

	public String getError(Context context){
		if(error != null && !error.equals("")){
			return error;
		}
		return context.getString(errorResId);
	}

	public String getDetails(Context context){
		if(details != null && !details.equals("")){
			return details;
		}
		return context.getString(detailsResId);
	}

	public abstract void updateView(boolean lastStep);

	public View getView(){
		return inputView;
	}

	public abstract boolean validate();

	public Bundle data(){
		return data;
	}

	public Bundle save(Bundle setupData){
		onSave();
		if(setupData != null){
			setupData.putBundle(key, data);
		}
		return setupData;
	}

	protected abstract void onSave();

	public void restore(Bundle setupData){
		if(setupData != null){
			Bundle data = setupData.getBundle(key);
			if(data != null){
				this.data = data;
			}
		}
		onRestore();
	}

	protected abstract void onRestore();

	public static abstract class Builder{

        protected Context context;
        protected String key;

        protected String title;
        protected String error;
        protected String details;

        protected int titleResId;
        protected int errorResId;
        protected int detailsResId;

        public Builder(Context context, String key){
            this.context = context;
            this.key = key;
        }

        public String title() {
            return title;
        }
        public Builder title(String title) {
            this.title = title;
            return this;
        }

		public int titleResId() {
			return titleResId;
		}
		public Builder titleResId(int titleResId) {
			this.titleResId = titleResId;
			return this;
		}

		public String error() {
			return error;
		}
		public Builder error(String error) {
			this.error = error;
			return this;
		}

		public int errorResId() {
			return errorResId;
		}
		public Builder errorResId(int errorResId) {
			this.errorResId = errorResId;
			return this;
		}

		public String details() {
			return details;
		}
		public Builder details(String details) {
			this.details = details;
			return this;
		}

		public int detailsResId() {
			return detailsResId;
		}
		public Builder detailsResId(int detailsResId) {
			this.detailsResId = detailsResId;
			return this;
		}

        public abstract Step build();
	}
}
