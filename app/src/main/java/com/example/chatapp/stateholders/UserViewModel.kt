package com.example.chatapp.stateholders

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.chatapp.data.repositories.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject


/*
za menjanje korisnickih podataka
 */
@HiltViewModel
class UserViewmodel @Inject constructor(
    @ApplicationContext val context: Context,
    val userRepository: UserRepository
): ViewModel() {

    val authToken = userRepository.authToken
    val userData = userRepository.userData

    suspend fun setProfilePic(pic: ByteArray,username: String) =
        userRepository.setProfilePic(pic,username)
}