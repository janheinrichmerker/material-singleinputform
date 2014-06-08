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
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.heinrichreimersoftware.singleinputform.R;

public class TextStep extends Step{
	public static final String DATA_TEXT = "data_text";
	private int mInputType;
	private StepChecker mChecker;

	private int mEditTextColor;
	private int mEditTextBackgroundColor;

	public TextStep(Context context, String dataKey, int inputType, int titleResId, int errorResId, int detailsResId, StepChecker checker, TextView.OnEditorActionListener l){
		super(context, dataKey, titleResId, errorResId, detailsResId);
		mInputType = inputType;
		mChecker = checker;
		getView().setOnEditorActionListener(l);
	}

	public TextStep(Context context, String dataKey, int inputType, int titleResId, int errorResId, int detailsResId, TextView.OnEditorActionListener l){
		this(context, dataKey, inputType, titleResId, errorResId, detailsResId, new StepChecker(){
			@Override
			public boolean check(String input){
				return true;
			}
		}, l);
	}

	public TextStep(Context context, String dataKey, int inputType, int titleResId, int errorResId, int detailsResId, StepChecker checker){
		this(context, dataKey, inputType, titleResId, errorResId, detailsResId, checker, null);
	}

	public TextStep(Context context, String dataKey, int inputType, int titleResId, int errorResId, int detailsResId){
		this(context, dataKey, inputType, titleResId, errorResId, detailsResId, new StepChecker(){
			@Override
			public boolean check(String input){
				return true;
			}
		}, null);
	}

	public static String text(Bundle data, String dataKey){
		String text = null;
		if(data != null && data.containsKey(dataKey)){
			Bundle bundleText = data.getBundle(dataKey);
			if(bundleText != null){
				text = bundleText.getString(DATA_TEXT);
			}
		}
		return text;
	}

	@Override
	public View onCreateView(){
		loadTheme();
		EditText editText = (EditText) View.inflate(getContext(), R.layout.view_input, null);
		editText.setTextColor(mEditTextColor);
		Drawable background = editText.getBackground();
		if(background != null){
			background.setColorFilter(mEditTextBackgroundColor, PorterDuff.Mode.SRC_IN);
		}
		return editText;
	}

	@Override
	public void updateView(boolean lastStep){
		Log.d("sif", "updateView(" + lastStep + ")");
		if(lastStep){
			setImeOptions(EditorInfo.IME_ACTION_DONE | EditorInfo.IME_FLAG_NO_EXTRACT_UI);
		}
		else{
			setImeOptions(EditorInfo.IME_ACTION_NEXT | EditorInfo.IME_FLAG_NO_EXTRACT_UI);
		}
		getView().setInputType(mInputType);
		if(mInputType == InputType.TYPE_NULL){
			hideSoftInput();
			Log.d("sif", "hideSoftInput()");
		}
		else{
			showSoftInput();
			Log.d("sif", "showSoftInput()");
		}
	}

	@Override
	public TextView getView(){
		if(super.getView() instanceof TextView){
			return (TextView) super.getView();
		}
		throw new ClassCastException("Input view must be TextView");
	}

	@Override
	public boolean check(){
		String inputString = "";
		CharSequence inputText = getView().getText();
		if(inputText != null){
			inputString = inputText.toString();
		}
		return mChecker.check(inputString);
	}

	@Override
	protected void onSave(){
		data().putString(DATA_TEXT, getText().toString());
	}

	@Override
	protected void onRestore(){
		String text = data().getString(DATA_TEXT);
		if(text != null && !text.equals("")){
			setText(text);
		}
		else{
			setText("");
		}
	}

	public CharSequence getText(){
		return getView().getText();
	}

	public void setText(CharSequence text){
		getView().setText(text);
	}

	public void setImeOptions(int imeOptions){
		getView().setImeOptions(imeOptions);
	}

	private void hideSoftInput(){
		Context context = getContext();
		if(context != null){
			InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
		}
	}

	private void showSoftInput(){
		Context context = getContext();
		if(context != null){
			InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
			getView().requestFocus();
			imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
		}
	}

	public void setOnClickListener(View.OnClickListener l){
		getView().setOnClickListener(l);
	}

	private void loadTheme(){
		/* Default values */
		mEditTextColor = getContext().getResources().getColor(R.color.default_input_text_color);
		mEditTextBackgroundColor = getContext().getResources().getColor(R.color.default_edit_text_background_color);

		int themeResId = 0;
		try{
			String packageName = getClass().getPackage().getName();
			PackageManager packageManager = getContext().getPackageManager();
			if(packageManager != null){
				PackageInfo packageInfo = packageManager.getPackageInfo(packageName, PackageManager.GET_META_DATA);

				ApplicationInfo applicationInfo = packageInfo.applicationInfo;
				if(applicationInfo != null){
					themeResId = applicationInfo.theme;
				}
			}
		} catch(PackageManager.NameNotFoundException e){
			e.printStackTrace();
		}


		/* Custom values */
		int[] attrs = {R.attr.sifStyle};
		TypedArray array = getContext().obtainStyledAttributes(themeResId, attrs);

		if(array != null){
			TypedArray styleArray = getContext().obtainStyledAttributes(array.getResourceId(0, 0), R.styleable.SingleInputFormStyle);

			if(styleArray != null){
				mEditTextColor = styleArray.getColor(R.styleable.SingleInputFormStyle_sifInputTextColor, mEditTextColor);
				mEditTextBackgroundColor = styleArray.getColor(R.styleable.SingleInputFormStyle_sifEditTextBackgroundColor, mEditTextBackgroundColor);
			}
		}
	}

	public static interface StepChecker{
		boolean check(String input);
	}
}