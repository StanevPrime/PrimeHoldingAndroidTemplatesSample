package com.primeholding.primesampleapp.model.error

import android.os.Parcelable

/**
Use this as interface for all your errors
 */
interface IError : Parcelable {
    var description: String
}