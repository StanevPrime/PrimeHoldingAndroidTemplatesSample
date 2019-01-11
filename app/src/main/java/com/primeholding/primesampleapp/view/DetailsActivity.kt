package com.primeholding.primesampleapp.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.jakewharton.rxbinding2.view.visibility
import com.primeholding.primesampleapp.R
import com.primeholding.primesampleapp.application.BaseApplication
import com.primeholding.primesampleapp.databinding.ActivityDetailsBinding
import com.primeholding.primesampleapp.misc.extension.addTo
import com.primeholding.primesampleapp.misc.extension.rxClick
import com.primeholding.primesampleapp.view.base.BaseActivity
import com.primeholding.primesampleapp.viewmodel.DetailsInput
import com.primeholding.primesampleapp.viewmodel.DetailsOutput
import com.primeholding.primesampleapp.viewmodel.DetailsType
import com.primeholding.primesampleapp.viewmodel.DetailsViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import javax.inject.Inject


class DetailsActivity : BaseActivity() {

    lateinit var activityBinding: ActivityDetailsBinding

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: DetailsType by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(DetailsViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        BaseApplication.appComponent.inject(this)
        activityBinding = DataBindingUtil.setContentView(this, R.layout.activity_details)
    }

    override fun bind() {
        viewModel
            .bind(this)
            .addTo(compositeDisposable)
        viewModel.input.fetch()
    }


    companion object {
        fun start(sourceActivity: Activity) {
            sourceActivity.startActivity(Intent(sourceActivity, DetailsActivity::class.java))
        }
    }
}

private fun DetailsType.bind(activity: DetailsActivity): List<Disposable> {
    return listOf(
        output.bind(activity),
        output.bind(activity.activityBinding),
        input.bind(activity.activityBinding)
    ).flatten()
}

private fun DetailsInput.bind(binding: ActivityDetailsBinding): List<Disposable> {
    return listOf(
        binding.detailsRefreshButton.rxClick.subscribe {
            fetch()
        }
    )
}

private fun DetailsOutput.bind(binding: ActivityDetailsBinding): List<Disposable> {
    return listOf(
        details
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { binding.detailsDetailsTextView.text = it },
        isLoading
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                binding.detailsProgressBar.visibility().accept(it)
            }
    )
}


private fun DetailsOutput.bind(activity: DetailsActivity): List<Disposable> {
    return listOf(
        errorStream
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                activity.renderErrorState(it)
            }
    )
}