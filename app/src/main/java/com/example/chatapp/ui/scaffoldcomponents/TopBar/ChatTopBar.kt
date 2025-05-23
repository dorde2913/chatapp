package com.example.chatapp.ui.scaffoldcomponents.TopBar

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination
import com.example.chatapp.stateholders.AuthViewModel
import com.example.chatapp.ui.screens.ChatRoomDestination
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun ChatTopBar(
    currentDestination: NavDestination?,
    drawerState: DrawerState,
    scope: CoroutineScope,
    viewModel: AuthViewModel
){
    if (currentDestination?.route==null) return

    /*
    default topbar je drawer expand dugme + searchbar za grupe/kontakte
     */

    if (!currentDestination.route!!.startsWith(ChatRoomDestination.route))
//        TopBarRow(
//            drawerState = drawerState,
//            coroutineScope = scope
//        ) {
//            TopBarText(
//                text = allDestinations[currentDestination.route]!!.topBarText,
//                modifier = Modifier.fillMaxWidth()
//            )
//        }
        TopSearchBar(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp).padding(top = 30.dp),
            coroutineScope = scope,
            drawerState = drawerState,
            searchBarLabel = "Search contacts/chats"
        )
    else{

        val chat by viewModel.chat.collectAsState()
        TopBarRow(
            drawerState = drawerState,
            coroutineScope = scope
        ) {
            TopBarText(
                text = chat.name,
                modifier = Modifier.fillMaxWidth()
            )
        }

    }
}

@Composable
fun TopSearchBar(
    modifier: Modifier = Modifier,
    coroutineScope: CoroutineScope,
    drawerState: DrawerState,
    searchBarLabel: String
){
    var searchValue by rememberSaveable { mutableStateOf("") }
    OutlinedTextField(
        value = searchValue,
        onValueChange = {searchValue = it},
        label = {Text(text = searchBarLabel)},
        leadingIcon = {
            IconButton(
                onClick = {
                    coroutineScope.launch {
                        drawerState.apply {
                            if (isClosed) open() else close()
                        }
                    }
                }
            ) {
                Icon(
                    Icons.Default.Menu,
                    null
                )
            }
        },
        shape = RoundedCornerShape(50.dp),
        modifier = modifier
    )

}

@Composable
fun TopBarRow(coroutineScope: CoroutineScope, drawerState: DrawerState, content: @Composable()()->Unit){
    Row(
        modifier = Modifier.fillMaxWidth()
            .height(85.dp).padding(bottom = 10.dp),
        verticalAlignment = Alignment.Bottom,
    ){
        IconButton(
            onClick = {
                coroutineScope.launch {
                    drawerState.apply {
                        if (isClosed) open() else close()
                    }
                }
            }
        ) {
            Icon(
                Icons.Default.Menu,
                null
            )
        }
        content()
    }
}

@Composable
fun TopBarText(modifier: Modifier = Modifier, text: String){
    Text(
        text = text,
        fontSize = 25.sp,
        fontWeight = FontWeight.SemiBold,
        textAlign = TextAlign.Center,
        modifier = modifier
    )
}