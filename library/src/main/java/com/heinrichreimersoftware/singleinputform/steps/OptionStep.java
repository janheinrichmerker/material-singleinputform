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
import android.text.TextUtils;
import android.view.View;

public class OptionStep extends TextStep {

  public static final String DATA_SELECTED_OPTION = "data_selected_option";

  private int mSelectedItemPos = -1;

  private OptionStep(OptionStepBuilder builder) {
    super(builder);
  }

  @Override
  public View create() {
    View view = super.create();
    view.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        String title = getBuilder().title;
        if (title == null) {
          title = getBuilder().context.getString(getBuilder().titleResId);
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(title)
            .setItems(getBuilder().options, new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface dialog, int which) {
                mSelectedItemPos = which;
                updateText();
              }
            });
        builder.show();
      }
    });
    return view;
  }

  public OptionStepBuilder getBuilder() {
    return (OptionStepBuilder) mBuilder;
  }

  public static int selectedOption(Bundle data, String dataKey) {
    int selectedOption = -1;
    if (data != null && data.containsKey(dataKey)) {
      Bundle bundleSelectedOption = data.getBundle(dataKey);
      if (bundleSelectedOption != null) {
        selectedOption = bundleSelectedOption.getInt(DATA_SELECTED_OPTION, -1);
      }
    }
    return selectedOption;
  }

  private void updateText() {
    if (mSelectedItemPos >= 0 && mSelectedItemPos < getBuilder().options.length) {
      setText(getBuilder().options[mSelectedItemPos]);
    }
  }

  @Override
  protected void onSave() {
    data().putInt(DATA_SELECTED_OPTION, mSelectedItemPos);
  }

  @Override
  protected void onRestore() {
    int selectedItemPos = data().getInt(DATA_SELECTED_OPTION, -1);
    if (selectedItemPos >= 0) {
      mSelectedItemPos = selectedItemPos;
      updateText();
    }
  }

  public static class OptionStepBuilder extends TextStepBuilder<OptionStepBuilder> {

    private String[] options;
    private static final StepChecker staticChecker = new TextStep.StepChecker() {
      @Override
      public boolean check(String input) {
        return !TextUtils.isEmpty(input);
      }
    };

    public OptionStepBuilder(Context context) {
      this.context = context;
    }

    @Override
    public OptionStepBuilder self() {
      return this;
    }

    public OptionStepBuilder setOptions(String[] options) {
      this.options = options;
      return this;
    }

    public OptionStepBuilder setOptionsIds(int[] optionsResIds) {
      options = new String[optionsResIds.length];
      for (int i = 0; i < optionsResIds.length; i++) {
        String option = context.getString(optionsResIds[i]);
        if (option != null) {
          options[i] = option;
        } else {
          options[i] = "";
        }
      }
      return this;
    }

    public OptionStep createStep() {
      if (options == null) {
        throw new IllegalArgumentException("You must setup options");
      }
      checker = staticChecker;
      return new OptionStep(this);
    }
  }
}
