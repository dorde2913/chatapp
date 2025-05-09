package com.example.chatapp.stateholders

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapp.AUTH_TOKEN
import com.example.chatapp.USER_DATA
import com.example.chatapp.data.repositories.ChatRepository
import com.example.chatapp.data.retrofit.models.Chat
import com.example.chatapp.data.socketio.Message
import com.example.chatapp.dataStore

import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val chatRepository: ChatRepository,
    @ApplicationContext context: Context
): ViewModel() {

    private lateinit var userData: UserData
    private lateinit var auth_token: String


    private val _chatRooms = MutableStateFlow(listOf<Chat>())
    val chatRooms = _chatRooms.asStateFlow()

    init{
        //ovde moramo da imamo auth_token, uzimamo USER_DATA
        CoroutineScope(Dispatchers.IO).launch {
            context.dataStore.data.collect{preferences ->
                userData = Json.decodeFromString(preferences[USER_DATA]!!.toString(charset = Charsets.UTF_8))
                auth_token = preferences[AUTH_TOKEN]!!
                /*
                ne bi trebalo da moze da se dodje do
                ekrana koji koristi ovaj viewmodel ako nemamo auth token i user_data
                 */
                _chatRooms.value = chatRepository.getChats(userData.username)
                connect()
            }

        }
    }
    //ovde su metode za rad sa socketom

    val latestMessage = chatRepository.latestMessage


    fun connect() {
        chatRepository.connect(auth_token, userData.username)
    }


    fun disconnect() =
        chatRepository.disconnect()

    fun joinRoom(roomID: String) =
        chatRepository.joinRoom(roomID)

    fun sendMsg(message: Message) =
        chatRepository.sendMsg(message)


    fun createRoom(name: String,isGroup: Boolean, participants: List<String>) = viewModelScope.launch {
        chatRepository.createRoom(name,isGroup,participants)
    }


    suspend fun getRecentMessages(roomID: String) =
        chatRepository.getRecentMessages(roomID)


    suspend fun getChats() =
        chatRepository.getChats(userData.username)
}