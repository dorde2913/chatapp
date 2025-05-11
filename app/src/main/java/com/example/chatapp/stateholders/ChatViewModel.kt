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
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.stateIn
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


    val latestMessage = chatRepository.latestMessage

    val _uiState = MutableStateFlow(ChatUiState())
    val uiState = _uiState.asStateFlow()

    var roomID: String? = null

    val authToken = chatRepository.authToken
    val userData = chatRepository.userData



    init{
        viewModelScope.launch {
            latestMessage.collect{ message ->
                println(message.content)
                _uiState.value = _uiState.value.copy(
                    messages = _uiState.value.messages.plus(message)
                )
            }
        }

        setOpts(authToken,userData.username)
        connect()

    }


    fun setOpts(authToken: String, username: String) =
        chatRepository.setOpts(authToken,username)

    fun connect() {
        chatRepository.connect()
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


    suspend fun getChats(username: String) =
        chatRepository.getChats(username)

}