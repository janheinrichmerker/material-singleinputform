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
import com.heinrichreimersoftware.singleinputform.steps.OptionStep;
import com.heinrichreimersoftware.singleinputform.steps.SeekBarStep;
import com.heinrichreimersoftware.singleinputform.steps.Step;
import com.heinrichreimersoftware.singleinputform.steps.TextStep;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class MainActivity extends SingleInputFormActivity {

  private static final String DATA_KEY_HEIGHT = "height";
  private static final String DATA_KEY_EULA = "eula";
  private static final String DATA_KEY_EMAIL = "email";
  private static final String DATA_KEY_PASSWORD = "password";
  private static final String DATA_KEY_BIRTHDAY = "birthday";
  private static final String DATA_KEY_OPTIONS = "options";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  protected List<Step> getSteps(Context context) {
    long startTime = System.currentTimeMillis();
    List<Step> steps = new ArrayList<Step>();
    steps.add(new CheckBoxStep.CheckBoxStepBuilder().setContext(context)
        .setDataKey(DATA_KEY_EULA)
        .setTextResId(R.string.eula)
        .setTitleResId(R.string.eula_title)
        .setErrorResId(R.string.eula_error)
        .setDetailsResId(R.string.eula_details)
        .setChecker(new CheckBoxStep.StepChecker() {
          @Override
          public boolean check(boolean input) {
            return input;
          }
        })
        .createStep());
    steps.add(new TextStep.TextInnerStepBuilder().setContext(context)
        .setDataKey(DATA_KEY_EMAIL)
        .setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS)
        .setTitleResId(R.string.email)
        .setErrorResId(R.string.email_error)
        .setDetailsResId(R.string.email_details)
        .setChecker(new TextStep.StepChecker() {
          @Override
          public boolean check(String input) {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(input).matches();
          }
        })
        .createStep());
    steps.add(new TextStep.TextInnerStepBuilder().setContext(context)
        .setDataKey(DATA_KEY_PASSWORD)
        .setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)
        .setTitleResId(R.string.password)
        .setErrorResId(R.string.password_error)
        .setDetailsResId(R.string.password_details)
        .setChecker(new TextStep.StepChecker() {
          @Override
          public boolean check(String input) {
            return input.length() >= 5;
          }
        })
        .createStep());
    steps.add(new DateStep.DateStepBuilder().setContext(context)
        .setDataKey(DATA_KEY_BIRTHDAY)
        .setTitleResId(R.string.birthday)
        .setErrorResId(R.string.birthday_error)
        .setDetailsResId(R.string.birthday_details)
        .setDateChecker(new DateStep.DateStepChecker() {
          @Override
          public boolean check(int year, int month, int day) {
            Calendar today = new GregorianCalendar();
            Calendar birthday = new GregorianCalendar(year, month, day);
            today.add(Calendar.YEAR, -14);
            return today.after(birthday);
          }
        })
        .createStep());
    steps.add(new SeekBarStep.SeekBarStepBuilder().setContext(context)
        .setDataKey(DATA_KEY_HEIGHT)
        .setMax(180)
        .setMin(150)
        .setTitleResId(R.string.height)
        .setErrorResId(R.string.height_error)
        .setDetailsResId(R.string.height_details)
        .setChecker(new SeekBarStep.StepChecker() {
          @Override
          public boolean check(int progress) {
            return progress >= 160;
          }
        })
        .createStep());

    steps.add(new OptionStep.OptionStepBuilder(this).setDataKey(DATA_KEY_OPTIONS)
        .setTitleResId(R.string.eula_title)
        .setDetailsResId(R.string.eula_details)
        .setErrorResId(R.string.eula_error)
        .setOptions(new String[] { "Option 1", "Option 2", "Option 3" })
        .createStep());


    Log.d("MPB","TEST: "+(System.currentTimeMillis()-startTime));
    return steps;
  }

  @Override
  protected void onFormFinished(Bundle data) {
    Toast.makeText(this, "Form finished: " +
        CheckBoxStep.checked(data, DATA_KEY_EULA) + ", " +
        TextStep.text(data, DATA_KEY_EMAIL) + ", " +
        TextStep.text(data, DATA_KEY_PASSWORD) + ", " +
        DateStep.day(data, DATA_KEY_BIRTHDAY) + "." + DateStep.month(data, DATA_KEY_BIRTHDAY) + "."
        + DateStep.year(data, DATA_KEY_BIRTHDAY) + ", " +
        SeekBarStep.progress(data, DATA_KEY_HEIGHT), Toast.LENGTH_LONG).show();
    Log.d("MainActivity", "data: " + data.toString());
  }
}
