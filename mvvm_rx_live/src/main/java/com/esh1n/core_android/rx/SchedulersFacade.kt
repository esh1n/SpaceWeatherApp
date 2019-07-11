package com.esh1n.core_android.rx

import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * Provides various threading schedulers.
 */

class SchedulersFacade @Inject
constructor() {
    companion object {


        fun <T> applySchedulersObservable(): ObservableTransformer<T, T> {
            return applySchedulersObservable(false)
        }

        fun <T> applySchedulersObservable(delayError: Boolean): ObservableTransformer<T, T> {
            return ObservableTransformer { upstream ->
                upstream.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread(), delayError)
            }
        }

        fun <T> applySchedulersMaybe(): MaybeTransformer<T, T> {
            return MaybeTransformer { upstream ->
                upstream.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
            }
        }

        fun <T> applySchedulersSingle(): SingleTransformer<T, T> {
            return SingleTransformer { upstream ->
                upstream.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
            }
        }

        fun applySchedulersCompletable(): CompletableTransformer {
            return CompletableTransformer { upstream ->
                upstream.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
            }
        }
    }
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
