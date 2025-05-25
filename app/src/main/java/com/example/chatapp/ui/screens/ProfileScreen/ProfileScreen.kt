package com.example.chatapp.ui.screens.ProfileScreen

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.MailOutline
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.chatapp.R
import com.example.chatapp.data.retrofit.BASE_URL
import com.example.chatapp.stateholders.UserData
import com.example.chatapp.stateholders.UserViewmodel
import com.example.chatapp.ui.screens.ChatRoomScreen.components.darken
import com.example.chatapp.ui.screens.ProfileScreen.components.ProfileInfoRow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream


@Composable
fun ProfileScreen(){

    val viewModel: UserViewmodel = hiltViewModel()

    val userData by viewModel.userData.collectAsStateWithLifecycle()



    val context = LocalContext.current

    val pickedImage = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) {uri ->
        if (uri!=null){
            Log.d("Photo","image: $uri")
            CoroutineScope(Dispatchers.IO).launch {
                val compressed = compressImageFromUri(context, uri)
                if (compressed!=null){
                    viewModel.setProfilePic(compressed,userData.username)
                }
            }
        }
        else Log.d("Photo","none")
    }

    LaunchedEffect(userData) {
        Log.d("Profile Screen",userData.pfpUrl)
    }


    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
        ){

        Box(
            modifier = Modifier.padding(top = 24.dp)
        ){


            AsyncImage(
                BASE_URL + "user/pfp/${userData.pfpUrl}",
                null,
                modifier = Modifier
                    .clip(CircleShape)
                    .size(150.dp)
                    .align(Alignment.Center),
                clipToBounds = true
            )


            IconButton(
                onClick = {
                    pickedImage.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                },
                modifier = Modifier
                    .size(48.dp)
                    .align(Alignment.BottomEnd),
                    //.padding(top = 20.dp, start = 20.dp),
                colors = IconButtonColors(
                    containerColor = Color.Green.darken(0.5f),
                    contentColor = Color.DarkGray,
                    disabledContentColor =Color.DarkGray,
                    disabledContainerColor =  Color.Green.darken(0.5f)
                )
            ) {
                Icon(Icons.Filled.Edit,null)
            }
        }

        /*
        display name i about treba da budu editable, treba da se napravi bolja ikonica za username,
        taj editing na backendu da se uradi na slican nacin kao set pfp
         */

        ProfileInfoRow(
            modifier = Modifier.padding(top = 32.dp, bottom = 16.dp),
            icon = { modifier,tint ->
                Icon(Icons.Outlined.Person,null,tint = tint,modifier = modifier)
            },
            label = "Displayed Name",
            value = userData.displayName,
            editable = true,
            updateData = {
                viewModel.setDisplayName(it)
            }
        )

        ProfileInfoRow(
            modifier = Modifier.padding(vertical = 16.dp),
            icon = { modifier,tint ->
                //ova ikonica je placeholder
                Icon(Icons.Outlined.MailOutline,null,tint = tint,modifier = modifier)
            },
            label = "Username",
            editable = false,
            value = "@${userData.username}",
        )

        ProfileInfoRow(
            modifier = Modifier.padding(vertical = 16.dp),
            icon = { modifier,tint ->
                Icon(Icons.Outlined.Info,null,tint = tint,modifier = modifier)
            },
            label = "About",
            value = userData.about,
            editable = true,
            updateData = {
                viewModel.setAbout(it)
            }
        )


    }
}

suspend fun compressImageFromUri(
    context: Context,
    uri: Uri,
    maxSizeInBytes: Int = 520 * 520,
    format: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG
): ByteArray? = withContext(Dispatchers.IO) {
    try {
        val inputStream = context.contentResolver.openInputStream(uri) ?: return@withContext null
        val originalBitmap = BitmapFactory.decodeStream(inputStream)

        var quality = 100
        var byteArrayOutputStream: ByteArrayOutputStream

        do {
            byteArrayOutputStream = ByteArrayOutputStream()
            originalBitmap.compress(format, quality, byteArrayOutputStream)
            quality -= 10
        } while (byteArrayOutputStream.size() > maxSizeInBytes)

        byteArrayOutputStream.toByteArray()
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}