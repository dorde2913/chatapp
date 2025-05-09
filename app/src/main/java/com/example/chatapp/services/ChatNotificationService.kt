package com.example.chatapp.services

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class ChatNotificationService: FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        //save in db
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        println(message.notification?.title)
        
        
    }
}