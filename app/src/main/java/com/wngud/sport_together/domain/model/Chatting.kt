package com.wngud.sport_together.domain.model

data class Chatting(
    val messageId: String = "",
    val roomId: String = "",
    val senderId: String = "",
    val content: String = "",
    val sentAt: Long = 0,
    val isRead: Boolean = false
)