package com.wngud.sport_together.domain.model

data class ChattingRoom(
    val roomId: String = "",
    val users: List<String> = emptyList(),
    val lastChat: String = "",
    val createdAt: Long = 0,
    val unreadCount: Int = 0,
    val nickname: String = "",
    val profileImage: String = ""
)
