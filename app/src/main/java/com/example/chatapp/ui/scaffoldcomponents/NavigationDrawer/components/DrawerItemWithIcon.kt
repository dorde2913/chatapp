package com.example.chatapp.ui.scaffoldcomponents.NavigationDrawer.components

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
fun DrawerItemWithIcon(
    modifier: Modifier = Modifier,
    icon: @Composable()(modifier: Modifier,tint: Color)->Unit,
    label: String
){
    Row(modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically){

        icon(modifier.padding(horizontal = 16.dp).size(30.dp), Color.White)

        Text(
            text = label,
            fontSize = 16.sp,
            color = Color.White, //promeni kasnije
        )
    }
}