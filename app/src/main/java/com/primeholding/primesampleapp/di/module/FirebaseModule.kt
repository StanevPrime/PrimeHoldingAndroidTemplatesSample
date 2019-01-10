package com.primeholding.primesampleapp.di.module

import android.content.Context
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

//TODO add to your app component
@Module
class FirebaseModule {

    @Provides
    @Singleton
    fun provideAnalytics(context: Context): FirebaseAnalytics {
        return FirebaseAnalytics.getInstance(context)
    }
}