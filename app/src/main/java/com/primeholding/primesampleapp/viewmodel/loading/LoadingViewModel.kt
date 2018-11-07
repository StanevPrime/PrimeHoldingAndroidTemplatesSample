package com.primeholding.primesampleapp.viewmodel.loading


import androidx.lifecycle.ViewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit
import javax.inject.Inject


//region View Model Interfaces
interface LoadingInput {
    /**
     * Adds a new loading observable
     */
    fun addLoadingObservable(observable: Observable<Boolean>)
}

interface LoadingOutput {
    /**
     * Observable whether the application is loading
     */
    val isLoading: Observable<Boolean>

}

interface LoadingType {
    val input: LoadingInput
    val output: LoadingOutput

}
//endregion

class LoadingViewModel @Inject constructor() : ViewModel(), LoadingInput, LoadingOutput, LoadingType {

    //region Input Output
    override val input = this
    override val output = this
    //endregion

    //region Subjects consuming from Input
    private val newLoadingObservable = PublishSubject.create<Observable<Boolean>>()
//endregion

    //region Output
    override val isLoading: Observable<Boolean>
    //endregion

    //region Locally used
    private val loadingCount = BehaviorSubject.createDefault(0)
    private val addedObservables = BehaviorSubject.createDefault<MutableList<Observable<Boolean>>>(mutableListOf())
    private val isLoadingObservable: Observable<Boolean>
    private val compositeDisposable = CompositeDisposable()
    //endregion

    init {

        newLoadingObservable
            .subscribe {
                val observables = addedObservables.value!!
                observables.add(it)
                addedObservables.onNext(observables)
            }.addTo(compositeDisposable)

        isLoadingObservable =
                addedObservables
                    .flatMap { it ->
                        return@flatMap Observable.merge(it)
                    }

        isLoadingObservable
            .delay(100, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
            .bind(loadingCount)
            .addTo(compositeDisposable)

        isLoading = loadingCount.map { it > 0 }

    }


    //region Input methods
    override fun addLoadingObservable(observable: Observable<Boolean>) {
        newLoadingObservable.onNext(observable)
    }
    // endregion


    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}

private fun Observable<Boolean>.bind(count: BehaviorSubject<Int>): Disposable =
    map { it ->
        val increment = if (it) {
            1
        } else {
            -1
        }
        return@map if (count.value != null) {
            count.value!!.plus(increment)
        } else {
            0
        }
    }.subscribe(count::onNext)