package com.example.chatapp.stateholders

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.chatapp.AUTH_TOKEN
import com.example.chatapp.USER_DATA
import com.example.chatapp.data.repositories.ChatRepository
import com.example.chatapp.data.retrofit.models.Chat
import com.example.chatapp.dataStore

import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val chatRepository: ChatRepository,
    @ApplicationContext private val context: Context
): ViewModel() {




    private val _chatRooms = MutableStateFlow(listOf<Chat>())
    val chatRooms = _chatRooms.asStateFlow()



    fun loadChats() = viewModelScope.launch {
        val userData: UserData =
            context.dataStore.data.map{Json.decodeFromString(it[USER_DATA]!!.toString(Charsets.UTF_8)) as UserData}.first()
        Log.d("User data", userData.toString())
        _chatRooms.value = chatRepository.getChats(userData.username)
        context.dataStore.edit {
            it[USER_DATA] = Json.encodeToString(
                userData.copy(chatRooms = _chatRooms.value.map{it._id})
            ).toByteArray()
        }
    }






    fun createRoom(
        name: String,isGroup: Boolean,
        participants: List<String>)
    = viewModelScope.launch {
        chatRepository.createRoom(name,isGroup,participants)
    }



}