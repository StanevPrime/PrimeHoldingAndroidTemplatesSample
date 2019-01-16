package com.primeholding.primesampleapp

import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.TestScheduler
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

class TestSchedulerRule : TestRule {

    val thread = TestScheduler()
        get() = field

    override fun apply(base: Statement, description: Description?): Statement {
        return object : Statement() {
            @Throws(Throwable::class)
            override fun evaluate() {
                RxJavaPlugins.setIoSchedulerHandler { thread }
                RxJavaPlugins.setComputationSchedulerHandler { thread }
                RxJavaPlugins.setNewThreadSchedulerHandler { thread }
                RxAndroidPlugins.setInitMainThreadSchedulerHandler { thread }

                try {
                    base.evaluate()
                } finally {
                    RxJavaPlugins.reset()
                    RxAndroidPlugins.reset()
                }
            }
        }
    }
}