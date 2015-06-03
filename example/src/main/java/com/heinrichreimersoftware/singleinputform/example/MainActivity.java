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

package com.heinrichreimersoftware.singleinputform.example;

import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.widget.Toast;

import com.heinrichreimersoftware.singleinputform.SingleInputFormActivity;
import com.heinrichreimersoftware.singleinputform.steps.CheckBoxStep;
import com.heinrichreimersoftware.singleinputform.steps.DateStep;
import com.heinrichreimersoftware.singleinputform.steps.SeekBarStep;
import com.heinrichreimersoftware.singleinputform.steps.Step;
import com.heinrichreimersoftware.singleinputform.steps.StepCheckerCallback;
import com.heinrichreimersoftware.singleinputform.steps.TextStep;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class MainActivity extends SingleInputFormActivity{

	private static final String DATA_KEY_HEIGHT = "height";
	private static final String DATA_KEY_EULA = "eula";
    private static final String DATA_KEY_EMAIL = "email";
	private static final String DATA_KEY_PASSWORD = "password";
	private static final String DATA_KEY_BIRTHDAY = "birthday";
	private static final String DATA_KEY_CITY = "city";

	@Override
	protected List<Step> getSteps(Context context){
		List<Step> steps = new ArrayList<Step>();
        steps.add(
                new CheckBoxStep(context, DATA_KEY_EULA, R.string.eula, R.string.eula_title, R.string.eula_error, R.string.eula_details, new CheckBoxStep.StepChecker() {
                    @Override
                    public void check(boolean input, StepCheckerCallback stepCheckerCallback) {
                        if(input)
							stepCheckerCallback.onInputValid();
						else
							stepCheckerCallback.onInputInvalid();
                    }
                })
        );
        steps.add(
                new TextStep(context, DATA_KEY_EMAIL, InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS, R.string.email, R.string.email_error, R.string.email_details, new TextStep.StepChecker() {
                    @Override
                    public void check(String input, StepCheckerCallback stepCheckerCallback) {
						if(android.util.Patterns.EMAIL_ADDRESS.matcher(input).matches())
							stepCheckerCallback.onInputValid();
						else
							stepCheckerCallback.onInputInvalid();
                    }
                })
        );
		steps.add(
				new TextStep(context, DATA_KEY_PASSWORD, InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD, R.string.password, R.string.password_error, R.string.password_details, new TextStep.StepChecker() {
					@Override
					public void check(String input, StepCheckerCallback stepCheckerCallback) {
						if(input.length() >= 5)
							stepCheckerCallback.onInputValid();
						else
							stepCheckerCallback.onInputInvalid();
					}
				})
		);
		steps.add(
				new DateStep(context, DATA_KEY_BIRTHDAY, R.string.birthday, R.string.birthday_error, R.string.birthday_details, new DateStep.StepChecker(){
					@Override
					public void check(int year, int month, int day, StepCheckerCallback stepCheckerCallback) {
						Calendar today = new GregorianCalendar();
						Calendar birthday = new GregorianCalendar(year, month, day);
						today.add(Calendar.YEAR, -14);

						if(today.after(birthday))
							stepCheckerCallback.onInputValid();
						else
							stepCheckerCallback.onInputInvalid();
					}
				})
		);
		steps.add(
				new SeekBarStep(context, DATA_KEY_HEIGHT, 150, 180, R.string.height, R.string.height_error, R.string.height_details, new SeekBarStep.StepChecker() {
					@Override
					public void check(int progress, StepCheckerCallback stepCheckerCallback) {
						if(progress >= 160)
							stepCheckerCallback.onInputValid();
						else
							stepCheckerCallback.onInputInvalid();
					}
				})
		);
		steps.add(
				new TextStep(context, DATA_KEY_CITY, InputType.TYPE_CLASS_TEXT, R.string.city, R.string.city_error, R.string.city_details)
		);

		return steps;
	}

	@Override
	protected void onFormFinished(Bundle data){
		Toast.makeText(this, "Form finished: " +
				CheckBoxStep.checked(data, DATA_KEY_EULA) + ", " +
                TextStep.text(data, DATA_KEY_EMAIL) + ", " +
				TextStep.text(data, DATA_KEY_PASSWORD) + ", " +
				DateStep.day(data, DATA_KEY_BIRTHDAY) + "." + DateStep.month(data, DATA_KEY_BIRTHDAY) + "." + DateStep.year(data, DATA_KEY_BIRTHDAY) + ", " +
				SeekBarStep.progress(data, DATA_KEY_HEIGHT) + ", " +
				TextStep.text(data, DATA_KEY_CITY),
				Toast.LENGTH_LONG).show();
		Log.d("MainActivity", "data: " + data.toString());
	}
}
