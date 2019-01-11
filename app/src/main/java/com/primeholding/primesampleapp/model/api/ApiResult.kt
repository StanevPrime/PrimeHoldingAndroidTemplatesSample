package com.primeholding.primesampleapp.model.api

import com.primeholding.primesampleapp.model.error.IError

sealed class ApiResult<Data> {
    /**
     * Represents a Loading state of the request
     */
    data class Loading<Data>(var unit: Unit = Unit) : ApiResult<Data>()

    /**
     * Represents a success from the request
     * @param data The data received successfully
     */
    data class Success<Data>(var data: Data) : ApiResult<Data>()

    /**
     * Represents an Error
     * @param error the error to be passed to the view
     * @see IError
     */
    data class Error<Data>(var error: IError) : ApiResult<Data>()
}