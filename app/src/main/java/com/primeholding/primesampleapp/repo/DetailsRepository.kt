package com.primeholding.primesampleapp.repo

import com.primeholding.primesampleapp.model.api.ApiError
import com.primeholding.primesampleapp.model.api.ApiResult
import io.reactivex.Observable
import retrofit2.HttpException
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class DetailsRepository @Inject constructor() {

    fun fetchDetails(): Observable<ApiResult<String>> {
        return Observable.just(Unit)
            .delay(2000, TimeUnit.MILLISECONDS)
            .take(1)
            .map { "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat." }
            .map<ApiResult<String>> { it -> ApiResult.Success(it) }
            .onErrorReturn { it -> ApiResult.Error(ApiError.from(it as HttpException)) }
            .startWith(ApiResult.Loading())
    }
}