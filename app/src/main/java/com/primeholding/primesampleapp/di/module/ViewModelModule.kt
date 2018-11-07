package com.primeholding.primesampleapp.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.primeholding.primesampleapp.di.util.ViewModelFactory
import com.primeholding.primesampleapp.di.util.ViewModelKey
import com.primeholding.primesampleapp.viewmodel.CounterViewModel
import com.primeholding.primesampleapp.viewmodel.DetailsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    /**
     * Use as shown in the comment below
     */

//    @Binds
//    @IntoMap
//    @ViewModelKey(YourViewModelClass::class)
//    internal abstract fun yourViewModelClass(viewModel: YourViewModelClass): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CounterViewModel::class)
    internal abstract fun counterViewModel(viewModel: CounterViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DetailsViewModel::class)
    internal abstract fun detailsViewModel(viewModel: DetailsViewModel): ViewModel

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}
