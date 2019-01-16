package com.primeholding.primesampleapp.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.primeholding.primesampleapp.R
import com.primeholding.primesampleapp.application.BaseApplication
import com.primeholding.primesampleapp.databinding.ActivityCounterBinding
import com.primeholding.primesampleapp.misc.extension.addTo
import com.primeholding.primesampleapp.misc.extension.rxClick
import com.primeholding.primesampleapp.view.base.BaseActivity
import com.primeholding.primesampleapp.viewmodel.CounterInput
import com.primeholding.primesampleapp.viewmodel.CounterOutput
import com.primeholding.primesampleapp.viewmodel.CounterType
import com.primeholding.primesampleapp.viewmodel.CounterViewModel
import io.reactivex.disposables.Disposable
import javax.inject.Inject


class CounterActivity : BaseActivity() {

    private lateinit var activityBinding: ActivityCounterBinding

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: CounterType by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(CounterViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        BaseApplication.appComponent.inject(this)
        activityBinding = DataBindingUtil.setContentView(this, R.layout.activity_counter)
    }

    override fun bind() {
        viewModel
            .bind(activityBinding)
            .addTo(compositeDisposable)
        activityBinding
            .bind(this)
            .addTo(compositeDisposable)
    }

    fun goToDetails() {
        DetailsActivity.start(this)
    }


    companion object {
        fun start(sourceActivity: Activity) {
            sourceActivity.startActivity(Intent(sourceActivity, CounterActivity::class.java))
        }
    }
}

private fun ActivityCounterBinding.bind(activity: CounterActivity): List<Disposable> {
    return listOf(
        counterGoToDetailsButton.rxClick.subscribe { activity.goToDetails() }
    )

}

private fun CounterType.bind(binding: ActivityCounterBinding): List<Disposable> {
    return listOf(
        output.bind(binding),
        input.bind(binding)
    ).flatten()
}

private fun CounterInput.bind(binding: ActivityCounterBinding): List<Disposable> {
    return listOf(
        binding.counterIncrementButton.rxClick.subscribe { increment() },
        binding.counterDecrementButton.rxClick.subscribe { decrement() }
    )
}

private fun CounterOutput.bind(binding: ActivityCounterBinding): List<Disposable> {
    return listOf(
        currentCount.subscribe {
            binding.counterCurrentCount.text =
                    String.format(binding.counterCurrentCount.context.getString(R.string.current_count_format), it)
        }
    )
}