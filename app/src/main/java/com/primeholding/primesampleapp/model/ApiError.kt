package com.primeholding.primesampleapp.model

import android.os.Parcelable
import com.google.gson.Gson
import kotlinx.android.parcel.Parcelize
import retrofit2.HttpException

@Parcelize
data class ApiError(
    val detail: String,
    val status: HttpStatusCode,
    val title: String,
    val type: String,
    val validationMessages: HashMap<String, HashMap<String, String>>
) : Parcelable {


    companion object {

        private val gson = Gson()

        fun from(throwable: HttpException): ApiError {
            return gson.fromJson(throwable.response().errorBody()?.charStream(), ApiError::class.java)
        }

        fun fromDetail(detail: String, status: HttpStatusCode): ApiError {
            return ApiError(detail, status, "", "", HashMap())
        }
    }
}