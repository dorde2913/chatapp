package com.example.chatapp.services

import android.util.Log
import androidx.datastore.preferences.core.edit
import com.example.chatapp.USER_DATA
import com.example.chatapp.data.repositories.UserRepository
import com.example.chatapp.data.retrofit.ChatApi
import com.example.chatapp.dataStore
import com.example.chatapp.stateholders.UserData
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject

@AndroidEntryPoint
class ChatNotificationService: FirebaseMessagingService() {

    @Inject lateinit var userRepository: UserRepository

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        //save in db
        println("new token")
        CoroutineScope(Dispatchers.IO).launch {
            userRepository.sendFCMToken(token)
        }

    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)


        println(message.notification?.title)

        Log.d("FCM",message.data.toString())

        /*
        za sad fcm isljucivo salje userdata updates, posle cemo imati neki when da odredimo
        sta smo primili da li je notifikacija ili sta
         */
        CoroutineScope(Dispatchers.IO).launch {
            this@ChatNotificationService.dataStore.edit {
                //it[USER_DATA] =
                val temp: UserData = Json.decodeFromString(it[USER_DATA]!!.toString(Charsets.UTF_8))
                val update: UserDataUpdate = Json.decodeFromString(message.data["userData"]!!)
                it[USER_DATA] = Json.encodeToString(
                    temp.copy(
                        displayName = update.displayname,
                        friends = update.friends,
                        chatRooms = update.chatRooms,
                        pfpUrl = update.pfpUrl,
                        about = update.about
                    )
                ).toByteArray()
            }
        }

        
    }
}

@Serializable
data class UserDataUpdate(
    val displayname: String,
    val friends: List<String>,
    val chatRooms: List<String>,
    val pfpUrl: String,
    val about: String
)