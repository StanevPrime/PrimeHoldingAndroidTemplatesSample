package com.primeholding.primesampleapp.misc.extension

import android.util.Log
import com.primeholding.primesampleapp.model.api.ApiResult
import com.primeholding.primesampleapp.model.error.IError
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

// region General
/**
 * Add all [Disposable] from a [List] to a [CompositeDisposable]
 */
fun List<Disposable>.addTo(compositeDisposable: CompositeDisposable) {
    this.forEach { compositeDisposable.add(it) }
}

/**
 * Used for debugging
 */
fun <R> Observable<R>.debug(tag: String): Observable<R> {

    return doOnSubscribe {
        Log.d(tag, "Subscribed")
    }
        .doOnNext {
            Log.d(tag, "On Next")
        }
        .doOnComplete {
            Log.d(tag, "Completed")
        }
        .doOnError {
            Log.d(tag, "Error")
        }
        .doOnDispose {
            Log.d(tag, "Disposed")
        }
}

/**
 * Used for checking the thread
 */
fun <R> Observable<R>.debugThread(tag: String): Observable<R> {

    return doOnSubscribe {
        Log.d(tag, "OnSubscribe, current thread: " + Thread.currentThread())
    }
        .doOnNext {
            Log.d(tag, "OnNext, current thread: " + Thread.currentThread())
        }
        .doOnComplete {
            Log.d(tag, "OnComplete, current thread: " + Thread.currentThread())
        }
        .doOnError {
            Log.d(tag, "OnError, current thread: " + Thread.currentThread())
        }
        .doOnDispose {
            Log.d(tag, "OnDispose, current thread: " + Thread.currentThread())
        }
}

//endregion
//region Filtering Observables
/**
 * Filter when an [ApiResult] is [ApiResult.Success]
 */
fun <T> Observable<ApiResult<T>>.whenSuccess(): Observable<T> =
    filter { it -> it is ApiResult.Success }
        .map { it -> it as ApiResult.Success<T> }
        .map { it -> it.data }

/**
 * Filter when an [ApiResult] is [ApiResult.Error]
 */
fun <T> Observable<ApiResult<T>>.whenError(): Observable<IError> =
    filter { it -> it is ApiResult.Error }
        .map { it -> it as ApiResult.Error<T> }
        .map { it -> it.error }

/**
 * Filter when an [ApiResult] is [ApiResult.Loading]
 */
fun <T> Observable<ApiResult<T>>.whenLoading(): Observable<Boolean> =
    map { it -> it is ApiResult.Loading<T> }

//endregion


