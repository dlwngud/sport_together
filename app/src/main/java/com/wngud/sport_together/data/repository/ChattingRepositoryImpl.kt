package com.wngud.sport_together.data.repository

import android.util.Log
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.toObject
import com.google.firebase.firestore.toObjects
import com.wngud.sport_together.App
import com.wngud.sport_together.domain.model.Chatting
import com.wngud.sport_together.domain.model.ChattingRoom
import com.wngud.sport_together.domain.repository.ChattingRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ChattingRepositoryImpl @Inject constructor() : ChattingRepository {
    override suspend fun sendChatting(chatting: Chatting, users: List<String>) {
        val chattingRoomRef = App.db.collection("ChattingRooms")
        var roomId = getRoomIdOrNull(users)
        val chattingRoom = ChattingRoom(users = users, createdAt = System.currentTimeMillis())
        if (roomId == null) {
            roomId = chattingRoomRef.document().id
            chattingRoomRef.document(roomId).set(chattingRoom.copy(roomId = roomId))
                .addOnSuccessListener {
                    Log.i("chatting", "room 성공")
                }
        }

        val chattingRef = chattingRoomRef.document(roomId).collection("Chattings")
        val messageId = chattingRef.document().id
        val chat = chatting.copy(
            roomId = roomId,
            messageId = messageId,
            sentAt = System.currentTimeMillis()
        )
        chattingRef.document(messageId).set(chat).addOnSuccessListener {
            chattingRoomRef.document(roomId)
                .set(chattingRoom.copy(roomId = roomId, lastChat = chat.content))
            Log.i("chatting", "chatting 성공")
        }
    }

    override suspend fun getChatting(roomId: String): Flow<Result<List<Chatting>>> = callbackFlow {
        App.db.collection("ChattingRooms")
            .document(roomId)
            .collection("Chattings")
            .orderBy("sentAt")
            .addSnapshotListener { value, error ->
                if (value != null) {
                    val chattings = mutableListOf<Chatting>()
                    value.documentChanges.forEach { doc ->
                        if (doc.type == DocumentChange.Type.ADDED) {
                            chattings.add(doc.document.toObject<Chatting>())
                        }
                    }
                    trySend(Result.success(chattings))
                } else {
                    trySend(Result.failure(Error(error?.message)))
                }
            }
        awaitClose {

        }
    }

    override suspend fun getAllChattingRoom(): List<ChattingRoom> {
        val querySnapshot = App.db.collection("ChattingRooms").get().await()
        return querySnapshot.toObjects<ChattingRoom>()
    }

    override suspend fun getRoomIdOrNull(users: List<String>): String? {
        val roomList = getAllChattingRoom()
        var roomId: String? = null
        for (room in roomList) {
            if (room.users.containsAll(users)) {
                roomId = room.roomId
                break
            }
        }
        return roomId
    }
}