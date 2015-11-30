package com.heinrichreimersoftware.singleinputform.steps;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class DateStep extends TextStep{

	public static final String DATA_YEAR = "data_year";
	public static final String DATA_MONTH = "data_month";
	public static final String DATA_DAY = "data_day";

	private int year;
	private int month;
	private int day;
	private Validator validator;

    protected DateStep(Builder builder){
		super(builder);

		year = builder.year;
		month = builder.month;
		day = builder.day;
		validator = builder.validator;

		setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				Calendar initial = new GregorianCalendar();

				if (year >= 0)
					initial.set(Calendar.YEAR, year);
				if (month >= 0)
					initial.set(Calendar.MONTH, month);
				if (day >= 0)
					initial.set(Calendar.DAY_OF_MONTH, day);


				new DatePickerDialog(getView().getContext(), new DatePickerDialog.OnDateSetListener() {
					@Override
					public void onDateSet(DatePicker datePicker, int year, int month, int day) {
						DateStep.this.year = year;
						DateStep.this.month = month;
						DateStep.this.day = day;
						updateText();
					}
				}, initial.get(Calendar.YEAR), initial.get(Calendar.MONTH), initial.get(Calendar.DAY_OF_MONTH)).show();
			}
		});
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
		if(year >= 0 && month >= 0 && day >= 0){
			DateFormat format = SimpleDateFormat.getDateInstance();
			output = format.format(new GregorianCalendar(year, month, day).getTime());
		}
		setText(output);
	}

	@Override
	public boolean validate(){
		return validator.validate(year, month, day);
	}

	@Override
	protected void onSave(){
		data().putInt(DATA_YEAR, year);
		data().putInt(DATA_MONTH, month);
		data().putInt(DATA_DAY, day);
	}

	@Override
	protected void onRestore(){
		year = data().getInt(DATA_YEAR, -1);
		month = data().getInt(DATA_MONTH, -1);
		day = data().getInt(DATA_DAY, -1);
		updateText();
	}

	public static class Validator extends TextStep.Validator {
		public boolean validate(int year, int month, int day){
			return true;
		}

		@Override
		public final boolean validate(String input){
			return true;
		}
	}

	public static class Builder extends TextStep.Builder{

		protected int year;
		protected int month;
		protected int day;
		protected Validator validator;

		public Builder(Context context, String key) {
			super(context, key);
			validator = new Validator();
		}

		public int year() {
			return year;
		}
		public Builder year(int year) {
			this.year = year;
			return this;
		}

		public int month() {
			return month;
		}
		public Builder month(int month) {
			this.month = month;
			return this;
		}

		public int day() {
			return day;
		}
		public Builder day(int day) {
			this.day = day;
			return this;
		}

		public Validator validator() {
			return validator;
		}
		public Builder validator(Validator validator) {
			this.validator = validator;
			return this;
		}

		@Override
		public Step build() {
			return new DateStep(this);
		}

        /* Casted parent methods */
        public Builder title(String title) {
            return (Builder) super.title(title);
        }
        public Builder titleResId(int titleResId) {
            return (Builder) super.titleResId(titleResId);
        }
        public Builder error(String error) {
            return (Builder) super.error(error);
        }
        public Builder errorResId(int errorResId) {
            return (Builder) super.errorResId(errorResId);
        }
        public Builder details(String details) {
            return (Builder) super.details(details);
        }
        public Builder detailsResId(int detailsResId) {
            return (Builder) super.detailsResId(detailsResId);
        }
        public Builder inputType(int inputType) {
            return (Builder) super.inputType(inputType);
        }
        public Builder textWatcher(TextView.OnEditorActionListener textWatcher) {
            return (Builder) super.textWatcher(textWatcher);
        }
        public Builder textColor(int textColor) {
            return (Builder) super.textColor(textColor);
        }
	}
}
