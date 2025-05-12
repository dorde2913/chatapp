package com.example.chatapp.screens.HomeScreen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.core.edit

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.chatapp.AUTH_TOKEN
import com.example.chatapp.dataStore
import com.example.chatapp.screens.HomeScreen.components.ChatRow

import com.example.chatapp.stateholders.HomeViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@Composable
fun HomeScreen(
    navigateToChat: (String) ->Unit,
    navigateToLogin: () -> Unit
){

    val context = LocalContext.current

    val viewModel: HomeViewModel = hiltViewModel()


   // Text(text = "${chatRooms.size}")text = Firebase.messaging.token.await()

    val chatRooms by viewModel.chatRooms.collectAsStateWithLifecycle(initialValue = listOf())

    LaunchedEffect(Unit) {
        viewModel.loadChats()//za sad najbolje sto sam mogao da smislim
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        //ovde su razliciti chatovi
        item{
            Button(
                onClick = {
                    CoroutineScope(Dispatchers.IO).launch {
                        context.dataStore.edit {
                            it.remove(AUTH_TOKEN)
                        }
                    }
                    navigateToLogin()
                }
            ){
                Text("sign out")
            }
        }
        items(chatRooms){ chat ->

            ChatRow(chat = chat, navigateToChat = navigateToChat)

        }
        item{
            if (chatRooms.isEmpty()){
                CircularProgressIndicator(
                    modifier = Modifier.size(40.dp)
                )
            }
        }
    }


}