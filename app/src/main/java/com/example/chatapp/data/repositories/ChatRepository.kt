package com.example.chatapp.data.repositories

import android.content.Context
import android.util.Log
import com.example.chatapp.AUTH_TOKEN
import com.example.chatapp.USER_DATA
import com.example.chatapp.data.retrofit.ChatApi
import com.example.chatapp.data.retrofit.ChatBody
import com.example.chatapp.data.retrofit.ChatsBody
import com.example.chatapp.data.retrofit.RoomBody
import com.example.chatapp.data.retrofit.models.Chat

import com.example.chatapp.data.socketio.Message
import com.example.chatapp.data.socketio.SocketManager
import com.example.chatapp.dataStore
import com.example.chatapp.stateholders.UserData
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

/*
ovaj repo je za dohvatanje poruka/chatova, pravljenje chatova, slanje poruka etc sve vezano za
same chatove
 */
@Singleton
class ChatRepository @Inject constructor(
    private val socketManager: SocketManager,
    private val chatApi: ChatApi,
    @ApplicationContext context: Context
){

    val _currentChat = MutableStateFlow(Chat(_id=""))
    val currentChat = _currentChat.asStateFlow()

    val _connectionFailed = MutableStateFlow(false)
    val connectionFailed = _connectionFailed.asStateFlow()



    val latestMessage = socketManager.latestMessage

    private fun logError(e: Exception) = Log.e("ChatRepository",e.toString())

    fun setOpts(token: String, username: String) =
        socketManager.setOpts(token,username)

    fun connect() {
        try{
            socketManager.connect()
        }
        catch(e: Exception){
            logError(e)
        }
    }

    fun refresh() {
        _connectionFailed.value = false
    }

    fun disconnect() {
        socketManager.disconnect()
    }


    fun sendMsg(message: Message) {
        try{
            socketManager.sendMsg(message = message)
        }
        catch(e: Exception){
            logError(e)
        }
    }


    fun joinRoom(roomID: String) {
        try{
            socketManager.joinRoom(roomID)
        }
        catch(e: Exception){
            logError(e)
        }
    }




    suspend fun createRoom(name: String,isGroup: Boolean, participants: List<String>)=
        try{
            chatApi.createRoom(ChatBody(name,isGroup,participants))
        }
        catch(e: Exception){
            logError(e)
            ChatBody()
        }



    suspend fun getRecentMessages(roomID: String) =
        try{
            chatApi.getRecentMessages(RoomBody(roomID))
        }
        catch(e: Exception){
            logError(e)
            listOf()
        }



    suspend fun getChats(username: String)=
        try{
            chatApi.getChats(ChatsBody(username))
        }
        catch(e: Exception){
            _connectionFailed.value = true
            logError(e)
            listOf()
        }



    suspend fun getChat(roomID: String)=
        try{
            chatApi.getChat(roomID)
        }
        catch(e: Exception){
            logError(e)
            null
        }




}