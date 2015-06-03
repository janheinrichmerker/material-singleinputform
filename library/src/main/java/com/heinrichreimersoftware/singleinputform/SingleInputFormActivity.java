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
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewAnimator;
import android.widget.ViewSwitcher;

import com.heinrichreimersoftware.singleinputform.steps.Step;
import com.heinrichreimersoftware.singleinputform.steps.StepCheckerCallback;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.util.Property;

import java.util.ArrayList;
import java.util.List;

public abstract class SingleInputFormActivity extends ActionBarActivity {

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
	private CardView mTextField;
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

	private int mTextFieldBackgroundColor = -1;
	private int mProgressBackgroundColor = -1;

	private int mTitleTextColor = -1;
	private int mDetailsTextColor = -1;
	private int mErrorTextColor = -1;

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

	@Override
	protected void onPause() {
		hideSoftInput();
		super.onPause();
	}

	protected abstract List<Step> getSteps(Context context);

	private void findViews(){
		mContainerScrollView = (ScrollView) findViewById(R.id.container_scroll_view);
		mTitleSwitcher = (TextSwitcher) findViewById(R.id.title_switcher);
		mErrorSwitcher = (TextSwitcher) findViewById(R.id.error_switcher);
		mDetailsSwitcher = (TextSwitcher) findViewById(R.id.details_switcher);
        mTextField = (CardView) findViewById(R.id.text_field);
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


		/* Custom values */
		int[] attrs = {R.attr.colorPrimary, R.attr.colorPrimaryDark, android.R.attr.textColorPrimary, android.R.attr.textColorSecondary, R.attr.sifNextIcon, R.attr.sifFinishIcon};
		TypedArray array = obtainStyledAttributes(attrs);

        mTextFieldBackgroundColor = array.getColor(0, 0);
        mProgressBackgroundColor = array.getColor(1, 0);
        mTitleTextColor = mErrorTextColor = array.getColor(2, 0);
        mDetailsTextColor = array.getColor(3, 0);

        Drawable buttonNextIcon = array.getDrawable(4);
        if(buttonNextIcon != null){
            mButtonNextIcon = buttonNextIcon;
        }

        Drawable buttonFinishIcon = array.getDrawable(5);
        if(buttonFinishIcon != null){
            mButtonFinishIcon = buttonFinishIcon;
        }

        array.recycle();
	}

    private Animation getAnimation(int animationResId, boolean isInAnimation){
        final Interpolator interpolator;

        if(isInAnimation){
            interpolator = new DecelerateInterpolator(1.0f);
        }
        else{
            interpolator = new AccelerateInterpolator(1.0f);
        }

        Animation animation = AnimationUtils.loadAnimation(activity, animationResId);
        animation.setInterpolator(interpolator);

        return animation;
    }

	private void setupTitle(){
        mTitleSwitcher.setInAnimation(getAnimation(R.anim.slide_in_to_bottom, true));
        mTitleSwitcher.setOutAnimation(getAnimation(R.anim.slide_out_to_top, false));

		mTitleSwitcher.setFactory(new ViewSwitcher.ViewFactory() {

			@Override
			public View makeView() {
				TextView view = (TextView) activity.getLayoutInflater().inflate(R.layout.view_title, null);
				if (view != null) {
					view.setTextColor(mTitleTextColor);
				}
				return view;
			}
		});

		mTitleSwitcher.setText("");
	}

	private void setupInput(){
		mInputSwitcher.setInAnimation(getAnimation(R.anim.alpha_in, true));
		mInputSwitcher.setOutAnimation(getAnimation(R.anim.alpha_out, false));

		mInputSwitcher.removeAllViews();
		for(int i = 0; i < stepsSize(); i++){
			mInputSwitcher.addView(getStep(i).getView());
		}
	}

	private void setupError(){
        mErrorSwitcher.setInAnimation(getAnimation(android.R.anim.slide_in_left, true));
        mErrorSwitcher.setOutAnimation(getAnimation(android.R.anim.slide_out_right, false));

		mErrorSwitcher.setFactory(new ViewSwitcher.ViewFactory(){

			@Override
			public View makeView(){
				TextView view = (TextView) activity.getLayoutInflater().inflate(R.layout.view_error, null);
				if(view != null && mErrorTextColor != -1){
					view.setTextColor(mErrorTextColor);
				}
				return view;
			}
		});

		mErrorSwitcher.setText("");
	}

	private void setupDetails(){
        mDetailsSwitcher.setInAnimation(getAnimation(R.anim.alpha_in, true));
        mDetailsSwitcher.setOutAnimation(getAnimation(R.anim.alpha_out, false));

		mDetailsSwitcher.setFactory(new ViewSwitcher.ViewFactory() {

			@Override
			public View makeView() {
				TextView view = (TextView) activity.getLayoutInflater().inflate(R.layout.view_details, null);
				if (view != null && mDetailsTextColor != -1) {
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

			mStepIndex--;//reset stepIndex to last step
//			finish(); removed to allow for more actions after last step e.g. async posting to the server
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

		if(mStepIndex + 1 >= stepsSize()){
			mNextButton.setImageDrawable(mButtonFinishIcon);
			step.updateView(true);
		}
		else{
			mNextButton.setImageDrawable(mButtonNextIcon);
			step.updateView(false);
		}

		step.restore(setupData);

		setTextFieldBackgroundDrawable();

		mInputSwitcher.setDisplayedChild(mStepIndex);
		mErrorSwitcher.setText("");
		mDetailsSwitcher.setText(step.getDetails());
		mTitleSwitcher.setText(step.getTitle());
		mStepText.setText(getString(R.string.page_number, mStepIndex + 1, stepsSize()));

        mStepText.setTextColor(mDetailsTextColor);

		updateProgressbar();
	}

	private void setTextFieldBackgroundDrawable(){
        if(mTextFieldBackgroundColor != -1) {
            mTextField.setCardBackgroundColor(mTextFieldBackgroundColor);
        }
	}

	private void setProgressDrawable(){
        if(mProgressBackgroundColor != -1) {
            Drawable progressDrawable = mProgressbar.getProgressDrawable();
            if (progressDrawable != null) {
                progressDrawable.setColorFilter(mProgressBackgroundColor, PorterDuff.Mode.SRC_IN);
            }
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
		final Step step = getCurrentStep();

		checkStep(new StepCheckerCallback() {

			@Override
			public void onInputValid() {
				mErrored = false;

				setupData = step.save(setupData);

				mStepIndex++;
				updateStep();
			}

			@Override
			public void onInputInvalid() {
				if(!mErrored){
					mErrored = true;
					mErrorSwitcher.setText(step.getError());
				}
			}

			@Override
			public void onInputInvalid(String error) {
				if(!mErrored){
					mErrored = true;
					mErrorSwitcher.setText(error);
				}
			}
		});
	}

	private void checkStep(StepCheckerCallback stepCheckerCallback){
		getCurrentStep().check(stepCheckerCallback);
	}
}