package com.primeholding.primesampleapp

import com.primeholding.primesampleapp.viewmodel.CounterViewModel
import io.reactivex.observers.TestObserver
import org.junit.Rule
import org.junit.Test
import org.mockito.junit.MockitoJUnit
import java.util.concurrent.TimeUnit

class CounterViewModelTest {


    @Rule
    @JvmField
    var rule = MockitoJUnit.rule()!!

    @Rule
    @JvmField
    val testSchedulerRule = TestSchedulerRule()


    @Test
    fun testIncrement() {
        //region Arrange
        val counterViewModel = CounterViewModel()
        val testObserver = TestObserver<String>()
        //endregion
        //region Execute
        counterViewModel.output.currentCount.subscribe(testObserver)
        counterViewModel.input.increment()
        testSchedulerRule.thread.advanceTimeBy(5000, TimeUnit.MILLISECONDS)
        //endregion
        //region Assert
        //initial state + the incremented
        testObserver.assertValueCount(2)
        testObserver.assertValueAt(0, 0.toString())
        testObserver.assertValueAt(1, 1.toString())
        //endregion
    }

    @Test
    fun testDecrement() {
        //region Arrange
        val counterViewModel = CounterViewModel()
        val testObserver = TestObserver<String>()
        //endregion
        //region Execute
        counterViewModel.output.currentCount.subscribe(testObserver)
        counterViewModel.input.decrement()
        testSchedulerRule.thread.advanceTimeBy(5000, TimeUnit.MILLISECONDS)
        //endregion
        //region Assert
        //initial state + the incremented
        testObserver.assertValueCount(2)
        testObserver.assertValueAt(0, 0.toString())
        testObserver.assertValueAt(1, (-1).toString())
        //endregion
    }
}