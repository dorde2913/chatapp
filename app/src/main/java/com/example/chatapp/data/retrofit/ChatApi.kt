package com.example.chatapp.data.retrofit


import com.example.chatapp.data.retrofit.models.Chat
import com.example.chatapp.data.socketio.Message
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

const val BASE_URL = "http://10.0.2.2:4000/"

interface ChatApi{
    @POST("user/login")
    suspend fun login(@Body body: LoginBody): AuthResponse

    @POST("user/test")
    suspend fun testToken(@Header("authorization") token: String): String

    @POST("user/register")
    suspend fun register(@Body body: LoginBody): AuthResponse

    @POST("user/createChat")
    suspend fun createRoom(@Body body: ChatBody): ChatBody

    @POST("user/getRecents")
    suspend fun getRecentMessages(@Body roomID: RoomBody): List<Message>


    @POST("user/getChats")
    suspend fun getChats(@Body chats: ChatsBody): List<Chat>
}

data class ChatsBody(
    val username: String
)
data class RoomBody(
    val roomID: String
)

data class ChatBody(
    val name: String,
    val isGroup: Boolean,
    val participants: List<String>
)



data class AuthResponse(
    val error: Int,
    val token: String,
    val username: String,
    val displayname: String,
    val friends: List<String>
)

enum class ErrorType(val value: Int){
    NO_ERROR(0),
    UNKNOWN_USER(1),
    BAD_PASSWORD(2),
    DUPLICATE_USER(3),
}

data class LoginBody(
    val username: String,
    val password: String
)