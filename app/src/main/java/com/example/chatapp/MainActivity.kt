package com.example.chatapp

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.byteArrayPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.chatapp.stateholders.AuthViewModel
import com.example.chatapp.ui.scaffoldcomponents.BottomBar.ChatBottomBar
import com.example.chatapp.ui.scaffoldcomponents.NavigationDrawer.ChatNavigationDrawer
import com.example.chatapp.ui.scaffoldcomponents.TopBar.ChatTopBar
import com.example.chatapp.ui.screens.ChatRoomDestination
import com.example.chatapp.ui.screens.ChatRoomScreen.ChatRoom
import com.example.chatapp.ui.screens.ContactsDestination
import com.example.chatapp.ui.screens.ContactsScreen.ContactsScreen
import com.example.chatapp.ui.screens.HomeDestination
import com.example.chatapp.ui.screens.HomeScreen.HomeScreen
import com.example.chatapp.ui.screens.LoginDestination
import com.example.chatapp.ui.screens.LoginScreen.LoginScreen
import com.example.chatapp.ui.screens.NewChatDestination
import com.example.chatapp.ui.screens.NewChatScreen.NewChatScreen
import com.example.chatapp.ui.screens.ProfileDestination
import com.example.chatapp.ui.screens.ProfileScreen.ProfileScreen
import com.example.chatapp.ui.theme.ChatAppTheme
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.ktx.messaging
import com.google.firebase.messaging.messaging
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


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
        navController.navigateSingleTop(
            "${ChatRoomDestination.route}/$it"
        )
    }
    val navigateToHome = {
        navController.navigateSingleTop(HomeDestination.route)
    }

    val navigateToLogin = {
        navController.navigateSingleTop(LoginDestination.route)
    }



    /*
        Ovaj viewmodel realno koristimo u ekranu za login ali
         zelimo da inicijalizujemo chat repository pre nego sto nam zatreba u home ekranu
         tkd tu garantovano imamo pristup tokenu i user data

         mozda nije najbolje resenje
     */
    val authViewModel: AuthViewModel = hiltViewModel()

    LaunchedEffect(Unit) {
        //ovo mozda kasnije pomerim
        val token = FirebaseMessaging.getInstance().token.await()
        authViewModel.sendFCMToken(token)

    }

    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)


    /*
    ovo nam za ovakvu aplikaciju realno ne treba ali hteo sam da vidim
    kako se ovo implementira :)
     */
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                val userData by authViewModel.userData.collectAsStateWithLifecycle()
                ChatNavigationDrawer(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).padding(bottom = 10.dp),
                    userData = userData,
                    navigateToProfile = {navController.navigateSingleTop(ProfileDestination.route)},
                    closeDrawer = {
                        scope.launch {
                            drawerState.apply {
                                if (isClosed) open() else close()
                            }
                        }
                    },
                    navigateToLogin = navigateToLogin
                )
            }
        },
    ) {


        Scaffold(
            modifier = Modifier.fillMaxSize(),

            bottomBar = {
                ChatBottomBar(
                    currentDestination = currentDestination,
                    onNavBarItemClick = { route ->
                        navController.navigateSingleTop(route)
                    }
                )
            },

            topBar = {
                ChatTopBar(
                    currentDestination = currentDestination,
                    drawerState = drawerState,
                    scope = scope,
                    viewModel = authViewModel,
                    popBackStack = {
                        navController.popBackStack()
                    }
                )

            },

            floatingActionButton = {
                if (currentDestination?.route == HomeDestination.route){
                    FloatingActionButton(
                        onClick = {

                            navController.navigateSingleTop(
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
                        navigateToChat = navigateToChat
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


                composable(
                    route = ProfileDestination.route
                ){
                    ProfileScreen()
                }
            }


        }



    }
}

fun NavController.navigateSingleTop(route: String){
    this.navigate(route){
        launchSingleTop = true
    }
}