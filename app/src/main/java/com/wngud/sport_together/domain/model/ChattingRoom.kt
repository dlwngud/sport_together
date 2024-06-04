package com.wngud.sport_together.domain.model

data class ChattingRoom(
    val roomId: String = "",
    val users: List<User> = emptyList(),
    val lastChat: String = "",
    val createdAt: Long = 0,
    val unreadCount: Int = 0
)
