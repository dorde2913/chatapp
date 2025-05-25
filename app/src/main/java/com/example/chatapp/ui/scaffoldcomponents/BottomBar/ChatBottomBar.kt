package com.example.chatapp.ui.scaffoldcomponents.BottomBar

import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import com.example.chatapp.ui.screens.LoginDestination
import com.example.chatapp.ui.screens.bottomNavigationRoutes
import com.example.chatapp.ui.screens.navigationBarDestinations

@Composable
fun ChatBottomBar(
    currentDestination: NavDestination?,
    onNavBarItemClick: (String) -> Unit
){
    if (currentDestination?.route !in bottomNavigationRoutes) return

    NavigationBar{
        navigationBarDestinations.forEach { destination ->
            val selected = currentDestination?.route?.startsWith(destination.route) ?: false

            NavigationBarItem(
                selected = selected,
                label = {Text(text = destination.label)},
                icon = {
                    if (selected) destination.selectedIcon(40.dp) else destination.icon(40.dp)
                },
                onClick = {
                    onNavBarItemClick(destination.route)
                },
                colors = NavigationBarItemDefaults.colors().copy(
                    selectedIndicatorColor = NavigationBarDefaults.containerColor)
            )
        }
    }
}