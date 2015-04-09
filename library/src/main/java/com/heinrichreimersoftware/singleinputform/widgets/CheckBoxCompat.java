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

package com.heinrichreimersoftware.singleinputform.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.CheckBox;

public class CheckBoxCompat extends CheckBox {

    public CheckBoxCompat(Context context) {
        super(context);
    }

    public CheckBoxCompat(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public CheckBoxCompat(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public int getCompoundPaddingLeft() {

        // Workarround for version codes < Jelly bean 4.2
        // The system does not apply the same padding. Explantion:
        // http://stackoverflow.com/questions/4037795/android-spacing-between-checkbox-and-text/4038195#4038195

        final float scale = this.getResources().getDisplayMetrics().density;
        return (super.getCompoundPaddingLeft() + (int) (10.0f * scale + 0.5f));

    }
}