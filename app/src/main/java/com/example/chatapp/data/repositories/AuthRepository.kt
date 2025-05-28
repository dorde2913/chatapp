package com.example.chatapp.data.repositories

import android.util.Log
import com.example.chatapp.data.retrofit.AuthResponse
import com.example.chatapp.data.retrofit.ChatApi
import com.example.chatapp.data.retrofit.LoginBody
import javax.inject.Inject
import javax.inject.Singleton

/*
ovo se iskljucivo koristi za autentikaciju, mozda se spoji posle sa user repo ko zna
 */
@Singleton
class AuthRepository @Inject constructor(
    private val chatApi: ChatApi
){

    private suspend fun tryCatch(call: suspend()->AuthResponse) =
        try{
            call()
        }
        catch(error: Exception){
            Log.e("Auth Repository", error.toString())
            AuthResponse()
        }


    suspend fun login(username: String, password: String) =
        tryCatch {chatApi.login(LoginBody(username, password))}


    suspend fun register(username: String, password: String) =
        tryCatch { chatApi.register(LoginBody(username,password)) }
}