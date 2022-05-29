package com.bruno.topheadlines.data.service

import com.bruno.topheadlines.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class OkHttpInterceptor: Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
            .newBuilder()
            .addHeader("X-API-KEY", BuildConfig.API_KEY)
            .build()
        return chain.proceed(request)
    }
}