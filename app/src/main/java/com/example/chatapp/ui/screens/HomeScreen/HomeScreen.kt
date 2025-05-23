package com.example.chatapp.ui.screens.HomeScreen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox

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
import com.example.chatapp.ui.screens.HomeScreen.components.ChatRow

import com.example.chatapp.stateholders.HomeViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navigateToChat: (String) ->Unit
){

    val context = LocalContext.current

    val viewModel: HomeViewModel = hiltViewModel()


   // Text(text = "${chatRooms.size}")text = Firebase.messaging.token.await()

    val chatRooms by viewModel.chatRooms.collectAsStateWithLifecycle(initialValue = listOf())

    val connectionFailed by viewModel.connectionFailed.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.loadChats()//za sad najbolje sto sam mogao da smislim
    }

    PullToRefreshBox(
        isRefreshing = (chatRooms.isEmpty() && !connectionFailed),
        onRefresh = {
            viewModel.refresh()
            viewModel.loadChats()
        }
    ) {

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            //ovde su razliciti chatovi

            items(chatRooms){ chat ->
                ChatRow(chat = chat, navigateToChat = navigateToChat)
            }

            item{
                if (connectionFailed && chatRooms.isEmpty()){
                    Text("Connection failed, swipe down to refresh", modifier = Modifier.padding(top = 150.dp))
                }
            }
        }



    }



}