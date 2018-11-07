package com.primeholding.primesampleapp.application

import android.app.Application
import com.primeholding.primesampleapp.di.component.AppComponent
import com.primeholding.primesampleapp.di.component.DaggerAppComponent
import com.primeholding.primesampleapp.di.module.AppModule


class BaseApplication : Application() {

    companion object {
        lateinit var appComponent: AppComponent
    }

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder().appModule(AppModule(this, this)).build()
        appComponent.inject(this)

    }

}