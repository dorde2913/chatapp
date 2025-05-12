package com.example.chatapp.data.repositories

import android.util.Log
import com.example.chatapp.data.retrofit.AuthResponse
import com.example.chatapp.data.retrofit.ChatApi
import com.example.chatapp.data.retrofit.LoginBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val chatApi: ChatApi
){

    suspend fun login(username: String, password: String) =
        try{
            chatApi.login(LoginBody(username, password))
        }
        catch(error: Exception){
            Log.e("Auth Repository", error.toString())
            AuthResponse()
        }


    suspend fun register(username: String, password: String) =
        try{
            chatApi.register(LoginBody(username,password))
        }
        catch(error: Exception){
            Log.e("Auth Repository", error.toString())
            AuthResponse()
        }
}