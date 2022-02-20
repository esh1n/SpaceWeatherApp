@file:Suppress("TooManyFunctions")

package com.esh1n.core_android.common


import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.zipWith
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

fun <T> Single<T>.observeOnMain(): Single<T> = observeOn(AndroidSchedulers.mainThread())

fun <T> Flowable<T>.observeOnMain(): Flowable<T> = observeOn(AndroidSchedulers.mainThread())

fun <T> Maybe<T>.observeOnMain(): Maybe<T> = observeOn(AndroidSchedulers.mainThread())

fun <T> Observable<T>.observeOnMain(): Observable<T> = observeOn(AndroidSchedulers.mainThread())

fun Completable.observeOnMain(): Completable = observeOn(AndroidSchedulers.mainThread())

fun <T> Observable<T>.observeIO(): Observable<T> = observeOn(Schedulers.io())

fun <T> Single<T>.observeIO(): Single<T> = observeOn(Schedulers.io())

fun <T> Maybe<T>.observeIO(): Maybe<T> = observeOn(Schedulers.io())

fun Completable.observeIO(): Completable = observeOn(Schedulers.io())

fun <T> Observable<T>.subscribeOnMain(): Observable<T> = subscribeOn(AndroidSchedulers.mainThread())

fun <T> Single<T>.subscribeOnMain(): Single<T> = subscribeOn(AndroidSchedulers.mainThread())

fun <T> Maybe<T>.subscribeOnMain(): Maybe<T> = subscribeOn(AndroidSchedulers.mainThread())

fun <T> Observable<T>.subscribeIO(): Observable<T> = subscribeOn(Schedulers.io())

fun <T> Single<T>.subscribeIO(): Single<T> = subscribeOn(Schedulers.io())

fun <T> Flowable<T>.subscribeIO(): Flowable<T> = subscribeOn(Schedulers.io())

fun <T> Maybe<T>.subscribeIO(): Maybe<T> = subscribeOn(Schedulers.io())

fun Completable.subscribeIO(): Completable = subscribeOn(Schedulers.io())

fun Completable.subscribeOnMain(): Completable = subscribeOn(AndroidSchedulers.mainThread())

fun <T> Observable<T>.async(): Observable<T> = subscribeIO().observeOnMain()

fun <T> Single<T>.async(): Single<T> = subscribeIO().observeOnMain()

fun <T> Maybe<T>.async(): Maybe<T> = subscribeIO().observeOnMain()

fun <T> Flowable<T>.async(): Flowable<T> = subscribeIO().observeOnMain()

fun Completable.async(): Completable = subscribeIO().observeOnMain()

fun <T> Observable<T>.subscribeOnError(
    onNext: (T) -> Unit = {},
    onError: (Error) -> Unit = {}
): Disposable = subscribe(onNext, onException(onError))

fun <T> Single<T>.subscribeOnError(
    onSuccess: (T) -> Unit = {},
    onError: (Error) -> Unit = {}
): Disposable = subscribe(onSuccess, onException(onError))

fun Completable.subscribeOnError(
    onComplete: () -> Unit = {},
    onError: (Error) -> Unit = {}
): Disposable = subscribe(onComplete, onException(onError))

fun <T> Maybe<T>.subscribeOnError(
    onSuccess: (T) -> Unit = {},
    onError: (Error) -> Unit = {}
): Disposable = subscribe(onSuccess, onException(onError))

fun <T> Observable<Result<T>>.subscribeOnResult(
    onNext: (Result<T>) -> Unit = {},
    onError: (Error) -> Unit = {}
): Disposable = subscribe({
    onNext(it)
    it.onFailure(onError)
}, onException(onError))

fun <T> Single<Result<T>>.subscribeOnResult(
    onSuccess: (Result<T>) -> Unit = {},
    onError: (Error) -> Unit = {}
): Disposable = subscribe({
    onSuccess(it)
    it.onFailure(onError)
}, onException(onError))

fun <T> Maybe<Result<T>>.subscribeOnResult(
    onSuccess: (Result<T>) -> Unit = {},
    onError: (Error) -> Unit = {}
): Disposable = subscribe({
    onSuccess(it)
    it.onFailure(onError)
}, onException(onError))

fun <T> Observable<Result<T>>.subscribeOnResultData(
    onNext: (T) -> Unit,
    onError: (Error) -> Unit
): Disposable = subscribe({ it.onSuccess(onNext).onFailure(onError) }, onException(onError))

fun <T> Single<Result<T>>.subscribeOnResultData(
    onSuccess: (T) -> Unit,
    onError: (Error) -> Unit
): Disposable = subscribe({ it.onSuccess(onSuccess).onFailure(onError) }, onException(onError))

fun <T> Maybe<Result<T>>.subscribeOnResultData(
    onSuccess: (T) -> Unit,
    onError: (Error) -> Unit
): Disposable = subscribe({ it.onSuccess(onSuccess).onFailure(onError) }, onException(onError))

private fun onException(onError: (Error) -> Unit): (Throwable) -> Unit = { onError(it.castOrMap()) }

private fun Throwable.castOrMap(): Error =
    if (this is Error) {
        this
    } else {
        GeneralError(this)
    }

fun <T : Any> Maybe<T>.catchError(): Maybe<Result<T>> =
    map<Result<T>> { Success(it) }.onErrorReturn { Failure(GeneralError(it)) }

fun <T : Any> Single<T>.catchError(): Single<Result<T>> =
    map<Result<T>> { Success(it) }.onErrorReturn { it.castOrMap().run(::Failure) }

fun <T : Any> Observable<T>.catchError(): Observable<Result<T>> =
    map<Result<T>> { Success(it) }.onErrorReturn { it.castOrMap().run(::Failure) }


fun <T> Single<T>.repeatOnPredicate(
    start: Int,
    attempts: Int,
    delay: Long,
    predicate: (T) -> Boolean
): Single<T> =
    repeatWhen {
        it.zipWith(other = Flowable.range(start, attempts)) { _, attempt -> attempt }
            .flatMap { repeatAttempt ->
                Flowable.timer(repeatAttempt * delay, TimeUnit.SECONDS)
            }
    }.takeUntil(predicate)
        .filter(predicate)
        .firstOrError()