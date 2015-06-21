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

	private Context mContext;
	private String mDataKey;
	private View mInputView;
	private Bundle mData = new Bundle();

	private int mTitleResId;
	private int mErrorResId;
	private int mDetailsResId;

	private String mTitle;
	private String mError;
	private String mDetails;

	public Step(Context context, String dataKey, int titleResId, int errorResId, int detailsResId){
		mContext = context;
		mDataKey = dataKey;
		mTitleResId = titleResId;
		mErrorResId = errorResId;
		mDetailsResId = detailsResId;
		mInputView = onCreateView();
	}

	public Step(Context context, String dataKey, String title, String error, String details){
		mContext = context;
		mDataKey = dataKey;
		mTitle = title;
		mError = error;
		mDetails = details;
		mInputView = onCreateView();
	}

	public void setContext(Context context){
		mContext = context;
	}
	public Context getContext(){
		return mContext;
	}

	public abstract View onCreateView();

	@Deprecated
	public int getTitleResId(){
		return mTitleResId;
	}

	public String getTitle(){
		if(mTitle != null && !mTitle.equals("")){
			return mTitle;
		}
		return getContext().getString(mTitleResId);
	}

	@Deprecated
	public int getErrorResId(){
		return mErrorResId;
	}

	public String getError(){
		if(mError != null && !mError.equals("")){
			return mError;
		}
		return getContext().getString(mErrorResId);
	}

	@Deprecated
	public int getDetailsResId(){
		return mDetailsResId;
	}

	public String getDetails(){
		if(mDetails != null && !mDetails.equals("")){
			return mDetails;
		}
		return getContext().getString(mDetailsResId);
	}

	public abstract void updateView(boolean lastStep);

	public View getView(){
		return mInputView;
	}

	public abstract void check(StepCheckerCallback stepCheckerCallback);

	public Bundle data(){
		return mData;
	}

	public Bundle save(Bundle setupData){
		onSave();
		if(setupData != null){
			setupData.putBundle(mDataKey, mData);
		}
		return setupData;
	}

	protected abstract void onSave();

	public void restore(Bundle setupData){
		if(setupData != null){
			Bundle data = setupData.getBundle(mDataKey);
			if(data != null){
				mData = data;
			}
		}
		onRestore();
	}

	protected abstract void onRestore();

	/**
     * Created by user on 6/3/2015.
     */
    public static interface StepCheckerCallback {
        void onInputValid();
        void onInputInvalid();
        void onInputInvalid(String error);
    }
}
