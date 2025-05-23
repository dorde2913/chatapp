package com.example.chatapp.ui.screens.HomeScreen.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.chatapp.R
import com.example.chatapp.data.retrofit.models.Chat

@Composable
fun ChatRow(modifier: Modifier = Modifier, chat: Chat,
            navigateToChat: (String) -> Unit){

    Row(
        modifier = modifier.fillMaxWidth()
            .height(100.dp)
            .clickable {
                navigateToChat(chat._id)
            },
        verticalAlignment = Alignment.CenterVertically
    ){

        ChatIcon(modifier = Modifier.weight(0.2f))

        ChatNameAndLastMessage(
            modifier = Modifier.weight(0.7f),
            chatName = chat.name,
            lastSender = chat.latestMessageSender?:"error",
            lastMessage = chat.latestMessage?:"error"
        )

        ChatActivity(modifier = Modifier.weight(0.1f))

    }

}

@Composable
fun ChatIcon(modifier: Modifier = Modifier){
    //treba da se doda postavljanje ikonice za chat
    Box(
        modifier = modifier.fillMaxHeight(),
        contentAlignment = Alignment.Center
    ){
        Image(
            painter = painterResource(R.drawable.ic_launcher_background),
            null,
            modifier = Modifier.padding(10.dp)
                .clip(CircleShape)
        )
    }
}

@Composable
fun ChatNameAndLastMessage(modifier: Modifier = Modifier,
                           chatName: String,
                           lastMessage: String,
                           lastSender: String){
    Column(
        modifier = modifier.fillMaxSize()
            .padding(10.dp),
    ){
        Text(
            text = chatName,
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text = "$lastSender: $lastMessage"
        )
    }
}

@Composable
fun ChatActivity(modifier: Modifier = Modifier){



}
