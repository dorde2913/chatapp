package com.example.chatapp.data.repositories

import android.content.Context
import com.example.chatapp.AUTH_TOKEN
import com.example.chatapp.USER_DATA
import com.example.chatapp.data.retrofit.ChatApi
import com.example.chatapp.data.retrofit.ChatBody
import com.example.chatapp.data.retrofit.ChatsBody
import com.example.chatapp.data.retrofit.RoomBody

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

@Singleton
class ChatRepository @Inject constructor(
    private val socketManager: SocketManager,
    private val chatApi: ChatApi,
    @ApplicationContext context: Context
){
    /*
    rad sa socketom, i pozivi za cuvanje poruka u db?
     */

    lateinit var authToken: String
    lateinit var userData: UserData
    init{
        CoroutineScope(Dispatchers.IO).launch {
            context.dataStore.data.collect{
                authToken = it[AUTH_TOKEN] ?: ""
                if (it[USER_DATA]!=null)
                    userData = Json.decodeFromString(it[USER_DATA]!!.toString(Charsets.UTF_8))
            }
        }

    }


    val latestMessage = socketManager.latestMessage



    fun setOpts(token: String, username: String) =
        socketManager.setOpts(token,username)
    fun connect() =
        socketManager.connect()

    fun disconnect() {
        socketManager.disconnect()
    }


    fun sendMsg(message: Message) =
        socketManager.sendMsg(message = message)

    fun joinRoom(roomID: String) =
        socketManager.joinRoom(roomID)


    suspend fun createRoom(name: String,isGroup: Boolean, participants: List<String>) =
        chatApi.createRoom(ChatBody(name,isGroup,participants))

    suspend fun getRecentMessages(roomID: String) =
        chatApi.getRecentMessages(RoomBody(roomID))

    suspend fun getChats(username: String)=
        chatApi.getChats(ChatsBody(username))


}