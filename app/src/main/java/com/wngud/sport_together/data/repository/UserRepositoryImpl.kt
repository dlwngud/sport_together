package com.wngud.sport_together.data.repository

import android.util.Log
import com.google.firebase.firestore.toObject
import com.kakao.sdk.user.Constants.TAG
import com.wngud.sport_together.App
import com.wngud.sport_together.domain.model.User
import com.wngud.sport_together.domain.repository.UserRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(): UserRepository {
    override suspend fun getUserInfo(uid: String): User {
        val docRef = App.db.collection("users").document(uid)
        val documentSnapshot = docRef.get().await()  // 비동기 작업 기다리기
        return documentSnapshot.toObject<User>()!!
    }
}