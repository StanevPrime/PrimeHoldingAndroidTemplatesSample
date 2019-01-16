package com.primeholding.primesampleapp.misc.extension

import android.view.View
import android.widget.EditText
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.Observable

var View.rxClick: Observable<Unit>
    get() = RxView.clicks(this).map { Unit }
    set(value) {}


var EditText.rxTextChanges: Observable<String>
    get() = RxTextView.textChanges(this).map { it.toString() }
    set(value) {}
