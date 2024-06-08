package com.wngud.sport_together.data.db.remote

import android.net.Uri
import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.toObject
import com.kakao.sdk.user.Constants
import com.wngud.sport_together.App
import com.wngud.sport_together.domain.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class UserDataSource {
    fun getMyInfo(uid: String) = callbackFlow {
        App.db.collection("users").document(uid).addSnapshotListener { snapshot, e ->
            if (e != null) {
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {
                val data = snapshot.data
                val email = data?.get("email").toString()
                val uid = data?.get("uid").toString()
                val introduce = data?.get("introduce").toString()
                val nickname = data?.get("nickname").toString()
                val profileImage = data?.get("profileImage").toString()
                val follower = data?.get("follower") as? List<String> ?: emptyList()
                val following = data?.get("following") as? List<String> ?: emptyList()
                Log.i("tag", "data " + follower)
                trySend(
                    User(
                        uid = uid,
                        email = email,
                        introduce = introduce,
                        nickname = nickname,
                        profileImage = profileImage,
                        follower = follower,
                        following = following
                    )
                )
            }
        }
        awaitClose { }
    }

    suspend fun getUserInfo(uid: String): User {
        val docSnap = App.db.collection("users").document(uid).get().await()
        if(docSnap.exists()) {
            return docSnap.toObject<User>() ?: throw IllegalStateException("User deserialization failed")
        } else {
            throw NoSuchElementException("No user found with uid: $uid")
        }
    }



    suspend fun saveUserInfo(user: User) {
        withContext(Dispatchers.IO) {
            App.db.collection("users")
                .document(App.auth.currentUser!!.uid)
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

            val reviewRef = App.db.collection("reviews")
            reviewRef.whereEqualTo("uid", App.auth.currentUser!!.uid).get().addOnSuccessListener {
                for (doc in it) {
                    doc.reference.update("nickname", user.nickname)
                }
            }
        }
    }

    suspend fun getUserProfile(fileName: String): Task<Uri> {
        return App.storage.reference.child(fileName).downloadUrl
    }

    suspend fun editUserProfile(fileName: String, uri: Uri) {
        val uploadTask = App.storage.reference.child("images/users/${fileName}.jpg").putFile(uri)
    }

    suspend fun getFollowingStatus(uid: String): Boolean {
        val myInfo = getUserInfo(App.auth.currentUser!!.uid)
        Log.i("datasource", myInfo.following.toString())
        Log.i("datasource", uid)
        return myInfo.following.contains(uid)
    }
}