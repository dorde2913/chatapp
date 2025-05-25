package com.example.chatapp.data.retrofit


import com.example.chatapp.data.repositories.AboutBody
import com.example.chatapp.data.repositories.DisplayBody
import com.example.chatapp.data.retrofit.models.Chat
import com.example.chatapp.data.socketio.Message
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
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

    @GET("user/getChat/{roomID}")
    suspend fun getChat(@Path("roomID") roomID: String): Chat?

    @PUT("user/setPfp")
    suspend fun setProfilePic(@Body body: PfpBody)

    @POST("user/FCM/sendToken")
    suspend fun sendFCMToken(@Body body: FCMTokenBody)

    @POST("user/setAbout")
    suspend fun setAbout(@Body aboutBody: AboutBody)

    @POST("user/setDisplayName")
    suspend fun setDisplayName(@Body displayBody: DisplayBody)
}

/*
ovo sve ovde treba malo da se sredi nmp kako doduse
 */
data class FCMTokenBody(
    val token: String,
    val username: String
)

data class PfpBody(
    val username: String,
    val pic: ByteArray
)

data class ChatsBody(
    val username: String
)
data class RoomBody(
    val roomID: String
)

data class ChatBody(
    val name: String? = null,
    val isGroup: Boolean = false,
    val participants: List<String> = listOf(),
)



data class AuthResponse(
    val error: Int = ErrorType.NO_RESPONSE.value,
    val token: String = "",
    val username: String = "",
    val displayname: String = "",
    val friends: List<String> = listOf(),
    val chatRooms: List<String> = listOf()
)

enum class ErrorType(val value: Int){
    NO_ERROR(0),
    UNKNOWN_USER(1),
    BAD_PASSWORD(2),
    DUPLICATE_USER(3),
    NO_RESPONSE(4)
}

data class LoginBody(
    val username: String,
    val password: String
)