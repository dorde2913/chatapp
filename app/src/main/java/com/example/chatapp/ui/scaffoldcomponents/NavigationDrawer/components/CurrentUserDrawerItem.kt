package com.example.chatapp.ui.scaffoldcomponents.NavigationDrawer.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.chatapp.R
import com.example.chatapp.data.retrofit.BASE_URL
import com.example.chatapp.stateholders.UserData

@Composable
fun CurrentUserDrawerItem(
    modifier : Modifier = Modifier,
    userData: UserData
){
    Text(
        modifier = modifier,
        text = "Current User",
        textAlign = TextAlign.Center,
        fontWeight = FontWeight.Light
    )

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center
    ){
        AsyncImage(
            BASE_URL + "user/pfp/${userData.pfpUrl}",
            null,
            modifier = Modifier
                .clip(CircleShape)
                .size(75.dp),
            clipToBounds = true
        )
    }


    Text(
        modifier = Modifier.fillMaxWidth(),
        text = userData.displayName,
        textAlign = TextAlign.Center,
        fontSize = 20.sp,
        fontWeight = FontWeight.SemiBold
    )

    Text(
        modifier = modifier,
        text = "@${userData.username}",
        textAlign = TextAlign.Center
    )
}