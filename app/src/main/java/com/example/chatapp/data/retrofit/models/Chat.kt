package com.example.chatapp.data.retrofit.models

data class Chat (
    val name: String = "",
    val isGroup: Boolean = false,
    val participants: List<String> = listOf(),
    val latestMessage: String? = "",
    val latestActivity: Long? = 0L,
    val latestMessageSender: String? = "",
    val _id: String
)

