package com.heinrichreimersoftware.singleinputform.steps;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.doomonafireball.betterpickers.datepicker.DatePickerBuilder;
import com.doomonafireball.betterpickers.datepicker.DatePickerDialogFragment;
import com.heinrichreimersoftware.singleinputform.R;

public class DateStep extends TextStep{

	public static final String DATA_YEAR = "data_year";
	public static final String DATA_MONTH = "data_month";
	public static final String DATA_DAY = "data_day";

	private int mYear;
	private int mMonth;
	private int mDay;

	private StepChecker mChecker;

	private int mDatePickerStyleResId;

	public DateStep(Context context, String dataKey, int titleResId, int errorResId, int detailsResId, StepChecker checker, TextView.OnEditorActionListener l){
		super(context, dataKey, InputType.TYPE_NULL, titleResId, errorResId, detailsResId, new TextStep.StepChecker(){
			@Override
			public boolean check(String input){
				return !TextUtils.isEmpty(input);
			}
		}, l);

		mChecker = checker;

		if(context instanceof FragmentActivity){}
		else{
			throw new ClassCastException("context has to implement FragmentActivity");
		}

		final FragmentManager fragmentManager = ((FragmentActivity)context).getSupportFragmentManager();

		loadTheme();

		setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				DatePickerBuilder datePickerBuilder = new DatePickerBuilder();
				datePickerBuilder
						.setFragmentManager(fragmentManager)
						.setStyleResId(mDatePickerStyleResId)
						.addDatePickerDialogHandler(new DatePickerDialogFragment.DatePickerDialogHandler(){
							@Override
							public void onDialogDateSet(int reference, int year, int monthOfYear, int dayOfMonth){
								mYear = year;
								mMonth = monthOfYear;
								mDay = dayOfMonth;
								updateText();
							}
						});
				if(mYear >= 0){
					datePickerBuilder.setYear(mYear);
				}
				if(mMonth >= 0){
					datePickerBuilder.setMonthOfYear(mMonth);
				}
				if(mDay >= 0){
					datePickerBuilder.setDayOfMonth(mDay);
				}
				datePickerBuilder.show();
			}
		});
	}

	public DateStep(Context context, String dataKey, int titleResId, int errorResId, int detailsResId, TextView.OnEditorActionListener l){
		this(context, dataKey, titleResId, errorResId, detailsResId, new StepChecker(){
			@Override
			public boolean check(int year, int month, int day){
				return true;
			}
		}, l);
	}

	public DateStep(Context context, String dataKey, int titleResId, int errorResId, int detailsResId, StepChecker checker){
		this(context, dataKey, titleResId, errorResId, detailsResId, checker, null);
	}

	public DateStep(Context context, String dataKey, int titleResId, int errorResId, int detailsResId){
		this(context, dataKey, titleResId, errorResId, detailsResId, new StepChecker(){
			@Override
			public boolean check(int year, int month, int day){
				return true;
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
	public boolean check(){
		return mChecker.check(mYear, mMonth, mDay);
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

	private void loadTheme(){
		/* Default values */
		mDatePickerStyleResId = R.style.BetterPickersDialogFragment_Light;

		int themeResId = 0;
		try{
			String packageName = getClass().getPackage().getName();
			PackageManager packageManager = getContext().getPackageManager();
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
		TypedArray array = getContext().obtainStyledAttributes(themeResId, attrs);

		if(array != null){
			TypedArray styleArray = getContext().obtainStyledAttributes(array.getResourceId(0, 0), R.styleable.SingleInputFormStyle);

			if(styleArray != null){
				mDatePickerStyleResId = styleArray.getResourceId(R.styleable.SingleInputFormStyle_sifBetterPickerStyle, mDatePickerStyleResId);
			}
		}
	}

	public static interface StepChecker{
		boolean check(int year, int month, int day);
	}
}
