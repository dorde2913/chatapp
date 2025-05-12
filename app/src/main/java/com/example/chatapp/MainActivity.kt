package com.example.chatapp

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.byteArrayPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.chatapp.screens.ChatRoomDestination
import com.example.chatapp.screens.ChatRoomScreen.ChatRoom
import com.example.chatapp.screens.ContactsDestination
import com.example.chatapp.screens.ContactsScreen.ContactsScreen
import com.example.chatapp.screens.HomeDestination
import com.example.chatapp.screens.HomeScreen.HomeScreen
import com.example.chatapp.screens.LoginDestination
import com.example.chatapp.screens.LoginScreen.LoginScreen
import com.example.chatapp.screens.NewChatDestination
import com.example.chatapp.screens.NewChatScreen.NewChatScreen
import com.example.chatapp.screens.allDestinations
import com.example.chatapp.screens.navigationBarDestinations
import com.example.chatapp.stateholders.AuthViewModel
import com.example.chatapp.ui.theme.ChatAppTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.map


val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_info")

val AUTH_TOKEN = stringPreferencesKey("auth_token")
val USER_DATA = byteArrayPreferencesKey("user_data")

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()



        setContent {
            ChatAppTheme {
                ChatApp()
            }
        }
    }
}

@Composable
fun ChatApp(){
    val context = LocalContext.current
    val auth_token by context.dataStore.data.map{it[AUTH_TOKEN]}.collectAsState(initial = "loading")
    val navController = rememberNavController()



    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStackEntry?.destination


    //val viewModel: LoginViewModel = viewModel()

    val navigateToChat: (String) ->Unit = {
        navController.navigate(
            "${ChatRoomDestination.route}/$it"
        )
    }
    val navigateToHome = {
        navController.navigate(HomeDestination.route)
    }

    /*
        Ovaj viewmodel realno koristimo u ekranu za login ali
         zelimo da inicijalizujemo chat repository pre nego sto nam zatreba u home ekranu
         tkd tu garantovano imamo pristup tokenu i user data

         mozda nije najbolje resenje
     */
    val authViewModel: AuthViewModel = hiltViewModel()


    println(currentDestination?.route)
    Scaffold(
        modifier = Modifier.fillMaxSize(),

        bottomBar = {
            if (currentDestination?.route == LoginDestination.route) return@Scaffold

            NavigationBar{
                navigationBarDestinations.forEach {destination ->
                    val selected = currentDestination?.route?.startsWith(destination.route) ?: false

                    NavigationBarItem(
                        selected = selected,
                        label = {},
                        icon = {
                            if (selected) destination.selectedIcon(40.dp) else destination.icon(40.dp)
                        },
                        onClick = {
                            navController.navigate(destination.route)
                        },
                        colors = NavigationBarItemDefaults.colors().copy(
                            selectedIndicatorColor = NavigationBarDefaults.containerColor)
                    )
                }
            }
        },

        topBar = {
            if (currentDestination?.route==null ||
                currentDestination.route!!.startsWith(ChatRoomDestination.route)) return@Scaffold

            //treba da se doda za chatroom ekran
            TopBarRow {
                TopBarText(
                    text = allDestinations[currentDestination.route]!!.topBarText,
                    modifier = Modifier.fillMaxWidth()
                )
            }


        },

        floatingActionButton = {
            if (currentDestination?.route == HomeDestination.route){
                FloatingActionButton(
                    onClick = {

                        navController.navigate(
                            NewChatDestination.route
                        )

                    }
                ) {
                    Icon(Icons.Default.Add,null)
                }
            }
        }



        ){ innerPadding ->


        if (auth_token == "loading"){
            CircularProgressIndicator(
                modifier = Modifier.size(50.dp)
            )
            return@Scaffold
        }


        NavHost(
            modifier = Modifier.padding(innerPadding),
            navController = navController,
            startDestination = if (auth_token != null && auth_token != "loading") HomeDestination.route
                                else LoginDestination.route
        ){
            composable(route = LoginDestination.route){
                LoginScreen(navigateToHome = navigateToHome, viewModel = authViewModel)
            }

            composable(route = HomeDestination.route){
                HomeScreen(
                    navigateToChat = navigateToChat,
                    navigateToLogin = {
                        navController.navigate(LoginDestination.route)
                    }
                )
            }

            composable(route = NewChatDestination.route){
                NewChatScreen()
            }

            composable(
                route = "${ChatRoomDestination.route}/{roomID}",
                arguments = listOf(navArgument("roomID"){type = NavType.StringType})
            ){navBackStackEntry ->
                val roomID = navBackStackEntry.arguments?.getString("roomID")?:""

                ChatRoom(roomID)
            }

            composable(
                route = ContactsDestination.route
            ){
                ContactsScreen()
            }
        }


    }
}
@Composable
fun TopBarRow(content: @Composable()()->Unit){
    Row(
        modifier = Modifier.fillMaxWidth()
            .height(85.dp).padding(bottom = 10.dp),
        verticalAlignment = Alignment.Bottom,
    ){
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