package com.primeholding.primesampleapp.viewmodel

import androidx.lifecycle.ViewModel
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

//region View Model Interfaces
interface CounterInput {
    /**
     * Instruct the View Model to increment the counter
     */
    fun increment()

    /**
     * Instruct the View Model to decrement the counter
     */
    fun decrement()
}

interface CounterOutput {
    /**
     * Observable with the current count
     */
    val currentCount: Observable<String>
}

interface CounterType {
    val input: CounterInput
    val output: CounterOutput
}

//endregion
class CounterViewModel @Inject constructor() : ViewModel(), CounterInput, CounterOutput, CounterType {

//region Input Output

    override val input = this
    override val output = this

//endregion

    //region Subjects consuming from Input
    private val incrementSubject = PublishSubject.create<Unit>()
    private val decrementSubject = PublishSubject.create<Unit>()
    //endregion
    //region Output

    override val currentCount: Observable<String>

    //endregion

    //region Locally used

    private val currentCountSubject = BehaviorSubject.createDefault(0)
    private val compositeDisposable = CompositeDisposable()

//endregion

    init {
        currentCount = currentCountSubject.format()
        Observable
            .merge(
                incrementSubject.increment(currentCountSubject),
                decrementSubject.decrement(currentCountSubject)
            )
            .subscribe(currentCountSubject::onNext)
            .addTo(compositeDisposable)

    }


    //region Input methods
    override fun increment() {
        incrementSubject.onNext(Unit)
    }

    override fun decrement() {
        decrementSubject.onNext(Unit)
    }


// endregion


    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}

private fun <T> BehaviorSubject<T>.format(): Observable<String> = map { it.toString() }
/**
 * Increments the value of a BehaviorSubject
 */
private fun PublishSubject<Unit>.increment(value: BehaviorSubject<Int>) =
    map {
        return@map if (value.value != null) {
            value.value!!.plus(1)
        } else {
            0
        }
    }

/**
 * Decrements the value of a BehaviorSubject
 */
private fun PublishSubject<Unit>.decrement(value: BehaviorSubject<Int>) =
    map {
        return@map if (value.value != null) {
            value.value!!.minus(1)
        } else {
            0
        }
    }


//Sample extension returning an Observable
//private fun CounterType.getName(): Observable<String> {
//    return Observable.just(this.javaClass.simpleName)
//            .map { it -> it.toUpperCase() }
//}
