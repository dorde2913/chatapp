package com.example.chatapp.screens.HomeScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.datastore.preferences.core.edit

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.chatapp.AUTH_TOKEN
import com.example.chatapp.USER_DATA
import com.example.chatapp.data.retrofit.models.Chat
import com.example.chatapp.dataStore
import com.example.chatapp.screens.ChatRoomScreen.ChatRoom
import com.example.chatapp.screens.HomeScreen.components.ChatRow

import com.example.chatapp.stateholders.ChatViewModel
import com.example.chatapp.stateholders.UserData
import com.google.firebase.Firebase
import com.google.firebase.messaging.messaging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


@Composable
fun HomeScreen(
    navigateToChat: (String) ->Unit,
    navigateToLogin: () -> Unit
){

    val context = LocalContext.current

    val viewModel: ChatViewModel = hiltViewModel()


   // Text(text = "${chatRooms.size}")text = Firebase.messaging.token.await()

    val chatRooms by viewModel.chatRooms.collectAsStateWithLifecycle()


    LazyColumn {
        //ovde su razliciti chatovi
        item{
            Button(
                onClick = {
                    CoroutineScope(Dispatchers.IO).launch {
                        context.dataStore.edit {
                            it.remove(AUTH_TOKEN)
                        }

                    }
                    navigateToLogin()
                }
            ){
                Text("sign out")
            }
        }
        items(chatRooms){ chat ->

            ChatRow(chat = chat, navigateToChat = navigateToChat)

        }
    }


}