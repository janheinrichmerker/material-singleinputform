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

package com.heinrichreimersoftware.singleinputform;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewAnimator;
import android.widget.ViewSwitcher;

import com.heinrichreimersoftware.singleinputform.steps.Step;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.util.Property;

import java.util.ArrayList;
import java.util.List;

public abstract class SingleInputFormActivity extends FragmentActivity{

	private static final String KEY_DATA = "key_data";
	private static final String KEY_STEP_INDEX = "key_step_index";

	private Property<ProgressBar, Integer> PB_PROGRESS_PROPERTY =
			new Property<ProgressBar, Integer>(Integer.class, "PB_PROGRESS_PROPERTY"){

				@Override
				public void set(ProgressBar pb, Integer value){
					pb.setProgress(value);
					setProgressDrawable();
				}

				@Override
				public Integer get(ProgressBar pb){
					return pb.getProgress();
				}
			};
	private static List<Step> sSteps = new ArrayList<Step>();
	private FragmentActivity activity = this;
	private Bundle setupData = new Bundle();
	private int mStepIndex = 0;
	private boolean mErrored;

	private ScrollView mContainerScrollView;
	private TextSwitcher mTitleSwitcher;
	private TextSwitcher mErrorSwitcher;
	private TextSwitcher mDetailsSwitcher;
	private FrameLayout mTextField;
	private ViewAnimator mInputSwitcher;
	private ImageButton mNextButton;
	private ProgressBar mProgressbar;
	private TextView mStepText;

	private View.OnClickListener mOnNextButtonClickListener = new View.OnClickListener(){
		@Override
		public void onClick(View v){
			nextStep();
		}
	};

	private Drawable mButtonNextIcon;
	private Drawable mButtonFinishIcon;

	private int mTextFieldBackgroundColor;
	private int mProgressBackgroundColor;

	private int mTitleTextColor;
	private int mDetailsTextColor;
	private int mErrorTextColor;

	@Override
	public void onBackPressed(){
		if(mStepIndex == 0){
			finish();
		}
		else{
			previousStep();
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_single_input_form);

		loadTheme();

		sSteps = getSteps(this);

		findViews();

		if(savedInstanceState != null){
			setupData = savedInstanceState.getBundle(KEY_DATA);
			mStepIndex = savedInstanceState.getInt(KEY_STEP_INDEX, 0);
		}

		setupTitle();
		setupInput();
		setupError();
		setupDetails();

		mNextButton.setOnClickListener(mOnNextButtonClickListener);
		mErrorSwitcher.setText("");

		updateStep();
	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState){
		super.onSaveInstanceState(savedInstanceState);
		if(savedInstanceState != null){
			setupData = savedInstanceState.getBundle(KEY_DATA);
			mStepIndex = savedInstanceState.getInt(KEY_STEP_INDEX, 0);
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState){
		super.onSaveInstanceState(outState);
		setupData = getCurrentStep().save(setupData);
		outState.putBundle(KEY_DATA, setupData);
		outState.putInt(KEY_STEP_INDEX, mStepIndex);
	}

	protected abstract List<Step> getSteps(Context context);

	private void findViews(){
		mContainerScrollView = (ScrollView) findViewById(R.id.container_scroll_view);
		mTitleSwitcher = (TextSwitcher) findViewById(R.id.title_switcher);
		mErrorSwitcher = (TextSwitcher) findViewById(R.id.error_switcher);
		mDetailsSwitcher = (TextSwitcher) findViewById(R.id.details_switcher);
		mTextField = (FrameLayout) findViewById(R.id.text_field);
		mInputSwitcher = (ViewAnimator) findViewById(R.id.input_switcher);
		mNextButton = (ImageButton) findViewById(R.id.next_button);
		mProgressbar = (ProgressBar) findViewById(R.id.progressbar);
		mStepText = (TextView) findViewById(R.id.step_text);
	}

	protected Step getCurrentStep(){
		return getStep(mStepIndex);
	}

	protected Step getStep(int position){
		Step step = sSteps.get(position);
		step.setContext(this);
		return step;
	}

	private void loadTheme(){
		/* Default values */
		mButtonNextIcon = getResources().getDrawable(R.drawable.ic_action_next_item);
		mButtonFinishIcon = getResources().getDrawable(R.drawable.ic_action_accept);

		mTextFieldBackgroundColor = getResources().getColor(R.color.default_text_field_background_color);
		mProgressBackgroundColor = getResources().getColor(R.color.default_progress_background_color);

		mTitleTextColor = getResources().getColor(R.color.default_title_text_color);
		mDetailsTextColor = getResources().getColor(R.color.default_details_text_color);
		mErrorTextColor = getResources().getColor(R.color.default_error_text_color);

		int themeResId = 0;
		try{
			String packageName = getClass().getPackage().getName();
			PackageManager packageManager = getPackageManager();
			if(packageManager != null){
				PackageInfo packageInfo = packageManager.getPackageInfo(packageName, PackageManager.GET_META_DATA);

				ApplicationInfo applicationInfo = packageInfo.applicationInfo;
				if(applicationInfo != null){
					themeResId = applicationInfo.theme;
				}
			}
		} catch(PackageManager.NameNotFoundException e){
			e.printStackTrace();
		}


		/* Custom values */
		int[] attrs = {R.attr.sifStyle};
		TypedArray array = obtainStyledAttributes(themeResId, attrs);

		if(array != null){
			TypedArray styleArray = obtainStyledAttributes(array.getResourceId(0, 0), R.styleable.SingleInputFormStyle);

			if(styleArray != null){
				Drawable buttonNextIcon = styleArray.getDrawable(R.styleable.SingleInputFormStyle_sifButtonNextIcon);
				if(buttonNextIcon != null){
					mButtonNextIcon = buttonNextIcon;
				}
				Drawable buttonFinishIcon = styleArray.getDrawable(R.styleable.SingleInputFormStyle_sifButtonFinishIcon);
				if(buttonFinishIcon != null){
					mButtonFinishIcon = buttonFinishIcon;
				}

				mTextFieldBackgroundColor = styleArray.getColor(R.styleable.SingleInputFormStyle_sifTextFieldBackgroundColor, mTextFieldBackgroundColor);
				mProgressBackgroundColor = styleArray.getColor(R.styleable.SingleInputFormStyle_sifProgressBackgroundColor, mProgressBackgroundColor);

				mTitleTextColor = styleArray.getColor(R.styleable.SingleInputFormStyle_sifTitleTextColor, mTitleTextColor);
				mDetailsTextColor = styleArray.getColor(R.styleable.SingleInputFormStyle_sifDetailsTextColor, mDetailsTextColor);
				mErrorTextColor = styleArray.getColor(R.styleable.SingleInputFormStyle_sifErrorTextColor, mErrorTextColor);
			}
		}
	}

	private void setupTitle(){
		mTitleSwitcher.setInAnimation(AnimationUtils.loadAnimation(activity, R.anim.slide_in_to_bottom));
		mTitleSwitcher.setOutAnimation(AnimationUtils.loadAnimation(activity, R.anim.slide_out_to_top));

		mTitleSwitcher.setFactory(new ViewSwitcher.ViewFactory(){

			@Override
			public View makeView(){
				TextView view = (TextView) activity.getLayoutInflater().inflate(R.layout.view_title, null);
				if(view != null){
					view.setTextColor(mTitleTextColor);
				}
				return view;
			}
		});

		mTitleSwitcher.setText("");
	}

	private void setupInput(){
		mInputSwitcher.setInAnimation(AnimationUtils.loadAnimation(activity, R.anim.alpha_in));
		mInputSwitcher.setOutAnimation(AnimationUtils.loadAnimation(activity, R.anim.alpha_out));

		mInputSwitcher.removeAllViews();
		for(int i = 0; i < stepsSize(); i++){
			mInputSwitcher.addView(getStep(i).getView());
		}
	}

	private void setupError(){
		mErrorSwitcher.setInAnimation(AnimationUtils.loadAnimation(activity, android.R.anim.slide_in_left));
		mErrorSwitcher.setOutAnimation(AnimationUtils.loadAnimation(activity, android.R.anim.slide_out_right));

		mErrorSwitcher.setFactory(new ViewSwitcher.ViewFactory(){

			@Override
			public View makeView(){
				TextView view = (TextView) activity.getLayoutInflater().inflate(R.layout.view_error, null);
				if(view != null){
					view.setTextColor(mErrorTextColor);
				}
				return view;
			}
		});

		mErrorSwitcher.setText("");
	}

	private void setupDetails(){
		mDetailsSwitcher.setInAnimation(AnimationUtils.loadAnimation(activity, R.anim.alpha_in));
		mDetailsSwitcher.setOutAnimation(AnimationUtils.loadAnimation(activity, R.anim.alpha_out));

		mDetailsSwitcher.setFactory(new ViewSwitcher.ViewFactory(){

			@Override
			public View makeView(){
				TextView view = (TextView) activity.getLayoutInflater().inflate(R.layout.view_details, null);
				if(view != null){
					view.setTextColor(mDetailsTextColor);
				}
				return view;
			}
		});

		mDetailsSwitcher.setText("");
	}

	private void updateStep(){
		if(mStepIndex >= stepsSize()){
			hideSoftInput();
			onFormFinished(setupData);
			finish();
			return;
		}
		updateViews();
		mContainerScrollView.smoothScrollTo(0, 0);
	}

	private int stepsSize(){
		return sSteps.size();
	}

	private void hideSoftInput(){
		InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);

		View v = getCurrentFocus();
		if(v == null) return;

		imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
	}

	protected abstract void onFormFinished(Bundle data);

	private void updateViews(){
		Step step = getCurrentStep();
		step.restore(setupData);

		if(mStepIndex + 1 >= stepsSize()){
			mNextButton.setImageDrawable(mButtonFinishIcon);
			step.updateView(true);
		}
		else{
			mNextButton.setImageDrawable(mButtonNextIcon);
			step.updateView(false);
		}

		setTextFieldBackgroundDrawable();

		mInputSwitcher.setDisplayedChild(mStepIndex);
		mErrorSwitcher.setText("");
		mDetailsSwitcher.setText(step.getDetails());
		mTitleSwitcher.setText(step.getTitle());
		mStepText.setText(getString(R.string.page_number, mStepIndex + 1, stepsSize()));

		((TextView)findViewById(R.id.step_text)).setTextColor(mDetailsTextColor);

		updateProgressbar();
	}

	private void setTextFieldBackgroundDrawable(){
		Drawable background = mTextField.getBackground();
		if(background != null){
			background.setColorFilter(mTextFieldBackgroundColor, PorterDuff.Mode.SRC_IN);
		}
	}

	private void setProgressDrawable(){
		Drawable progressDrawable = mProgressbar.getProgressDrawable();
		if(progressDrawable != null){
			progressDrawable.setColorFilter(mProgressBackgroundColor, PorterDuff.Mode.SRC_IN);
		}
	}

	private void updateProgressbar(){
		mProgressbar.setMax(stepsSize() * 100);
		ObjectAnimator.ofInt(mProgressbar, PB_PROGRESS_PROPERTY, mStepIndex * 100).start();
	}

	protected void previousStep(){
		setupData = getCurrentStep().save(setupData);
		mStepIndex--;
		updateStep();
	}

	protected void nextStep(){
		Step step = getCurrentStep();
		boolean checkStep = checkStep();
		if(!checkStep){
			if(!mErrored){
				mErrored = true;
				mErrorSwitcher.setText(step.getError());
			}
		}
		else{
			mErrored = false;
		}
		if(mErrored){
			return;
		}
		setupData = step.save(setupData);

		mStepIndex++;
		updateStep();
	}

	private boolean checkStep(){
		return getCurrentStep().check();
	}
}