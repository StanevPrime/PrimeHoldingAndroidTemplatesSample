package com.primeholding.primesampleapp.view.base

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import com.primeholding.primesampleapp.model.ApiError
import io.reactivex.disposables.CompositeDisposable


@SuppressLint("Registered")
abstract class BaseActivity : AppCompatActivity() {

    val compositeDisposable = CompositeDisposable()

    override fun onStart() {
        super.onStart()
        bind()
    }

    override fun onStop() {
        super.onStop()
        compositeDisposable.clear()
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }

    /**
     * Set up your bindings in the child activity
     */
    abstract fun bind()


    open fun renderLoadingState(isLoading: Boolean) {
        //TODO set up your way of showing loading either here or in child activities
    }

    open fun renderErrorState(error: ApiError) {
        //TODO set up your way of showing errors either here or in child activities
    }

}