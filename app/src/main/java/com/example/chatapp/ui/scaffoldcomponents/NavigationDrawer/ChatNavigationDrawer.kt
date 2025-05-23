package com.example.chatapp.ui.scaffoldcomponents.NavigationDrawer

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ExitToApp
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.ExitToApp
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.edit
import com.example.chatapp.AUTH_TOKEN
import com.example.chatapp.R
import com.example.chatapp.dataStore
import com.example.chatapp.stateholders.UserData
import com.example.chatapp.ui.scaffoldcomponents.NavigationDrawer.components.CurrentUserDrawerItem
import com.example.chatapp.ui.scaffoldcomponents.NavigationDrawer.components.DrawerItemWithIcon
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@Composable
fun ChatNavigationDrawer(
    modifier: Modifier = Modifier,
    userData: UserData,
    navigateToProfile: ()->Unit,
    closeDrawer: ()->Unit,
    navigateToLogin: ()->Unit
){
    val context = LocalContext.current
    BackHandler {
        closeDrawer() //ne znam sto ovo nije podrazumevano
    }

    Text(
        modifier = modifier,
        text = "ChatApp",
        textAlign = TextAlign.Center,
        fontSize = 25.sp,
        fontWeight = FontWeight.SemiBold
    )

    HorizontalDivider(modifier = modifier)

    CurrentUserDrawerItem(modifier,userData)


    NavigationDrawerItem(
        label = {
            DrawerItemWithIcon(
                icon = { modifier, tint ->
                    Icon(Icons.Outlined.AccountCircle,null,tint = tint, modifier = modifier)
                },
                label = "Profile"
            )
        },
        selected = false,
        onClick = {
            navigateToProfile()
            closeDrawer()
        }
    )

    NavigationDrawerItem(
        label = { DrawerItemWithIcon(
            icon = { modifier, tint ->
                Icon(Icons.AutoMirrored.Outlined.ExitToApp,null,tint = tint, modifier = modifier)
            },
            label = "Sign out"
        )},
        selected = false,
        onClick = {
            CoroutineScope(Dispatchers.IO).launch {
                context.dataStore.edit {
                    it.remove(AUTH_TOKEN)
                }
            }
            navigateToLogin()
            closeDrawer()
        }
    )


}

