package com.esh1n.utils_android.widget;


import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.esh1n.utils_android.DateBuilder;
import com.esh1n.utils_android.R;
import com.esh1n.utils_android.ui.SnackbarBuilder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class CalendarInputDateFilterView extends BaseFromToDateFilterView {
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("d-MMM-yyyy HH:mm", Locale.UK);
    private static final int DEFAULT_FILTER_ENABLED = 1;
    private static final int DEFAULT_FILTER_DISABLED = 2;

    private TextView mToDateView;
    private TextView mFromDateView;
    private View clearFilterButton;
    private Calendar toCalendar;
    private Calendar fromCalendar;
    private DatePickerDialog toDatePickerDialog;
    private DatePickerDialog fromDatePickerDialog;
    private TimePickerDialog toTimePickerDialog;
    private TimePickerDialog fromTimePickerDialog;


    private SavedState savedState;
    private boolean isFilterCleared;

    public CalendarInputDateFilterView(Context context, boolean needToSetDefaultFilter) {
        super(context);
        isFilterCleared = !needToSetDefaultFilter;
        if (needToSetDefaultFilter) {
            setDefaultFilter();
        }
    }

    public CalendarInputDateFilterView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CalendarInputDateFilterView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void init(Context context, AttributeSet attrs) {
        View filterView = LayoutInflater.from(context).inflate(R.layout.view_date_filter, this, false);

        toCalendar = Calendar.getInstance();
        fromCalendar = Calendar.getInstance();
        mFromDateView = filterView.findViewById(R.id.date_picker_from_date);
        mToDateView = filterView.findViewById(R.id.date_picker_to_date);

        prepareFromDateView(context, fromCalendar);
        prepareToDateView(context, toCalendar);
        prepareClearButton(filterView);

        if (attrs != null) {
            TypedArray attributes = context.getTheme().obtainStyledAttributes(
                    attrs,
                    R.styleable.CalendarInputDateFilterView, 0, 0);
            boolean needToSetDefaultFilter = attributes
                    .getBoolean(R.styleable.CalendarInputDateFilterView_needDefaultFilter, true);
            if (needToSetDefaultFilter) {
                setDefaultFilter();
            }
        }
        addView(filterView);
    }

    private void setDefaultFilter() {
        clearFilterButton.setVisibility(VISIBLE);
        initDateInterval(getMaxDate(1), mCurrentToDate, toCalendar, mToDateView, toDatePickerDialog, toTimePickerDialog);
        initDateInterval(new Date(System.currentTimeMillis()), mCurrentFromDate, fromCalendar, mFromDateView, fromDatePickerDialog, fromTimePickerDialog);
    }

    private void initDateInterval(Date initialDateToSet, Date dateVar,
                                  Calendar calendarVar, TextView dateView,
                                  DatePickerDialog datePickerDialog, TimePickerDialog timePickerDialog) {
        dateVar = initialDateToSet;
        calendarVar.setTime(dateVar);
        updateDateView(dateVar, dateView);
        datePickerDialog.updateDate(
                calendarVar.get(Calendar.YEAR),
                calendarVar.get(Calendar.MONTH),
                calendarVar.get(Calendar.DAY_OF_MONTH));
        timePickerDialog.updateTime(calendarVar.get(Calendar.HOUR_OF_DAY), calendarVar.get(Calendar.MINUTE));
    }

    private void prepareClearButton(View filterView) {
        clearFilterButton = filterView.findViewById(R.id.tv_clear_date_filters);
        clearFilterButton.setOnClickListener(v -> {
            mFromDateView.setText("");
            mToDateView.setText("");
            isFilterCleared = true;
            mCurrentFromDate = null;
            mCurrentToDate = null;
            clearFilterButton.setVisibility(GONE);
            if (mDateFilterListener != null) {
                mDateFilterListener.onDateFilterChanged(null, null);
            }
        });
    }

    private void prepareToDateView(Context context, Calendar toCalendar) {
        boolean isFrom = false;
        toTimePickerDialog = createTimePickerDialog(context, toCalendar, (view, hour, minute) -> {
            selectTime(mToDateView, hour, minute, isFrom);
        });
        toDatePickerDialog = createDatePickerDialog(context, toCalendar,
                (view, year, month, dayOfMonth) -> {
                    selectDate(mToDateView, year, month, dayOfMonth, isFrom, () -> toTimePickerDialog.show());
                });

        mToDateView.setOnClickListener(v -> toDatePickerDialog.show());
    }

    private void prepareFromDateView(Context context, Calendar fromCalendar) {
        boolean isFrom = true;
        fromTimePickerDialog = createTimePickerDialog(context, fromCalendar, (view, hour, minute) -> {
            selectTime(mFromDateView, hour, minute, isFrom);
        });
        fromDatePickerDialog = createDatePickerDialog(context, fromCalendar,
                (view, year, month, dayOfMonth) -> {
                    selectDate(mFromDateView, year, month, dayOfMonth, isFrom, () -> fromTimePickerDialog.show());
                });
        mFromDateView.setOnClickListener(v -> fromDatePickerDialog.show());
    }

    private void updateDateView(Date date, TextView textView) {
        if (date != null) {
            textView.setText(DATE_FORMAT.format(date));
        }
    }

    private void selectDate(TextView dateView, int year, int month, int dayOfMonth, boolean isFromDate, TimeSetAction action) {
        Date date = getSelectedDate(year, month, dayOfMonth, isFromDate);
        boolean isSuccessful = setNewDate(dateView, date, isFromDate);
        if (isSuccessful) {
            action.setTime();
        }
    }

    interface TimeSetAction {
        void setTime();
    }

    private void selectTime(TextView dateView, int hour, int minute, boolean isFrom) {
        Date date = getDateWithTime(hour, minute, isFrom);
        setNewDate(dateView, date, isFrom);
    }

    private Date getDateWithTime(int hourOfDay, int minute, boolean isFrom) {
        Date date = isFrom ? currentFromDate() : currentToDate();
        return new DateBuilder(date)
                .withHourOfDay(hourOfDay)
                .withMinute(minute)
                .build();
    }

    private boolean setNewDate(TextView dateView, Date newDateTime, boolean isFrom) {
        if (isFrom) {
            mCurrentFromDate = newDateTime;
            if (mCurrentToDate != null && mCurrentFromDate.after(mCurrentToDate)) {
                SnackbarBuilder.INSTANCE.buildErrorSnack(dateView, "'From' date should be before 'To' date")
                        .show();
                mCurrentFromDate = null;
                return false;
            }
        } else {
            mCurrentToDate = newDateTime;
            if (mCurrentFromDate != null && mCurrentToDate.before(mCurrentFromDate)) {
                SnackbarBuilder.INSTANCE.buildErrorSnack(dateView, "'To' date should be after 'From' date")
                        .show();
                mCurrentToDate = null;
                return false;
            }
        }

        controlClearButtonVisibility();
        dateView.setText(DATE_FORMAT.format(newDateTime));
        notifyObserverDateChanged();
        return true;
    }


    private Date getSelectedDate(int year, int month, int dayOfMonth, boolean isFromDate) {
        DateBuilder dateBuilder = new DateBuilder()
                .withYear(year)
                .withMonth(month)
                .withDayInMonth(dayOfMonth);
        if (isFromDate) {
            dateBuilder.withMorning();
        } else {
            dateBuilder.withMidnight();
        }
        return dateBuilder.build();
    }

    private void controlClearButtonVisibility() {
        if (mCurrentFromDate != null & mCurrentToDate != null) {
            clearFilterButton.setVisibility(VISIBLE);
        } else {
            clearFilterButton.setVisibility(GONE);
        }
    }


    @NonNull
    private DatePickerDialog createDatePickerDialog(Context context, Calendar calendar, OnDateSetListener dateSetListener) {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(context, dateSetListener, year, month, dayOfMonth);
        datePickerDialog.getDatePicker().setMaxDate(getMaxDate(1).getTime());
        return datePickerDialog;
    }

    @NonNull
    private TimePickerDialog createTimePickerDialog(Context context, Calendar calendar, TimePickerDialog.OnTimeSetListener dateSetListener) {
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        return new TimePickerDialog(context, dateSetListener, hourOfDay, minute, true);
    }

    @Override
    public Date currentFromDate() {
        String dateTextValue = mFromDateView.getText().toString();
        if (mCurrentFromDate == null && !dateTextValue.isEmpty()) {
            try {
                mCurrentFromDate = DATE_FORMAT.parse(dateTextValue);
            } catch (ParseException e) {
                Log.e(CalendarInputDateFilterView.class.getSimpleName(), "Failed to parse from date:" + dateTextValue, e);
            }
        }
        return mCurrentFromDate;
    }

    @Override
    public Date currentToDate() {
        String dateTextValue = mToDateView.getText().toString();
        if (mCurrentToDate == null && !dateTextValue.isEmpty()) {
            try {
                mCurrentToDate = DATE_FORMAT.parse(dateTextValue);
            } catch (ParseException e) {
                Log.e(CalendarInputDateFilterView.class.getSimpleName(), "Failed to parse to date:" + dateTextValue, e);
            }
        }
        return mCurrentToDate;
    }

    @Override
    public void refreshByMinMaxDates(Date minDate, Date maxDate) {
    }

    @Nullable
    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        savedState = new SavedState(superState);
        savedState.defaultFilter = isFilterCleared ? DEFAULT_FILTER_DISABLED : DEFAULT_FILTER_ENABLED;
        return savedState;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        savedState = (SavedState) state;
        if (savedState.defaultFilter == DEFAULT_FILTER_DISABLED) {
            clearFilterButton.setVisibility(GONE);
        } else {
            clearFilterButton.setVisibility(VISIBLE);
        }
        super.onRestoreInstanceState(savedState.getSuperState());
    }

    private static class SavedState extends BaseSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR
                = new Parcelable.Creator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
        int defaultFilter;

        SavedState(Parcelable superState) {
            super(superState);
        }

        SavedState(Parcel source) {
            super(source);
            defaultFilter = source.readInt();
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeSerializable(defaultFilter);
        }
    }

    private Date getMaxDate(int maxFutureMonth) {
        Date date = new Date(System.currentTimeMillis());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, 1);
        return calendar.getTime();
    }

}