package com.example.chatapp.screens.ChatRoomScreen


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.core.edit
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.chatapp.AUTH_TOKEN
import com.example.chatapp.USER_DATA
import com.example.chatapp.data.socketio.Message
import com.example.chatapp.dataStore
import com.example.chatapp.screens.ChatRoomScreen.components.MessageCard
import com.example.chatapp.stateholders.ChatViewModel
import com.example.chatapp.stateholders.UserData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.time.LocalTime

@Composable
fun ChatRoom(roomID: String){

    var connected by rememberSaveable { mutableStateOf(false) }
    val chatViewModel: ChatViewModel = hiltViewModel()

    val context = LocalContext.current
    var messageToBeSent by rememberSaveable { mutableStateOf("") }

    val auth_token by context.dataStore.data.map{it[AUTH_TOKEN]}.collectAsState(initial = null)

    val messagesList = remember { mutableStateListOf<Message>() }




    val userData = Json.decodeFromString<UserData>(context.dataStore.data.map{it[USER_DATA]}
        .collectAsState(initial = Json.encodeToString(UserData()).toByteArray()).value!!.toString(charset = Charsets.UTF_8))

    LaunchedEffect(auth_token) {
        if (auth_token == null) return@LaunchedEffect


        connected = true
        chatViewModel.connect()
        chatViewModel.joinRoom(roomID)

        messagesList.addAll(
            chatViewModel.getRecentMessages(roomID)
        )

        CoroutineScope(Dispatchers.IO).launch {
            chatViewModel.latestMessage.collect{
                messagesList.add(it as Message)//wouldn't build without redundant cast, most likely IDE is buggin out
            }
        }
    }
    Column(modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ){
        LazyColumn (
            modifier = Modifier.fillMaxWidth().weight(0.9f),
            horizontalAlignment = Alignment.CenterHorizontally,
            reverseLayout = true
        ){

            items(messagesList.reversed()){msg ->
                MessageCard(msg.sender != userData.username,msg.sender, msg.content)
            }

        }
        Row(modifier = Modifier.fillMaxWidth().weight(0.1f)){
            OutlinedTextField(
                value = messageToBeSent,
                onValueChange = {messageToBeSent = it},
                modifier = Modifier.weight(0.7f)
            )
            Button(
                modifier = Modifier.weight(0.3f),
                onClick = {
                    chatViewModel.sendMsg(
                        Message(
                            sender = userData.username,
                            roomID = roomID,
                            content = messageToBeSent,
                            timeStamp = LocalTime.now().toNanoOfDay()
                        )
                    )
                    messageToBeSent = ""
                },
            ){
                Text("send message")
            }
        }

    }



}