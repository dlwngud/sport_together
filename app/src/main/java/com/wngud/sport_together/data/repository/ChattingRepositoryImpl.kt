package com.wngud.sport_together.data.repository

import android.util.Log
import com.google.firebase.firestore.toObjects
import com.wngud.sport_together.App
import com.wngud.sport_together.data.db.remote.UserDataSource
import com.wngud.sport_together.domain.model.Chatting
import com.wngud.sport_together.domain.model.ChattingRoom
import com.wngud.sport_together.domain.model.User
import com.wngud.sport_together.domain.repository.ChattingRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ChattingRepositoryImpl @Inject constructor(
    private val userDataSource: UserDataSource
) : ChattingRepository {
    override suspend fun sendChatting(chatting: Chatting, users: List<String>) {
        val chattingRoomRef = App.db.collection("ChattingRooms")
        val counterUId = users.find { it != App.auth.currentUser!!.uid }
        val myUId = users.find { it == App.auth.currentUser!!.uid }
        val mUser = userDataSource.getMyInfo(myUId!!).firstOrNull()!!
        val counterUser = userDataSource.getMyInfo(counterUId!!).firstOrNull()!!
        var roomId = getRoomIdOrNull(listOf(mUser, counterUser))
        val chattingRoom =
            ChattingRoom(
                users = listOf(mUser, counterUser),
                createdAt = System.currentTimeMillis()
            )
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

    override suspend fun getChatting(roomId: String): Flow<List<Chatting>> = callbackFlow {
        val subscription = App.db.collection("ChattingRooms")
            .document(roomId)
            .collection("Chattings")
            .orderBy("sentAt")
            .addSnapshotListener { querySnapshot, error ->
                if (error != null) return@addSnapshotListener
                querySnapshot?.let {
                    trySend(it.documents.map { doc ->
                        doc.toObject(Chatting::class.java)!!.copy()
                    }).isSuccess
                }
            }
        awaitClose { subscription.remove() }
    }

    override suspend fun getAllChattingRoom(): List<ChattingRoom> {
        val querySnapshot = App.db.collection("ChattingRooms").get().await()
        return querySnapshot.toObjects<ChattingRoom>()
    }

    override suspend fun getRoomIdOrNull(users: List<User>): String? {
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