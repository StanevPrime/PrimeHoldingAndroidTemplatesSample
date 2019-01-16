package com.primeholding.primesampleapp.di.component

import android.content.Context
import com.primeholding.primesampleapp.application.BaseApplication
import com.primeholding.primesampleapp.di.module.AppModule
import com.primeholding.primesampleapp.di.module.NetworkModule
import com.primeholding.primesampleapp.di.module.ViewModelModule
import com.primeholding.primesampleapp.view.CounterActivity
import com.primeholding.primesampleapp.view.DetailsActivity
import dagger.Component
import javax.inject.Singleton


@Singleton
@Component(modules = [AppModule::class, NetworkModule::class, ViewModelModule::class])
interface AppComponent {

    fun context(): Context

    /**
     * Inject your activities like this :
     * fun inject(activity: AnyActivity)
     */
    fun inject(app: BaseApplication)

    fun inject(clazz: CounterActivity)

    fun inject(clazz: DetailsActivity)


}