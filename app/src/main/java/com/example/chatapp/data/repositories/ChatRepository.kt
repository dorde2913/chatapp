package com.example.chatapp.data.repositories

import com.example.chatapp.data.retrofit.ChatApi
import com.example.chatapp.data.retrofit.ChatBody
import com.example.chatapp.data.retrofit.ChatsBody
import com.example.chatapp.data.retrofit.RoomBody

import com.example.chatapp.data.socketio.Message
import com.example.chatapp.data.socketio.SocketManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChatRepository @Inject constructor(
    private val socketManager: SocketManager,
    private val chatApi: ChatApi
){
    /*
    rad sa socketom, i pozivi za cuvanje poruka u db?
     */

    val latestMessage = socketManager.latestMessage

    fun connect(token: String, username: String) =
        socketManager.connect(token, username)

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

    suspend fun getChats(username: String) =
        chatApi.getChats(ChatsBody(username))
}