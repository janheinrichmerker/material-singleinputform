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

import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.heinrichreimersoftware.singleinputform.SingleInputFormActivity;
import com.heinrichreimersoftware.singleinputform.steps.CheckBoxStep;
import com.heinrichreimersoftware.singleinputform.steps.DateStep;
import com.heinrichreimersoftware.singleinputform.steps.SeekBarStep;
import com.heinrichreimersoftware.singleinputform.steps.Step;
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
    protected List<Step> onCreateSteps(){
        List<Step> steps = new ArrayList<>();

        setInputGravity(Gravity.CENTER);

        steps.add(new CheckBoxStep.Builder(this, DATA_KEY_EULA)
                .titleResId(R.string.eula_title)
                .errorResId(R.string.eula_error)
                .detailsResId(R.string.eula_details)
                .textResId(R.string.eula)
                .validator(new CheckBoxStep.Validator() {
                    @Override
                    public boolean validate(boolean input) {
                        return input;
                    }
                })
                .build());

        steps.add(new TextStep.Builder(this, DATA_KEY_EMAIL)
                .titleResId(R.string.email)
                .errorResId(R.string.email_error)
                .detailsResId(R.string.email_details)
                .inputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS)
                .validator(new TextStep.Validator() {
                    @Override
                    public boolean validate(String input) {
                        return Patterns.EMAIL_ADDRESS.matcher(input).matches();
                    }
                })
                .build());

        steps.add(new TextStep.Builder(this, DATA_KEY_PASSWORD)
                .titleResId(R.string.password)
                .errorResId(R.string.password_error)
                .detailsResId(R.string.password_details)
                .inputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)
                .validator(new TextStep.Validator() {
                    @Override
                    public boolean validate(String input) {
                        return input.length() >= 5;
                    }
                })
                .build());

        steps.add(new DateStep.Builder(this, DATA_KEY_BIRTHDAY)
                .titleResId(R.string.birthday)
                .errorResId(R.string.birthday_error)
                .detailsResId(R.string.birthday_details)
                .validator(new DateStep.Validator() {
                    @Override
                    public boolean validate(int year, int month, int day) {
                        Calendar today = new GregorianCalendar();
                        Calendar birthday = new GregorianCalendar(year, month, day);
                        today.add(Calendar.YEAR, -14);
                        return today.after(birthday);
                    }
                })
                .build());

        steps.add(new SeekBarStep.Builder(this, DATA_KEY_HEIGHT)
                .titleResId(R.string.height)
                .errorResId(R.string.height_error)
                .detailsResId(R.string.height_details)
                .min(150)
                .max(190)
                .validator(new SeekBarStep.Validator() {
                    @Override
                    public boolean validate(int progress) {
                        return progress >= 160;
                    }
                })
                .build());

        steps.add(new TextStep.Builder(this, DATA_KEY_CITY)
                .titleResId(R.string.city)
                .errorResId(R.string.city_error)
                .detailsResId(R.string.city_details)
                .inputType(InputType.TYPE_CLASS_TEXT)
                .build());

        return steps;
    }

    @Override
    protected View onCreateFinishedView(LayoutInflater inflater, ViewGroup parent) {
        return inflater.inflate(R.layout.finished_layout, parent, false);
    }

	@Override
	protected void onFormFinished(Bundle data) {
        Toast.makeText(this, "Form finished: " +
                        CheckBoxStep.checked(data, DATA_KEY_EULA) + ", " +
                        TextStep.text(data, DATA_KEY_EMAIL) + ", " +
                        TextStep.text(data, DATA_KEY_PASSWORD) + ", " +
                        DateStep.day(data, DATA_KEY_BIRTHDAY) + "." + DateStep.month(data, DATA_KEY_BIRTHDAY) + "." + DateStep.year(data, DATA_KEY_BIRTHDAY) + ", " +
                        SeekBarStep.progress(data, DATA_KEY_HEIGHT) + ", " +
                        TextStep.text(data, DATA_KEY_CITY),
                Toast.LENGTH_LONG).show();
        Log.d("MainActivity", "data: " + data.toString());

        //Wait 4 seconds and finish
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 4000);

    }
}
