package com.heinrichreimersoftware.singleinputform.steps;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.View;
import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.heinrichreimersoftware.singleinputform.R;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class DateStep extends TextStep {

  public static final String DATA_YEAR = "data_year";
  public static final String DATA_MONTH = "data_month";
  public static final String DATA_DAY = "data_day";

  public DateStep(DateStepBuilder builder) {
    super(builder);
  }

  @Override
  public View create() {
    View view = super.create();
    if (getBuilder().context instanceof FragmentActivity) {
    } else {
      throw new ClassCastException("context has to implement FragmentActivity");
    }

    final FragmentManager
        fragmentManager
        = ((FragmentActivity) getBuilder().context).getSupportFragmentManager();

    view.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        Calendar initial = new GregorianCalendar();

        if (getBuilder().year >= 0) {
          initial.set(Calendar.YEAR, getBuilder().year);
        }
        if (getBuilder().month >= 0) {
          initial.set(Calendar.MONTH, getBuilder().month);
        }
        if (getBuilder().day >= 0) {
          initial.set(Calendar.DAY_OF_MONTH, getBuilder().day);
        }

        DatePickerDialog.newInstance(new DatePickerDialog.OnDateSetListener() {
                                       @Override
                                       public void onDateSet(DatePickerDialog datePickerDialog,
                                           int year, int monthOfYear, int dayOfMonth) {
                                         getBuilder().year = year;
                                         getBuilder().month = monthOfYear;
                                         getBuilder().day = dayOfMonth;
                                         updateText();
                                       }
                                     }, initial.get(Calendar.YEAR), initial.get(Calendar.MONTH),
            initial.get(Calendar.DAY_OF_MONTH), false).show(fragmentManager, "DateStep");
      }
    });
    return view;
  }

  private DateStepBuilder getBuilder() {
    return (DateStepBuilder) mBuilder;
  }

  public static int year(Bundle data, String dataKey) {
    int year = Integer.MIN_VALUE;
    if (data != null && data.containsKey(dataKey)) {
      Bundle bundleYear = data.getBundle(dataKey);
      if (bundleYear != null) {
        year = bundleYear.getInt(DATA_YEAR, Integer.MIN_VALUE);
      }
    }
    return year;
  }

  public static int month(Bundle data, String dataKey) {
    int month = Integer.MIN_VALUE;
    if (data != null && data.containsKey(dataKey)) {
      Bundle bundleMonth = data.getBundle(dataKey);
      if (bundleMonth != null) {
        month = bundleMonth.getInt(DATA_MONTH, Integer.MIN_VALUE);
      }
    }
    return month;
  }

  public static int day(Bundle data, String dataKey) {
    int day = Integer.MIN_VALUE;
    if (data != null && data.containsKey(dataKey)) {
      Bundle bundleDay = data.getBundle(dataKey);
      if (bundleDay != null) {
        day = bundleDay.getInt(DATA_DAY, Integer.MIN_VALUE);
      }
    }
    return day;
  }

  private void updateText() {
    String output = "";
    if (getBuilder().year >= 0 && getBuilder().month >= 0 && getBuilder().day >= 0) {
      output = getContext().getString(R.string.date_format, getBuilder().year,
          getBuilder().month + 1, getBuilder().day);
    }
    setText(output);
  }

  @Override
  public boolean check() {
    return getBuilder().dateChecker.check(getBuilder().year, getBuilder().month, getBuilder().day);
  }

  @Override
  protected void onSave() {
    data().putInt(DATA_YEAR, getBuilder().year);
    data().putInt(DATA_MONTH, getBuilder().month);
    data().putInt(DATA_DAY, getBuilder().day);
  }

  @Override
  protected void onRestore() {
    getBuilder().year = data().getInt(DATA_YEAR, -1);
    getBuilder().month = data().getInt(DATA_MONTH, -1);
    getBuilder().day = data().getInt(DATA_DAY, -1);
    updateText();
  }

  public static class DateStepBuilder extends TextStepBuilder<DateStepBuilder> {

    private int year;
    private int month;
    private int day;
    private DateStepChecker dateChecker;

    private final static TextStep.StepChecker staticChecker = new TextStep.StepChecker() {
      @Override
      public boolean check(String input) {
        return !TextUtils.isEmpty(input);
      }
    };

    public DateStepBuilder() {
    }

    @Override
    public DateStepBuilder self() {
      return this;
    }

    public DateStepBuilder setYear(int year) {
      this.year = year;
      return self();
    }

    public DateStepBuilder setMonth(int month) {
      this.month = month;
      return self();
    }

    public DateStepBuilder setDay(int day) {
      this.day = day;
      return self();
    }

    public DateStepBuilder setDateChecker(DateStepChecker dateChecker) {
      this.dateChecker = dateChecker;
      return self();
    }

    public DateStep createStep() {
      checker = staticChecker;
      if (dateChecker == null) {
        dateChecker = new DateStepChecker() {
          @Override
          public boolean check(int year, int month, int day) {
            return true;
          }
        };
      }
      return new DateStep(this);
    }
  }

  public interface DateStepChecker {
    boolean check(int year, int month, int day);
  }
}
