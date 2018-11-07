package com.primeholding.primesampleapp.di.module

import androidx.lifecycle.ViewModelProvider
import com.primeholding.primesampleapp.di.util.ViewModelFactory
import dagger.Binds
import dagger.Module

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
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}
