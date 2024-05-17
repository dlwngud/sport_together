package com.wngud.sport_together.domain.repository

import com.wngud.sport_together.domain.model.Chatting
import com.wngud.sport_together.domain.model.ChattingRoom
import kotlinx.coroutines.flow.Flow

interface ChattingRepository {
    suspend fun sendChatting(chatting: Chatting, users: List<String>)

    suspend fun getChatting(roomId: String): Flow<Result<List<Chatting>>>

    suspend fun getAllChattingRoom(): List<ChattingRoom>

    suspend fun getRoomIdOrNull(users: List<String>): String?
}