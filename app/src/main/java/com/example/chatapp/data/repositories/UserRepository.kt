package com.example.chatapp.data.repositories

import android.content.Context
import android.util.Log
import com.example.chatapp.AUTH_TOKEN
import com.example.chatapp.USER_DATA
import com.example.chatapp.data.retrofit.ChatApi
import com.example.chatapp.data.retrofit.FCMTokenBody
import com.example.chatapp.data.retrofit.PfpBody
import com.example.chatapp.dataStore
import com.example.chatapp.stateholders.UserData
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

/*
 repo za pristup korisnickim podacima i auth tokenu, mozda cemo neke u local db
 ovde ce biti i metode koje ce da menjaju korisnicke podatke(displayname, profilna slika)
 , tkd ce trebati i pristup chatApi
 */

@Singleton
class UserRepository @Inject constructor(
    @ApplicationContext context: Context,
    val chatApi: ChatApi
) {

    val _userData = MutableStateFlow(UserData())
    val userData = _userData.asStateFlow()

    val _authToken = MutableStateFlow("")
    val authToken = _authToken.asStateFlow()

    init{
        CoroutineScope(Dispatchers.IO).launch {
            context.dataStore.data.collect{preferences ->
                if (preferences[USER_DATA]!=null) {
                    println("test")
                    _userData.value = Json.decodeFromString(preferences[USER_DATA]!!.toString(Charsets.UTF_8))
                    Log.d("UserRepo",_userData.value.pfpUrl)
                }

                if (preferences[AUTH_TOKEN]!=null){
                    _authToken.value = preferences[AUTH_TOKEN]!!
                }

            }
        }

    }

    suspend fun setProfilePic(pic: ByteArray,username: String) =
        try{
            chatApi.setProfilePic(PfpBody(username,pic))
        }
        catch(err: Exception){
            Log.e("UserRepo",err.toString())
        }


    suspend fun sendFCMToken(token: String) =
        try{
            chatApi.sendFCMToken(FCMTokenBody( token,_userData.value.username))
        }
        catch(err: Exception){
            Log.e("UserRepo",err.toString())
        }

}