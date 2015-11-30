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
import android.view.View;
import android.widget.TextView;

public class OptionStep extends TextStep{

	public static final String DATA_SELECTED_OPTION = "data_selected_option";

	private String[] options;
	private int optionsResId;
	private int selectedOption = -1;

    protected OptionStep(Builder builder){
		super(builder);

		options = builder.options;
		optionsResId = builder.optionsResId;
		selectedOption = builder.selectedOption;

		setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(v.getContext());
				dialogBuilder.setTitle(getTitle(v.getContext()));
				if(optionsResId != 0){
					dialogBuilder.setItems(optionsResId, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							selectedOption = which;
							updateText();
						}
					});
				}
				else{
					dialogBuilder.setItems(options, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							selectedOption = which;
							updateText();
						}
					});
				}
				dialogBuilder.show();
			}
		});
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
		if(selectedOption >= 0 && selectedOption < options.length){
			setText(options[selectedOption]);
		}
	}


	@Override
	protected void onSave(){
		data().putInt(DATA_SELECTED_OPTION, selectedOption);
	}

	@Override
	protected void onRestore(){
		int selectedOption = data().getInt(DATA_SELECTED_OPTION, -1);
		if(selectedOption >= 0){
			this.selectedOption = selectedOption;
			updateText();
		}
	}

	public static class Builder extends TextStep.Builder{

		protected String[] options;
		protected int optionsResId;
		protected int selectedOption = -1;

		public Builder(Context context, String key) {
			super(context, key);
		}

		public String[] options() {
			return options;
		}
		public Builder options(String[] options) {
			this.options = options;
			return this;
		}
		public int optionsResId() {
			return optionsResId;
		}
		public Builder optionsResId(int optionsResId) {
			this.optionsResId = optionsResId;
			return this;
		}

		public int selectedOption() {
			return selectedOption;
		}
		public Builder selectedOption(int selectedOption) {
			this.selectedOption = selectedOption;
			return this;
		}

		@Override
		public Step build() {
			return new OptionStep(this);
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
        public Builder inputType(int inputType) {
            return (Builder) super.inputType(inputType);
        }
        public Builder validator(Validator validator) {
            return (Builder) super.validator(validator);
        }
        public Builder textWatcher(TextView.OnEditorActionListener textWatcher) {
            return (Builder) super.textWatcher(textWatcher);
        }
        public Builder textColor(int textColor) {
            return (Builder) super.textColor(textColor);
        }
	}
}
