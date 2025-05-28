package com.example.chatapp.data.retrofit


import com.example.chatapp.data.repositories.TokenProvider
import com.example.chatapp.data.repositories.UserRepository

import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthInterceptor  @Inject constructor(
    private val tokenProvider: TokenProvider
): Interceptor {


    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        val token = tokenProvider._authToken.value

        val newRequest =
            request.newBuilder()
                .addHeader("Authorization", token)
                .build()


        return chain.proceed(newRequest)
    }
}