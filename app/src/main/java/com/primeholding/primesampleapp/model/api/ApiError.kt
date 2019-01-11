package com.primeholding.primesampleapp.model.api

import com.google.gson.Gson
import com.primeholding.primesampleapp.model.error.IError
import kotlinx.android.parcel.Parcelize
import org.json.JSONObject
import retrofit2.HttpException


@Parcelize
data class ApiError(
    override var description: String = "Network Error",
    val status: HttpStatusCode = HttpStatusCode.NO_CONTENT,
    val details: String
) : IError {

    companion object {
        private val gson = Gson()

        fun from(throwable: HttpException): ApiError {
            val errorDetail = getErrorDetail(throwable)
            return ApiError(
                errorDetail?.message?.status ?: "",
                HttpStatusCode.fromCode(throwable.code()),
                errorDetail?.message?.message ?: ""
            )
        }


        private fun getErrorDetail(throwable: HttpException): ErrorDetail? {
            return try {
                val jObjError = JSONObject(throwable.response().errorBody()?.string())
                gson.fromJson(jObjError.toString(), ErrorDetail::class.java)
            } catch (e: Exception) {
                ErrorDetail(throwable.code(), Message(throwable.localizedMessage, throwable.code().toString()))
            }
        }

    }
}
