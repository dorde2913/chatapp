package com.example.chatapp.data.repositories

import com.example.chatapp.data.retrofit.ChatApi
import com.example.chatapp.data.retrofit.LoginBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val chatApi: ChatApi
){
    suspend fun login(username: String, password: String) =
        chatApi.login(LoginBody(username, password))

    suspend fun register(username: String, password: String) =
        chatApi.register(LoginBody(username,password))
}