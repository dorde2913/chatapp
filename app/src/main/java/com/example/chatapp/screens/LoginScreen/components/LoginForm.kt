package com.example.chatapp.screens.LoginScreen.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.chatapp.AUTH_TOKEN
import com.example.chatapp.R
import com.example.chatapp.data.retrofit.ErrorType
import com.example.chatapp.dataStore

import com.example.chatapp.stateholders.AuthViewModel

import kotlinx.coroutines.flow.map


@Composable
fun LoginForm(
    navigateToHome: ()->Unit,
    viewModel: AuthViewModel
){

    val context = LocalContext.current
    val auth_token by context.dataStore.data.map{it[AUTH_TOKEN]}.collectAsState(initial = null)


    val waiting by viewModel.waiting.collectAsStateWithLifecycle()
    val error by viewModel.error.collectAsStateWithLifecycle()

    val uiState by viewModel.loginState.collectAsStateWithLifecycle()

    LaunchedEffect(waiting,auth_token){
        if (!waiting && auth_token!=null){
            if (auth_token != ""){
                navigateToHome()
            }
        }
    }

    println(error)

    Column(
        modifier = Modifier.fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){

        OutlinedTextField(
            value = uiState.username,
            onValueChange = {viewModel.setLoginUsername(it)},
            label = {
                Text(text = stringResource(R.string.login_label))
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = null
                )
            },
            isError = (error == ErrorType.UNKNOWN_USER.value),

            supportingText = {
                if (error == ErrorType.UNKNOWN_USER.value){
                    Text(text = stringResource(R.string.username_unknown_error))
                }
            }
        )

        OutlinedTextField(
            value = uiState.password,
            onValueChange = {viewModel.setLoginPassword(it)},
            label = {
                Text(text = stringResource(R.string.password_label))
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = null
                )
            },
            visualTransformation = PasswordVisualTransformation(),
            isError = (error == ErrorType.BAD_PASSWORD.value),
            supportingText = {
                if (error == ErrorType.BAD_PASSWORD.value){
                    Text(text = stringResource(R.string.bad_password_error))
                }
            }
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                viewModel.login()
            },
            enabled = (!waiting && uiState.password!="")
        ) {
            if (!waiting)
                Text(
                    text = stringResource(R.string.login_selection)
                )
            else CircularProgressIndicator()
        }

    }

}