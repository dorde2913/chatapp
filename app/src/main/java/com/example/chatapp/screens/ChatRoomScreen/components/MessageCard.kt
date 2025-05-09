package com.example.chatapp.screens.ChatRoomScreen.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MessageCard(otherUser: Boolean,sender: String, message: String){
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = if (otherUser) Arrangement.Start else Arrangement.End
    ){

        val cardColors = CardColors(
            containerColor = if (otherUser) Color.Black else Color.DarkGray,
            contentColor = Color.White,
            disabledContentColor = Color.Black,
            disabledContainerColor = Color.Black
        )

        Card(
            modifier = Modifier.padding(bottom = 15.dp, end = 10.dp)
                .widthIn(min = 50.dp, max = 250.dp)
                ,
            colors = cardColors
        ){
            Text("$sender : $message", fontSize = 20.sp, textAlign = TextAlign.Center,
                modifier = Modifier.padding(10.dp))
        }

    }
}