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
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.heinrichreimersoftware.singleinputform.R;

public class TextStep extends Step{

	public static final String DATA_TEXT = "data_text";

	private int inputType;
	private Validator validator;
	private TextView.OnEditorActionListener textWatcher;
	private int textColor;

	protected TextStep(Builder builder){
		super(builder);

		inputType = builder.inputType;
		validator = builder.validator;
		textWatcher = builder.textWatcher;
		textColor = builder.textColor;
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
	public View onCreateView(Context context){
        return View.inflate(context, R.layout.view_input, null);
	}

	@Override
	public void updateView(boolean lastStep){
		if(lastStep){
			setImeOptions(EditorInfo.IME_ACTION_DONE | EditorInfo.IME_FLAG_NO_EXTRACT_UI);
		}
		else{
			setImeOptions(EditorInfo.IME_ACTION_NEXT | EditorInfo.IME_FLAG_NO_EXTRACT_UI);
		}
		getView().setInputType(inputType);
		if(inputType == InputType.TYPE_NULL){
			hideSoftInput();
		}
		else{
			showSoftInput();
		}
        getView().setTextColor(textColor);
        getView().setOnEditorActionListener(textWatcher);
	}

	@Override
	public EditText getView(){
		if(super.getView() instanceof EditText){
			return (EditText) super.getView();
		}
		throw new ClassCastException("View view must be EditText.");
	}

	@Override
	public boolean validate(){
		String inputString = "";
		CharSequence inputText = getView().getText();
		if(inputText != null){
			inputString = inputText.toString();
		}
		return validator.validate(inputString);
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
		Context context = getView().getContext();
		if(context != null){
			InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
		}
	}

	private void showSoftInput(){
		Context context = getView().getContext();
		if(context != null){
			InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
			getView().requestFocus();
			imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
		}
	}

	public void setOnClickListener(View.OnClickListener l){
		getView().setOnClickListener(l);
	}

	public static class Validator {
		public boolean validate(String input){
			return true;
		}
	}

	public static class Builder extends Step.Builder{
		protected int inputType;
		protected Validator validator;
		protected TextView.OnEditorActionListener textWatcher;
		protected int textColor;

		public Builder(Context context, String key) {
			super(context, key);
			loadTheme();
			validator = new Validator();
		}

		public int inputType() {
			return inputType;
		}
		public Builder inputType(int inputType) {
			this.inputType = inputType;
			return this;
		}

		public Validator validator() {
			return validator;
		}
		public Builder validator(Validator validator) {
			this.validator = validator;
			return this;
		}

		public TextView.OnEditorActionListener textWatcher() {
			return textWatcher;
		}
		public Builder textWatcher(TextView.OnEditorActionListener textWatcher) {
			this.textWatcher = textWatcher;
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
			return new TextStep(this);
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