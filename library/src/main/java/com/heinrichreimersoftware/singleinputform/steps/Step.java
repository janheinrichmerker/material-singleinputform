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
import android.os.Bundle;
import android.view.View;

public abstract class Step {

  protected final StepBuilder mBuilder;
  private Bundle mData = new Bundle();

  protected Step(StepBuilder builder) {
    this.mBuilder = builder;
  }

  public Context getContext() {
    return mBuilder.context;
  }

  @Deprecated
  public int getTitleResId() {
    return mBuilder.titleResId;
  }

  public String getTitle() {
    if (mBuilder.title != null && !mBuilder.title.equals("")) {
      return mBuilder.title;
    }
    return getContext().getString(mBuilder.titleResId);
  }

  @Deprecated
  public int getErrorResId() {
    return mBuilder.errorResId;
  }

  public String getError() {
    if (mBuilder.error != null && !mBuilder.error.equals("")) {
      return mBuilder.error;
    }
    return getContext().getString(mBuilder.errorResId);
  }

  @Deprecated
  public int getDetailsResId() {
    return mBuilder.detailsResId;
  }

  public String getDetails() {
    if (mBuilder.details != null && !mBuilder.details.equals("")) {
      return mBuilder.details;
    }
    return getContext().getString(mBuilder.detailsResId);
  }

  public View getView() {
    if (mBuilder.view == null) {
      mBuilder.view = create();
    }
    return mBuilder.view;
  }

  public Bundle data() {
    return mData;
  }

  public Bundle save(Bundle setupData) {
    onSave();
    if (setupData != null) {
      setupData.putBundle(mBuilder.dataKey, mData);
    }
    return setupData;
  }

  public void restore(Bundle setupData) {
    if (setupData != null) {
      Bundle data = setupData.getBundle(mBuilder.dataKey);
      if (data != null) {
        mData = data;
      }
    }
    onRestore();
  }

  public abstract void updateView(boolean lastStep);

  public abstract boolean check();

  public abstract View create();

  protected abstract void onSave();

  protected abstract void onRestore();

  public static abstract class StepBuilder<T extends StepBuilder<T>> {
    protected Context context;
    protected String dataKey;
    protected int titleResId;
    protected int errorResId;
    protected int detailsResId;
    protected String title;
    protected String error;
    protected String details;
    protected View view;

    public StepBuilder() {
    }

    public abstract T self();

    public T setContext(Context context) {
      this.context = context;
      return self();
    }

    public T setDataKey(String dataKey) {
      this.dataKey = dataKey;
      return self();
    }

    public T setTitleResId(int titleResId) {
      this.titleResId = titleResId;
      return self();
    }

    public T setErrorResId(int errorResId) {
      this.errorResId = errorResId;
      return self();
    }

    public T setDetailsResId(int detailsResId) {
      this.detailsResId = detailsResId;
      return self();
    }

    public T setTitle(String title) {
      this.title = title;
      return self();
    }

    public T setError(String error) {
      this.error = error;
      return self();
    }

    public T setDetails(String details) {
      this.details = details;
      return self();
    }

  }
}
