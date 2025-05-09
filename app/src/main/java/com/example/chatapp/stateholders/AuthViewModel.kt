package com.example.chatapp.stateholders

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapp.AUTH_TOKEN
import com.example.chatapp.USER_DATA
import com.example.chatapp.data.repositories.AuthRepository
import com.example.chatapp.data.retrofit.ErrorType
import com.example.chatapp.dataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject

data class LoginUiState(
    val username: String = "",
    val password: String = "",
)
data class RegisterUiState(
    val username: String = "",
    val password: String = "",
    val confirmPassword: String = ""
)

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val authRepository: AuthRepository,
    @ApplicationContext private val context: Context
): ViewModel() {

    val _loginState = MutableStateFlow(LoginUiState())
    val loginState = _loginState.asStateFlow()

    val _registerState = MutableStateFlow(RegisterUiState())
    val registerState = _registerState.asStateFlow()


    val _waiting = MutableStateFlow(false)
    val waiting = _waiting.asStateFlow()

    val _error = MutableStateFlow(ErrorType.NO_ERROR.value)
    val error = _error.asStateFlow()

    fun setWaiting(value: Boolean){
        _waiting.value = value
    }

    fun setLoginUsername(username: String){
        _loginState.value = _loginState.value.copy(username = username)
    }
    fun setLoginPassword(password: String){
        _loginState.value = _loginState.value.copy(password = password)
    }

    fun setRegisterUsername(username: String) {
        _registerState.value = _registerState.value.copy(username = username)
    }


    fun setRegisterPassword(password: String) {
        _registerState.value = _registerState.value.copy(password = password)
    }

    fun setConfirmPassword(confirmPassword: String) {
        _registerState.value = _registerState.value.copy(confirmPassword = confirmPassword)
    }


    fun login() = viewModelScope.launch {
        setWaiting(true)

        val username = _loginState.value.username
        val password = _loginState.value.password

        val retval = authRepository.login(username,password)

        context.dataStore.edit {preferences ->
            preferences[AUTH_TOKEN] = retval.token

            preferences[USER_DATA] = Json.encodeToString(UserData(
                username = retval.username,
                displayName = retval.displayname,
                friends = retval.friends,
                chatRooms = retval.chatRooms
            )).toByteArray()

        }
        _error.value = retval.error
        println(retval)
        println(retval.error)

        setWaiting(false)
    }

    fun register()= viewModelScope.launch {
        setWaiting(true)

        val username = _registerState.value.username
        val password = _registerState.value.password

        val retval = authRepository.register(username,password)

        context.dataStore.edit {preferences ->
            preferences[AUTH_TOKEN] = retval.token
        }
        _error.value = retval.error
        println(retval)
        println(retval.error)

        setWaiting(false)

    }
}

@Serializable
data class UserData(
    val username: String = "",
    val displayName: String = "",
    val chatRooms: List<String>?= null,
    val friends: List<String>?=null
)
