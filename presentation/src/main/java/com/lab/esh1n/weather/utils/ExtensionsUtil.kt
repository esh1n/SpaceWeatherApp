package com.lab.esh1n.weather.utils

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

fun ImageView.loadCircleImage(url: String?) {
    if (url.isNullOrBlank()) return

    Glide.with(this)
            .load(url)
            .apply(RequestOptions.circleCropTransform())
            .into(this)
}

fun <T> Observable<T>.applyAndroidSchedulers(): Observable<T> {
    return this
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
}

fun <T> Single<T>.applyAndroidSchedulers(): Single<T> {
    return this
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
}

fun <T> Flowable<T>.applyAndroidSchedulers(): Flowable<T> {
    return this
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
}