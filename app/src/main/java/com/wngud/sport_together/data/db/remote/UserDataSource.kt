package com.wngud.sport_together.data.db.remote

import android.util.Log
import com.kakao.sdk.user.Constants
import com.wngud.sport_together.App
import com.wngud.sport_together.domain.model.User
import kotlinx.coroutines.tasks.await

class UserDataSource {
    suspend fun getUserInfo(uid: String): MutableList<User> {
        val querySnapshot = App.db.collection("users").whereEqualTo("uid", uid).get().await()
        return querySnapshot.toObjects(User::class.java)
    }

    suspend fun saveUserInfo(user: User) {
        App.db.collection("users")
            .document(user.uid)
            .set(user)
            .addOnSuccessListener {
                Log.d(Constants.TAG, "DocumentSnapshot successfully written!")
            }
            .addOnFailureListener { e ->
                Log.w(
                    Constants.TAG,
                    "Error writing document",
                    e
                )
            }
    }
}