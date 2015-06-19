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
import android.content.res.TypedArray;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import com.heinrichreimersoftware.singleinputform.R;

public class TextStep extends Step {
  public static final String DATA_TEXT = "data_text";

  private int mEditTextColor;

  protected TextStep(TextStepBuilder builder) {
    super(builder);
  }

  private TextStepBuilder getBuilder() {
    return (TextStepBuilder) mBuilder;
  }

  public static String text(Bundle data, String dataKey) {
    String text = null;
    if (data != null && data.containsKey(dataKey)) {
      Bundle bundleText = data.getBundle(dataKey);
      if (bundleText != null) {
        text = bundleText.getString(DATA_TEXT);
      }
    }
    return text;
  }

  @Override
  public View create() {
    loadTheme();

    EditText editText = (EditText) View.inflate(getContext(), R.layout.view_input, null);
    editText.setTextColor(mEditTextColor);

    return editText;
  }

  @Override
  public void updateView(boolean lastStep) {
    Log.d("sif", "updateView(" + lastStep + ")");
    if (lastStep) {
      setImeOptions(EditorInfo.IME_ACTION_DONE | EditorInfo.IME_FLAG_NO_EXTRACT_UI);
    } else {
      setImeOptions(EditorInfo.IME_ACTION_NEXT | EditorInfo.IME_FLAG_NO_EXTRACT_UI);
    }

    getView().setInputType(getBuilder().inputType);
    if (getBuilder().inputType == InputType.TYPE_NULL) {
      hideSoftInput();
      Log.d("sif", "hideSoftInput()");
    } else {
      showSoftInput();
      Log.d("sif", "showSoftInput()");
    }
  }

  @Override
  public TextView getView() {
    if (super.getView() instanceof TextView) {
      return (TextView) super.getView();
    }
    throw new ClassCastException("Input view must be TextView");
  }

  @Override
  public boolean check() {
    String inputString = "";
    CharSequence inputText = getView().getText();
    if (inputText != null) {
      inputString = inputText.toString();
    }
    return getBuilder().checker.check(inputString);
  }

  @Override
  protected void onSave() {
    data().putString(DATA_TEXT, getText().toString());
  }

  @Override
  protected void onRestore() {
    String text = data().getString(DATA_TEXT);
    if (text != null && !text.equals("")) {
      setText(text);
    } else {
      setText("");
    }
  }

  public CharSequence getText() {
    return getView().getText();
  }

  public void setText(CharSequence text) {
    getView().setText(text);
  }

  public void setImeOptions(int imeOptions) {
    getView().setImeOptions(imeOptions);
  }

  private void hideSoftInput() {
    Context context = getContext();
    if (context != null) {
      InputMethodManager imm = (InputMethodManager) context.getSystemService(
          Context.INPUT_METHOD_SERVICE);
      imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }
  }

  private void showSoftInput() {
    Context context = getContext();
    if (context != null) {
      InputMethodManager imm = (InputMethodManager) context.getSystemService(
          Context.INPUT_METHOD_SERVICE);
      getView().requestFocus();
      imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }
  }

  public void setOnClickListener(View.OnClickListener l) {
    getView().setOnClickListener(l);
  }

  private void loadTheme() {
    /* Custom values */
    int[] attrs = { android.R.attr.textColorPrimaryInverse };
    TypedArray array = getContext().obtainStyledAttributes(attrs);

    mEditTextColor = array.getColor(0, 0);

    array.recycle();
  }

  public static class TextInnerStepBuilder extends TextStepBuilder<TextInnerStepBuilder> {

    @Override
    public TextInnerStepBuilder self() {
      return this;
    }

    public TextStep createStep() {
      if (this.checker == null) {
        setChecker(new StepChecker() {
          @Override
          public boolean check(String input) {
            return true;
          }
        });
      }
      return new TextStep(this);
    }
  }

  public abstract static class TextStepBuilder<T extends TextStepBuilder<T>> extends
      StepBuilder<T> {

    private int inputType;
    protected StepChecker checker;

    public TextStepBuilder() {
    }

    public T setChecker(StepChecker checker) {
      this.checker = checker;
      return self();
    }

    public T setInputType(int inputType) {
      this.inputType = inputType;
      return self();
    }

  }

  public interface StepChecker {
    boolean check(String input);
  }
}