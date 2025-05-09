package com.example.chatapp.screens.NewChatScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.chatapp.stateholders.ChatViewModel

@Composable
fun NewChatScreen(){

    val viewModel: ChatViewModel = hiltViewModel()

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {


        var newChatName by remember{mutableStateOf("")}
        OutlinedTextField(
            value = newChatName,
            onValueChange = {newChatName = it},
            label = {
                Text("New Chat Name")
            }
        )

        Button(
            onClick = {
                viewModel.createRoom(
                    name= newChatName,
                    isGroup = true,
                    participants = listOf("dorde","Test")
                )
            }
        ){
            Text("create")
        }

    }


}