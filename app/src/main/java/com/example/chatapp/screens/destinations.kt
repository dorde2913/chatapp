package com.example.chatapp.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.chatapp.R
import com.example.chatapp.stateholders.ChatUiState
import com.example.chatapp.stateholders.ChatViewModel

/*
ovaj fajl postaje malo neuredan, mozda da svaku destinaciju stavim u svoj fajl sta znam
 */
interface Destination{

    val route: String
    //iskreno ovo je bas glupo bilo, vracam na samo text
    val topBarText: String
}

interface IconDestination: Destination{
    //ne znam kako se nisam ranije setio da moze ovako
    val icon: @Composable()(Dp)->Unit
    val selectedIcon: @Composable()(Dp)->Unit
}

object LoginDestination: Destination{
    override val route: String
        get() = "login"
    override val topBarText: String
        get() = ""
}

object HomeDestination: IconDestination{
    override val icon: @Composable (Dp) -> Unit
        get() = {Icon(painter = painterResource(R.drawable.unselectedchathomeicon),null,
            modifier = Modifier.size(it))}
    override val selectedIcon: @Composable (Dp) -> Unit
        get() = {Icon(painter = painterResource(R.drawable.chathomeicon),null,
            modifier = Modifier.size(it))}

    override val route: String
        get() = "home"
    override val topBarText: String
        get() = "Chat App :D"


}

object NewChatDestination: Destination{
    override val route: String = "newchat"
    override val topBarText: String
        get() = "Create New Chat"

}

object ChatRoomDestination: Destination{
    override val route: String = "chatroom"
    override val topBarText: String
        get() = ""


}

object ContactsDestination: IconDestination{
    override val icon: @Composable (Dp) -> Unit
        get() = {Icon(Icons.Outlined.Person,null,
            modifier = Modifier.size(it))}
    override val selectedIcon: @Composable (Dp) -> Unit
        get() = {Icon(Icons.Filled.Person,null,
            modifier = Modifier.size(it))}

    override val route: String = "contacts"
    override val topBarText: String
        get() = "My Friends :D"


}

val navigationBarDestinations = listOf(HomeDestination,ContactsDestination)

val allDestinations =
    mapOf(
        LoginDestination.route to LoginDestination,
        HomeDestination.route to HomeDestination,
        NewChatDestination.route to NewChatDestination,
        ChatRoomDestination.route to ChatRoomDestination,
        ContactsDestination.route to ContactsDestination,
    )

