/*
 * Copyright 2015 Heinrich Reimer Software
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

import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import com.heinrichreimersoftware.singleinputform.R;
import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

public class SeekBarStep extends Step {
  public static final String DATA_PROGRESS = "data_progress";

  private int mTextColorPrimaryInverse;
  private int mTextColorSecondaryInverse;
  private int mColorPrimaryDark;

  public SeekBarStep(SeekBarStepBuilder builder) {
    super(builder);
  }

  private SeekBarStepBuilder getBuilder() {
    return (SeekBarStepBuilder) mBuilder;
  }

  public static int progress(Bundle data, String dataKey) {
    int progress = -1;
    if (data != null && data.containsKey(dataKey)) {
      Bundle bundleRange = data.getBundle(dataKey);
      if (bundleRange != null) {
        progress = bundleRange.getInt(DATA_PROGRESS, -1);
      }
    }
    return progress;
  }

  @Override
  public DiscreteSeekBar create() {
    loadTheme();

    return (DiscreteSeekBar) View.inflate(getContext(), R.layout.view_seek_bar, null);
  }

  @Override
  public void updateView(boolean lastStep) {
    getView().setMin(getBuilder().min);
    getView().setMax(getBuilder().max);
    getView().setThumbColor(mTextColorPrimaryInverse, mColorPrimaryDark);
    getView().setScrubberColor(mTextColorPrimaryInverse);
    getView().setTrackColor(mTextColorSecondaryInverse);
  }

  @Override
  public DiscreteSeekBar getView() {
    if (super.getView() instanceof DiscreteSeekBar) {
      return (DiscreteSeekBar) super.getView();
    }
    throw new ClassCastException("Input view must be RangeBar");
  }

  @Override
  public boolean check() {
    return getBuilder().checker.check(getView().getProgress());
  }

  @Override
  protected void onSave() {
    data().putInt(DATA_PROGRESS, getView().getProgress());
    Log.d("SeekBarStep", "onSave() DATA_PROGRESS: " + data().getInt(DATA_PROGRESS));
  }

  @Override
  protected void onRestore() {
    Log.d("SeekBarStep", "onRestore() DATA_PROGRESS: " + data().getInt(DATA_PROGRESS));
    getView().setProgress(data().getInt(DATA_PROGRESS));
  }

  private void loadTheme() {
    /* Custom values */

    int[] attrs = {
        android.R.attr.textColorPrimaryInverse, android.R.attr.textColorSecondaryInverse,
        R.attr.colorPrimaryDark
    };
    TypedArray array = getContext().obtainStyledAttributes(attrs);

    mTextColorPrimaryInverse = array.getColor(0, 0);
    mTextColorSecondaryInverse = array.getColor(1, 0);
    mColorPrimaryDark = array.getColor(2, 0);

    array.recycle();
  }

  public static class SeekBarStepBuilder extends StepBuilder<SeekBarStepBuilder> {

    private StepChecker checker;
    private int min;
    private int max;

    public SeekBarStepBuilder() {
    }

    @Override
    public SeekBarStepBuilder self() {
      return this;
    }

    public SeekBarStepBuilder setChecker(StepChecker mChecker) {
      this.checker = mChecker;
      return this;
    }

    public SeekBarStepBuilder setMin(int mMin) {
      this.min = mMin;
      return this;
    }

    public SeekBarStepBuilder setMax(int mMax) {
      this.max = mMax;
      return this;
    }

    public SeekBarStep createStep() {
      return new SeekBarStep(this);
    }
  }

  public interface StepChecker {
    boolean check(int progress);
  }
}
