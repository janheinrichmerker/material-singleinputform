package com.heinrichreimersoftware.singleinputform.steps;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.heinrichreimersoftware.singleinputform.R;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class DateStep extends TextStep {

	public static final String DATA_YEAR = "data_year";
	public static final String DATA_MONTH = "data_month";
	public static final String DATA_DAY = "data_day";

	private int mYear;
	private int mMonth;
	private int mDay;

	private StepCheckerAsync mChecker;

	public DateStep(Context context, String dataKey, int titleResId, int errorResId, int detailsResId, StepCheckerAsync checker, TextView.OnEditorActionListener l){
		super(context, dataKey, InputType.TYPE_NULL, titleResId, errorResId, detailsResId, new TextStep.StepCheckerAsync(){
			@Override
            public void check(String input, StepCheckerCallback stepCheckerCallback) {
                if(!TextUtils.isEmpty(input))
                    stepCheckerCallback.onInputValid();
                else
                    stepCheckerCallback.onInputInvalid();
            }
		}, l);

		mChecker = checker;

		if(context instanceof FragmentActivity){}
		else{
			throw new ClassCastException("context has to implement FragmentActivity");
		}

		final FragmentManager fragmentManager = ((FragmentActivity)context).getSupportFragmentManager();

		setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){

                Calendar initial = new GregorianCalendar();

                if(mYear >= 0){
                    initial.set(Calendar.YEAR, mYear);
                }
                if(mMonth >= 0){
                    initial.set(Calendar.MONTH, mMonth);
                }
                if(mDay >= 0){
                    initial.set(Calendar.DAY_OF_MONTH, mDay);
                }

				DatePickerDialog.newInstance(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog datePickerDialog, int year, int monthOfYear, int dayOfMonth) {
                        mYear = year;
                        mMonth = monthOfYear;
                        mDay = dayOfMonth;
                        updateText();
                    }
                }, initial.get(Calendar.YEAR), initial.get(Calendar.MONTH), initial.get(Calendar.DAY_OF_MONTH), false).show(fragmentManager, "DateStep");
			}
		});
	}

	public DateStep(Context context, String dataKey, int titleResId, int errorResId, int detailsResId, TextView.OnEditorActionListener l){
		this(context, dataKey, titleResId, errorResId, detailsResId, new StepCheckerAsync(){
			@Override
			public void check(int year, int month, int day, StepCheckerCallback stepCheckerCallback) {
                stepCheckerCallback.onInputValid();
			}
		}, l);
	}

    public DateStep(Context context, String dataKey, int titleResId, int errorResId, int detailsResId, StepCheckerAsync checker){
        this(context, dataKey, titleResId, errorResId, detailsResId, checker, null);
    }

    public DateStep(Context context, String dataKey, int titleResId, int errorResId, int detailsResId, final StepChecker checker){
        this(context, dataKey, titleResId, errorResId, detailsResId, new StepCheckerAsync() {
            @Override
            public void check(int year, int month, int day, StepCheckerCallback stepCheckerCallback) {
                if(checker.check(year, month, day))
                    stepCheckerCallback.onInputValid();
                else
                    stepCheckerCallback.onInputInvalid();
            }
        }, null);
    }

	public DateStep(Context context, String dataKey, int titleResId, int errorResId, int detailsResId){
		this(context, dataKey, titleResId, errorResId, detailsResId, new StepCheckerAsync(){
			@Override
			public void check(int year, int month, int day, StepCheckerCallback stepCheckerCallback) {
					stepCheckerCallback.onInputValid();
			}
		}, null);
	}

	public DateStep(Context context, String dataKey, String title, String error, String details, StepCheckerAsync checker, TextView.OnEditorActionListener l){
		super(context, dataKey, InputType.TYPE_NULL, title, error, details, new TextStep.StepCheckerAsync(){
			@Override
			public void check(String input, StepCheckerCallback stepCheckerCallback) {
				if(!TextUtils.isEmpty(input))
					stepCheckerCallback.onInputValid();
				else
					stepCheckerCallback.onInputInvalid();
			}
		}, l);

		mChecker = checker;

		if(context instanceof FragmentActivity){}
		else{
			throw new ClassCastException("context has to implement FragmentActivity");
		}

		final FragmentManager fragmentManager = ((FragmentActivity)context).getSupportFragmentManager();

		setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				Calendar initial = new GregorianCalendar();

				if (mYear >= 0) {
					initial.set(Calendar.YEAR, mYear);
				}
				if (mMonth >= 0) {
					initial.set(Calendar.MONTH, mMonth);
				}
				if (mDay >= 0) {
					initial.set(Calendar.DAY_OF_MONTH, mDay);
				}

				DatePickerDialog.newInstance(new DatePickerDialog.OnDateSetListener() {
					@Override
					public void onDateSet(DatePickerDialog datePickerDialog, int year, int monthOfYear, int dayOfMonth) {
						mYear = year;
						mMonth = monthOfYear;
						mDay = dayOfMonth;
						updateText();
					}
				}, initial.get(Calendar.YEAR), initial.get(Calendar.MONTH), initial.get(Calendar.DAY_OF_MONTH), false).show(fragmentManager, "DateStep");
			}
		});
	}

    public DateStep(Context context, String dataKey, String title, String error, String details, final StepChecker checker, TextView.OnEditorActionListener l) {
        this(context, dataKey, title, error, details, new StepCheckerAsync() {
            @Override
            public void check(int year, int month, int day, StepCheckerCallback stepCheckerCallback) {
                if(checker.check(year, month, day))
                    stepCheckerCallback.onInputValid();
                else
                    stepCheckerCallback.onInputInvalid();
            }
        }, l);
    }

        public DateStep(Context context, String dataKey, String title, String error, String details, TextView.OnEditorActionListener l){
		this(context, dataKey, title, error, details, new StepCheckerAsync(){
			@Override
			public void check(int year, int month, int day, StepCheckerCallback stepCheckerCallback) {
				stepCheckerCallback.onInputValid();
			}
		}, l);
	}

	public DateStep(Context context, String dataKey, String title, String error, String details, StepCheckerAsync checker){
		this(context, dataKey, title, error, details, checker, null);
	}

	public DateStep(Context context, String dataKey, String title, String error, String details, final StepChecker checker){
		this(context, dataKey, title, error, details, new StepCheckerAsync() {
			@Override
			public void check(int year, int month, int day, StepCheckerCallback stepCheckerCallback) {
				if(checker.check(year, month, day))
					stepCheckerCallback.onInputValid();
				else
					stepCheckerCallback.onInputValid();
			}
		});
	}

	public DateStep(Context context, String dataKey, String title, String error, String details){
		this(context, dataKey, title, error, details, new StepCheckerAsync() {
            @Override
            public void check(int year, int month, int day, StepCheckerCallback stepCheckerCallback) {
                stepCheckerCallback.onInputValid();
            }
        }, null);
	}

	public static int year(Bundle data, String dataKey){
		int year = Integer.MIN_VALUE;
		if(data != null && data.containsKey(dataKey)){
			Bundle bundleYear = data.getBundle(dataKey);
			if(bundleYear != null){
				year = bundleYear.getInt(DATA_YEAR, Integer.MIN_VALUE);
			}
		}
		return year;
	}

	public static int month(Bundle data, String dataKey){
		int month = Integer.MIN_VALUE;
		if(data != null && data.containsKey(dataKey)){
			Bundle bundleMonth = data.getBundle(dataKey);
			if(bundleMonth != null){
				month = bundleMonth.getInt(DATA_MONTH, Integer.MIN_VALUE);
			}
		}
		return month;
	}

	public static int day(Bundle data, String dataKey){
		int day = Integer.MIN_VALUE;
		if(data != null && data.containsKey(dataKey)){
			Bundle bundleDay = data.getBundle(dataKey);
			if(bundleDay != null){
				day = bundleDay.getInt(DATA_DAY, Integer.MIN_VALUE);
			}
		}
		return day;
	}

	private void updateText(){
		String output = "";
		if(mYear >= 0 && mMonth >= 0 && mDay >= 0){
			output = getContext().getString(R.string.date_format, mYear, mMonth + 1, mDay);
		}
		setText(output);
	}

	@Override
	public void check(StepCheckerCallback stepCheckerCallback) {
		mChecker.check(mYear, mMonth, mDay, stepCheckerCallback);
	}

	@Override
	protected void onSave(){
		data().putInt(DATA_YEAR, mYear);
		data().putInt(DATA_MONTH, mMonth);
		data().putInt(DATA_DAY, mDay);
	}

	@Override
	protected void onRestore(){
		mYear = data().getInt(DATA_YEAR, -1);
		mMonth = data().getInt(DATA_MONTH, -1);
		mDay = data().getInt(DATA_DAY, -1);
		updateText();
	}

	public interface StepCheckerAsync {
		void check(int year, int month, int day, StepCheckerCallback stepCheckerCallback);
	}

	public interface StepChecker {
		boolean check(int year, int month, int day);
	}
}
