/*
 * Copyright 2014 Heinrich Reimer Software
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
