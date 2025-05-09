package com.example.chatapp.data.socketio

import android.util.Log
import com.example.chatapp.data.retrofit.BASE_URL
import io.socket.client.IO
import io.socket.client.Socket
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.json.JSONObject
import javax.inject.Singleton

@Singleton
class SocketManager {

    private lateinit var socket: Socket

    val _latestMessage = MutableSharedFlow<Message>(0)
    val latestMessage = _latestMessage.asSharedFlow()

    fun connect(token: String, username: String) {
        Log.d("SocketManager", "entered connect")
        val opts = IO.Options()
        opts.auth = mapOf("token" to token, "username" to username)

        socket = IO.socket("ws://10.0.2.2:4000/", opts)

        socket.on(Socket.EVENT_CONNECT) {
            println("Connected to Socket.IO server")
        }

        socket.on("roomMessage") { args ->
            val data = args[0] as JSONObject
            val sender = data.getString("sender")
            val message = data.getString("message")
            CoroutineScope(Dispatchers.IO).launch {
                val msg = Json.decodeFromString<Message>(message)
                _latestMessage.emit(msg)
            }

            println("Message from $sender: $message")
        }

        socket.connect()
    }

    fun joinRoom(roomID: String)=
        socket.emit("joinRoom", roomID)

    fun sendMsg(message: Message){
        val msg = JSONObject()
        msg.put("room", message.roomID)
        msg.put("message", Json.encodeToString(message) )
        socket.emit("roomMessage", msg)

    }

    fun disconnect(){
        socket.disconnect()
        socket.close()
    }


}

@Serializable
data class Message(
    val sender: String,//username
    val roomID: String,
    val content: String,
    val timeStamp: Long
)