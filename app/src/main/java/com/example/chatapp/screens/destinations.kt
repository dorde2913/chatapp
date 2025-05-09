package com.example.chatapp.screens

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.chatapp.R

interface Destination{
    val route: String
    val topBarText: Int?
}

interface IconDestination: Destination{
    //ne znam kako se nisam ranije setio da moze ovako
    val icon: @Composable()(Dp)->Unit
    val selectedIcon: @Composable()(Dp)->Unit
}

object LoginDestination: Destination{
    override val route: String
        get() = "login"
    override val topBarText: Int?
        get() = null
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
    override val topBarText: Int
        get() = R.string.home_topbar
}

object NewChatDestination: Destination{
    override val route: String = "newchat"
    override val topBarText = 0
}

object ChatRoomDestination: Destination{
    override val route: String = "chatroom"
    override val topBarText = 0
}

object ContactsDestination: IconDestination{
    override val icon: @Composable (Dp) -> Unit
        get() = {Icon(Icons.Outlined.Person,null,
            modifier = Modifier.size(it))}
    override val selectedIcon: @Composable (Dp) -> Unit
        get() = {Icon(Icons.Filled.Person,null,
            modifier = Modifier.size(it))}

    override val route: String = "contacts"
    override val topBarText = 0
}

val navigationBarDestinations = listOf(HomeDestination,ContactsDestination )
