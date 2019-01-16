package com.primeholding.primesampleapp

import com.primeholding.primesampleapp.viewmodel.loading.LoadingViewModel
import com.tngtech.java.junit.dataprovider.DataProvider
import com.tngtech.java.junit.dataprovider.DataProviderRunner
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import io.reactivex.observers.TestObserver
import junit.framework.TestCase
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnit
import java.util.concurrent.TimeUnit

@RunWith(DataProviderRunner::class)
class LoadingViewModelTest : TestCase() {
    @Rule
    @JvmField
    var rule = MockitoJUnit.rule()!!

    @Rule
    @JvmField
    val testSchedulerRule = TestSchedulerRule()


    /**
     * Simplest case where there is only one loading observable that starts and finishes loading
     * Input
     * |--true--false----->
     *
     * Expected:
     * |-false--true--false----->
     */
    @Test
    fun testSingleObservable() {
        //region Arrange
        val loadingViewModel = LoadingViewModel()
        val testObserver = TestObserver<Boolean>()

        val coldObs =
            Observable.just(true, false)
                .zipWith(
                    Observable.interval(100, TimeUnit.MILLISECONDS),
                    BiFunction { item: Boolean, _: Long -> item })
        val hotObs = coldObs.publish()
        loadingViewModel.input.addLoadingObservable(hotObs)
        hotObs.subscribe()
        hotObs.connect()
        //endregion
        //region Execute
        loadingViewModel.output.isLoading.subscribe(testObserver)
        //advanced time due to the loading view model delay
        testSchedulerRule.thread.advanceTimeBy(10000, TimeUnit.MILLISECONDS)
        //endregion
        //region Assert
        //initial state
        testObserver.assertValueAt(0, false)
        //started loading
        testObserver.assertValueAt(1, true)
        //finished loading
        testObserver.assertValueAt(2, false)
        //endregion
    }


    /**
     * Two requests start very close to each other
     * Input:
     * |--true--false----->
     * |---true---false----->
     *
     * Expected:
     * * |-false--true---false----->
     */
    @Test
    fun testTwoAlmostSimultaneous() {
        //region Arrange
        val loadingViewModel = LoadingViewModel()
        val testObserver = TestObserver<Boolean>()

        val coldObs1 =
            Observable.just(true, false)
                .zipWith(
                    Observable.interval(100, TimeUnit.MILLISECONDS),
                    BiFunction { item: Boolean, interval: Long -> item })
        val coldObs2 =
            Observable.just(true, false)
                .zipWith(
                    Observable.interval(150, TimeUnit.MILLISECONDS),
                    BiFunction { item: Boolean, interval: Long -> item })


        val hotObs1 = coldObs1.publish()
        loadingViewModel.input.addLoadingObservable(hotObs1)
        hotObs1.subscribe()
        hotObs1.connect()
        val hotObs2 = coldObs2.publish()
        loadingViewModel.input.addLoadingObservable(hotObs2)
        hotObs2.subscribe()
        hotObs2.connect()
        //endregion
        //region Execute
        loadingViewModel.output.isLoading.subscribe(testObserver)
        //endregion
        //region Assert
        //advanced time due to the loading view model delay
        testSchedulerRule.thread.advanceTimeBy(5000, TimeUnit.MILLISECONDS)
        //initial state
        testObserver.assertValueAt(0, false)
        //started loading
        testObserver.assertValueAt(1, true)
        //finished loading
        testObserver.assertValueAt(2, false)
        //endregion
    }

    /**
     * |----------true----------false->
     * |-----------true----------false->
     * |-------------------------true-------------------------false->
     * |-------------------true--------------------false->
     * |--------------------true--------------------false->
     * |----------------------------true----------------------------false->
     *
     * Expected:
     * |-false-----true---------------------------------------------false->
     */
    @Test
    fun testMultiple() {
        //region Arrange
        val loadingViewModel = LoadingViewModel()
        val testObserver = TestObserver<Boolean>()

        val coldObs1 =
            Observable.just(true, false)
                .zipWith(
                    Observable.interval(100, TimeUnit.MILLISECONDS),
                    BiFunction { item: Boolean, interval: Long -> item })
        val coldObs2 =
            Observable.just(true, false)
                .zipWith(
                    Observable.interval(110, TimeUnit.MILLISECONDS),
                    BiFunction { item: Boolean, interval: Long -> item })

        val coldObs3 =
            Observable.just(true, false)
                .zipWith(
                    Observable.interval(210, TimeUnit.MILLISECONDS),
                    BiFunction { item: Boolean, _: Long -> item })

        val coldObs4 =
            Observable.just(true, false)
                .zipWith(
                    Observable.interval(205, TimeUnit.MILLISECONDS),
                    BiFunction { item: Boolean, _: Long -> item })
        val coldObs5 =
            Observable.just(true, false)
                .zipWith(
                    Observable.interval(206, TimeUnit.MILLISECONDS),
                    BiFunction { item: Boolean, _: Long -> item })
        val coldObs6 =
            Observable.just(true, false)
                .zipWith(
                    Observable.interval(280, TimeUnit.MILLISECONDS),
                    BiFunction { item: Boolean, _: Long -> item })

        val hotObs1 = coldObs1.publish()
        loadingViewModel.input.addLoadingObservable(hotObs1)
        hotObs1.subscribe()
        hotObs1.connect()

        val hotObs2 = coldObs2.publish()
        loadingViewModel.input.addLoadingObservable(hotObs2)
        hotObs2.subscribe()
        hotObs2.connect()

        val hotObs3 = coldObs3.publish()
        loadingViewModel.input.addLoadingObservable(hotObs3)
        hotObs3.subscribe()
        hotObs3.connect()

        val hotObs4 = coldObs4.publish()
        loadingViewModel.input.addLoadingObservable(hotObs4)
        hotObs4.subscribe()
        hotObs4.connect()

        val hotObs5 = coldObs5.publish()
        loadingViewModel.input.addLoadingObservable(hotObs5)
        hotObs5.subscribe()
        hotObs5.connect()

        val hotObs6 = coldObs6.publish()
        loadingViewModel.input.addLoadingObservable(hotObs6)
        hotObs6.subscribe()
        hotObs6.connect()
        //endregion
        //region Execute
        loadingViewModel.output.isLoading.subscribe(testObserver)
        //endregion
        //region Assert
        //advanced time due to the loading view model delay
        testSchedulerRule.thread.advanceTimeBy(10000, TimeUnit.MILLISECONDS)
        //initial state
        testObserver.assertValueAt(0, false)
        //started loading
        testObserver.assertValueAt(1, true)
        //finished loading
        testObserver.assertValueAt(2, false)
        //endregion
    }

    /**
     * Input
     * |----------true----------false-->
     * |------------------------true----------false-->
     * Expected:
     * |-false-----true------------------------false-->
     */
    @Test
    fun testConsecutive() {
        //region Arrange
        val loadingViewModel = LoadingViewModel()
        val testObserver = TestObserver<Boolean>()

        val coldObs1 =
            Observable.just(true, false)
                .zipWith(
                    Observable.interval(100, TimeUnit.MILLISECONDS),
                    BiFunction { item: Boolean, interval: Long -> item })
        val coldObs2 =
            Observable.just(true, false)
                .zipWith(
                    Observable.interval(200, TimeUnit.MILLISECONDS),
                    BiFunction { item: Boolean, interval: Long -> item })


        val hotObs1 = coldObs1.publish()
        loadingViewModel.input.addLoadingObservable(hotObs1)
        hotObs1.subscribe()
        hotObs1.connect()
        val hotObs2 = coldObs2.publish()
        loadingViewModel.input.addLoadingObservable(hotObs2)
        hotObs2.subscribe()
        hotObs2.connect()
        //endregion
        //region Execute
        loadingViewModel.output.isLoading.subscribe(testObserver)
        //endregion
        //region Assert
        //advanced time due to the loading view model delay
        testSchedulerRule.thread.advanceTimeBy(5000, TimeUnit.MILLISECONDS)
        //initial state
        testObserver.assertValueAt(0, false)
        //started loading
        testObserver.assertValueAt(1, true)
        //finished loading
        testObserver.assertValueAt(2, false)
        testObserver.assertValueCount(3)
        //endregion
    }


    companion object {
        @DataProvider
        @JvmStatic
        fun loadingObservables(): Array<Boolean> {
            val x = arrayOf(true, false)
            return x;
        }
    }

}