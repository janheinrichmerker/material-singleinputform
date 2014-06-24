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

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

public class OptionStep extends TextStep{

	public static final String DATA_SELECTED_OPTION = "data_selected_option";

	private String[] mOptions;
	private int mSelectedItemPos = -1;

	public OptionStep(final Context context, String dataKey, final String[] options, final int titleResId, int errorResId, int detailsResId, TextView.OnEditorActionListener l){
		super(context, dataKey, InputType.TYPE_NULL, titleResId, errorResId, detailsResId, new StepChecker(){
			@Override
			public boolean check(String input){
				return !TextUtils.isEmpty(input);
			}
		}, l);

		mOptions = options;

		setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				builder.setTitle(titleResId)
						.setItems(mOptions, new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								mSelectedItemPos = which;
								updateText();
							}
						});
				builder.show();
			}
		});
	}

	public OptionStep(Context context, String dataKey, String[] options, int titleResId, int errorResId, int detailsResId){
		this(context, dataKey, options, titleResId, errorResId, detailsResId, null);
	}

	public OptionStep(final Context context, String dataKey, int[] optionsResIds, final int titleResId, int errorResId, int detailsResId, TextView.OnEditorActionListener l){
		super(context, dataKey, InputType.TYPE_NULL, titleResId, errorResId, detailsResId, new StepChecker(){
			@Override
			public boolean check(String input){
				return !TextUtils.isEmpty(input);
			}
		}, l);

		String[] options = new String[optionsResIds.length];
		for(int i = 0; i < optionsResIds.length; i++){
			String option = context.getString(optionsResIds[i]);
			if(option != null){
				options[i] = option;
			}
			else{
				options[i] = "";
			}
		}

		mOptions = options;

		setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				builder.setTitle(titleResId)
						.setItems(mOptions, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						mSelectedItemPos = which;
						updateText();
					}
				})
						.show();
			}
		});
	}

	public OptionStep(Context context, String dataKey, int[] optionsResIds, int titleResId, int errorResId, int detailsResId){
		this(context, dataKey, optionsResIds, titleResId, errorResId, detailsResId, null);
	}

	public OptionStep(final Context context, String dataKey, final String[] options, final String title, String error, String details, TextView.OnEditorActionListener l){
		super(context, dataKey, InputType.TYPE_NULL, title, error, details, new StepChecker(){
			@Override
			public boolean check(String input){
				return !TextUtils.isEmpty(input);
			}
		}, l);

		mOptions = options;

		setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				builder.setTitle(title)
						.setItems(mOptions, new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								mSelectedItemPos = which;
								updateText();
							}
						});
				builder.show();
			}
		});
	}

	public OptionStep(Context context, String dataKey, String[] options, String title, String error, String details){
		this(context, dataKey, options, title, error, details, null);
	}

	public OptionStep(final Context context, String dataKey, int[] optionsResIds, final String title, String error, String details, TextView.OnEditorActionListener l){
		super(context, dataKey, InputType.TYPE_NULL, title, error, details, new StepChecker(){
			@Override
			public boolean check(String input){
				return !TextUtils.isEmpty(input);
			}
		}, l);

		String[] options = new String[optionsResIds.length];
		for(int i = 0; i < optionsResIds.length; i++){
			String option = context.getString(optionsResIds[i]);
			if(option != null){
				options[i] = option;
			}
			else{
				options[i] = "";
			}
		}

		mOptions = options;

		setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				builder.setTitle(title)
						.setItems(mOptions, new DialogInterface.OnClickListener(){
							public void onClick(DialogInterface dialog, int which){
								mSelectedItemPos = which;
								updateText();
							}
						})
						.show();
			}
		});
	}

	public OptionStep(Context context, String dataKey, int[] optionsResIds, String title, String error, String details){
		this(context, dataKey, optionsResIds, title, error, details, null);
	}

	public static int selectedOption(Bundle data, String dataKey){
		int selectedOption = -1;
		if(data != null && data.containsKey(dataKey)){
			Bundle bundleSelectedOption = data.getBundle(dataKey);
			if(bundleSelectedOption != null){
				selectedOption = bundleSelectedOption.getInt(DATA_SELECTED_OPTION, -1);
			}
		}
		return selectedOption;
	}

	private void updateText(){
		if(mSelectedItemPos >= 0 && mSelectedItemPos < mOptions.length){
			setText(mOptions[mSelectedItemPos]);
		}
	}


	@Override
	protected void onSave(){
		data().putInt(DATA_SELECTED_OPTION, mSelectedItemPos);
	}

	@Override
	protected void onRestore(){
		int selectedItemPos = data().getInt(DATA_SELECTED_OPTION, -1);
		if(selectedItemPos >= 0){
			mSelectedItemPos = selectedItemPos;
			updateText();
		}
	}
}
