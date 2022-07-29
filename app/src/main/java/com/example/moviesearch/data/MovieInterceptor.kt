package com.example.moviesearch.data

import com.example.moviesearch.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieInterceptor @Inject constructor() : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val clientId = BuildConfig.CLIENT_ID
        val clientKey = BuildConfig.CLIENT_KEY
        val requestBuilder = chain.request()
            .newBuilder()

        requestBuilder.addHeader("X-Naver-Client-Id", clientId)
        requestBuilder.addHeader("X-Naver-Client-Secret", clientKey)
        return chain.proceed(requestBuilder.build())
    }
}