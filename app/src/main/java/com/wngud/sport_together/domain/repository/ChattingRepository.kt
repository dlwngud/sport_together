package com.wngud.sport_together.domain.repository

import com.wngud.sport_together.domain.model.Chatting
import com.wngud.sport_together.domain.model.ChattingRoom
import com.wngud.sport_together.domain.model.User
import kotlinx.coroutines.flow.Flow

interface ChattingRepository {
    suspend fun sendChatting(chatting: Chatting, users: List<String>)

    suspend fun getChatting(roomId: String): Flow<List<Chatting>>

    suspend fun getAllChattingRoom(): List<ChattingRoom>

    suspend fun getRoomIdOrNull(users: List<User>): String?
}