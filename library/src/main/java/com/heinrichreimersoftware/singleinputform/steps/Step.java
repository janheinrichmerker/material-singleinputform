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

	public Step(Context context, String dataKey, int titleResId, int errorResId, int detailsResId){
		mContext = context;
		mDataKey = dataKey;
		mTitleResId = titleResId;
		mErrorResId = errorResId;
		mDetailsResId = detailsResId;
		mInputView = onCreateView();
	}

	public void setContext(Context context){
		mContext = context;
	}
	public Context getContext(){
		return mContext;
	}

	public abstract View onCreateView();

	public int getTitleResId(){
		return mTitleResId;
	}

	public int getErrorResId(){
		return mErrorResId;
	}

	public int getDetailsResId(){
		return mDetailsResId;
	}

	public abstract void updateView(boolean lastStep);

	public View getView(){
		return mInputView;
	}

	public abstract boolean check();

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

}
