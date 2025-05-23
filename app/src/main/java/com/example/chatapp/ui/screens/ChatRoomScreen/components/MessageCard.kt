package com.example.chatapp.ui.screens.ChatRoomScreen.components

import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.ColorUtils
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.chatapp.data.socketio.Message
import com.example.chatapp.stateholders.ChatViewModel
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun MessageCard(
    message: Message
){
    //otherUser: Boolean,sender: String, message: String
    val viewModel: ChatViewModel = hiltViewModel()//isti kao onaj pre? vljd
    val otherUser = (viewModel.userData.username != message.sender)

    val dateTime = LocalDateTime.ofInstant(
        Instant.ofEpochSecond(message.timeStamp),
        ZoneId.systemDefault())
    val formatterHourMinute = DateTimeFormatter.ofPattern("HH:mm")
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = if (otherUser) Arrangement.Start else Arrangement.End
    ){

        val cardColors = CardColors(
            containerColor = if (otherUser) Color.Green.darken(0.3f) else Color.DarkGray,
            contentColor = Color.White,
            disabledContentColor = Color.Black,
            disabledContainerColor = Color.Black
        )

        Card(
            modifier = Modifier
                .padding(bottom = 15.dp, end = 10.dp, start = 10.dp)
                .widthIn(min = 50.dp, max = 250.dp)
                ,
            colors = cardColors
        ){
            //display displayname + username of sencder
            if (otherUser){
                Row(
                    modifier = Modifier.fillMaxWidth()
                ){
                    Text(
                        "@${message.sender}",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(horizontal = 10.dp)
                    )
                }
            }
            //message content
            Text(message.content, fontSize = 20.sp, textAlign = TextAlign.Center,
                modifier = Modifier.padding(10.dp))

            //time sent, info on whether it was received/read
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ){

                Text(
                    text = dateTime.format(formatterHourMinute),
                    color = Color.Gray,
                    modifier = Modifier.padding(horizontal = 10.dp)
                )

            }
        }

    }
}

fun Color.lighten(factor: Float): Color {
    val newColor = ColorUtils.blendARGB(this.toArgb(), Color.White.toArgb(), factor)
    return Color(newColor)
}
fun Color.darken(factor: Float): Color {
    val newColor = ColorUtils.blendARGB(this.toArgb(), Color.Black.toArgb(), factor)
    return Color(newColor)
}