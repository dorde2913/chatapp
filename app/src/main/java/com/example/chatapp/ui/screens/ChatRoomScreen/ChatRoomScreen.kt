package com.example.chatapp.ui.screens.ChatRoomScreen


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
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
import androidx.datastore.dataStore
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.chatapp.AUTH_TOKEN
import com.example.chatapp.USER_DATA
import com.example.chatapp.data.socketio.Message
import com.example.chatapp.dataStore
import com.example.chatapp.ui.screens.ChatRoomScreen.components.MessageCard
import com.example.chatapp.stateholders.ChatViewModel
import com.example.chatapp.stateholders.UserData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.time.Instant
import java.time.LocalTime

@Composable
fun ChatRoom(
    roomID: String,
    ){
    val context = LocalContext.current
    val viewModel: ChatViewModel = hiltViewModel()

    val uiState by viewModel.uiState.collectAsState()
    var messageToBeSent by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.joinRoom(roomID)
    }



    Column(modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ){
        LazyColumn (
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.9f),
            horizontalAlignment = Alignment.CenterHorizontally,
            reverseLayout = true
        ){

            items(uiState.messages.reversed()){msg ->

                MessageCard(message = msg)

            }

        }
        Row(modifier = Modifier
            .fillMaxWidth()
            .weight(0.1f)){
            OutlinedTextField(
                value = messageToBeSent,
                onValueChange = {messageToBeSent = it},
                modifier = Modifier.weight(0.7f)
            )
            Button(
                modifier = Modifier.weight(0.3f),
                onClick = {
                    viewModel.joinRoom(roomID) //ovo radi, nece mu nista biti ako ovako ostane, iako nije logicno bas
                    viewModel.sendMsg(
                        Message(
                            sender = viewModel.userData.username,
                            roomID = roomID,
                            content = messageToBeSent,
                            timeStamp = Instant.now().epochSecond
                        )
                    )
                    messageToBeSent = ""
                },
                enabled = (messageToBeSent.isNotBlank())
            ){
                Text("send message")
            }
        }

    }



}