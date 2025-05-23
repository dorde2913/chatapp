package com.example.chatapp.ui.screens.ProfileScreen.components

import androidx.compose.foundation.content.MediaType.Companion.Text
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ProfileInfoRow(
    modifier: Modifier = Modifier,
    icon: @Composable()(modifier: Modifier, tint: Color)->Unit,
    label: String,
    value: String
){
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ){

        icon(Modifier.padding(horizontal = 20.dp).size(30.dp), Color.Gray)


        Column{
            Text(
                text = label,
                fontSize = 19.sp,
                color = Color.White
            )

            Text(
                text = value,
                fontSize = 18.sp,
                color = Color.Gray
            )
        }
    }
}