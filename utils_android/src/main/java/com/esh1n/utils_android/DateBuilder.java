package com.esh1n.utils_android;

import java.util.Calendar;
import java.util.Date;

public class DateBuilder {
    private Calendar calendar;

    public DateBuilder(Date initDate) {
        calendar = Calendar.getInstance();
        calendar.setTime(initDate);
    }

    public DateBuilder() {
        calendar = Calendar.getInstance();
    }

    public DateBuilder(int year,int month,int dayOfMonth) {
        calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR,year);
        calendar.set(Calendar.MONTH,month);
        calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
    }

    public Date build() {
        return calendar.getTime();
    }

    public DateBuilder withDayInMonth(int dayInMonth) {
        calendar.set(Calendar.DAY_OF_MONTH, dayInMonth);
        return this;
    }

    public DateBuilder withMorning() {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return this;
    }

    public DateBuilder withHourOfDay(int hourOfDay) {
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.SECOND, 0);
        return this;
    }

    public DateBuilder withMinute(int minute) {
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        return this;
    }

    public DateBuilder withMidnight() {
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        return this;
    }

    public DateBuilder withMonth(int month) {
        calendar.set(Calendar.MONTH, month);
        return this;
    }

    public DateBuilder withFirstMonth() {
        calendar.set(Calendar.MONTH, 0);
        return this;
    }

    public DateBuilder withYear(int year) {
        calendar.set(Calendar.YEAR, year);
        return this;
    }

    public DateBuilder withLastMonth() {
        calendar.set(Calendar.MONTH, calendar.getActualMaximum(Calendar.MONTH));
        return this;
    }

    public DateBuilder withLastDayInMonth() {
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        return this;
    }

    public DateBuilder withFirstDayInMonth() {
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return this;
    }

    public DateBuilder nextYear() {
        calendar.add(Calendar.YEAR, 1);
        return this;
    }

    public DateBuilder plusMinute() {
        calendar.add(Calendar.MINUTE, 1);
        return this;
    }

    public DateBuilder previousDay() {
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        return this;
    }

    public DateBuilder previousMonth() {
        calendar.add(Calendar.MONTH, -1);
        return this;
    }

    public DateBuilder nextDay() {
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        return this;
    }

    public DateBuilder nextMonth() {
        calendar.add(Calendar.MONTH, 1);
        return this;
    }
}