package com.esh1n.utils_android.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import java.util.Date;

public abstract class BaseFromToDateFilterView extends FrameLayout implements FromToDateObservable {
    protected FromToDateFilterObserver mDateFilterListener;
    protected Date mCurrentFromDate;
    protected Date mCurrentToDate;

    public BaseFromToDateFilterView(@NonNull Context context) {
        super(context);
        init(context, null);
    }

    public BaseFromToDateFilterView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public BaseFromToDateFilterView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    protected abstract void init(Context context, AttributeSet attributeSet);

    @Override
    public Date currentFromDate() {
        return mCurrentFromDate;
    }

    @Override
    public Date currentToDate() {
        return mCurrentToDate;
    }

    @Override
    public void registerDateChangesObserver(FromToDateFilterObserver observer) {
        mDateFilterListener = observer;
    }

    @Override
    public void removeObserver() {
        mDateFilterListener = null;
    }

    @Override
    public void notifyObserverDateChanged() {
        if (mDateFilterListener != null && mCurrentFromDate != null && mCurrentToDate != null) {
            mDateFilterListener.onDateFilterChanged(mCurrentFromDate, mCurrentToDate);
        }
    }
}