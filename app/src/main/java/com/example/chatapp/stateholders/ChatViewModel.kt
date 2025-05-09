package com.example.chatapp.stateholders

import android.content.Context
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapp.AUTH_TOKEN
import com.example.chatapp.USER_DATA
import com.example.chatapp.data.repositories.ChatRepository
import com.example.chatapp.data.socketio.Message
import com.example.chatapp.dataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import javax.inject.Inject

data class ChatUiState(
    val messages: List<Message> = listOf()
)

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val chatRepository: ChatRepository,
    @ApplicationContext context: Context
): ViewModel() {

    lateinit var userData: UserData
    private lateinit var auth_token: String

    val latestMessage = chatRepository.latestMessage

    val _uiState = MutableStateFlow(ChatUiState())
    val uiState = _uiState.asStateFlow()

    var roomID: String? = null




    init{
        viewModelScope.launch {
            context.dataStore.data.collect{preferences ->
                userData = Json.decodeFromString(preferences[USER_DATA]!!.toString(charset = Charsets.UTF_8))
                auth_token = preferences[AUTH_TOKEN]!!

                connect()
                joinRoom(roomID?:"")
            }

        }
        viewModelScope.launch {

            latestMessage.collect{ message ->

                println(message.content)
                _uiState.value = _uiState.value.copy(
                    messages = _uiState.value.messages.plus(message)
                )
            }
        }

    }



    fun connect() {
        chatRepository.connect(auth_token, userData.username)
    }


    fun disconnect() =
        chatRepository.disconnect()

    fun joinRoom(roomID: String) {
        chatRepository.joinRoom(roomID)
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                messages = getRecentMessages(roomID)
            )
        }

    }


    fun sendMsg(message: Message) =
        chatRepository.sendMsg(message)

        suspend fun getRecentMessages(roomID: String) =
        chatRepository.getRecentMessages(roomID)


    suspend fun getChats() =
        chatRepository.getChats(userData.username)

}