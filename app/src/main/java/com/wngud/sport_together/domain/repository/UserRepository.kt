package com.wngud.sport_together.domain.repository

import android.net.Uri
import com.google.android.gms.tasks.Task
import com.wngud.sport_together.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun getMyInfo(uid: String): Flow<User>

    suspend fun getUserInfo(uid: String): User

    suspend fun saveUserInfo(user: User)

    suspend fun getUserProfile(fileName: String): Task<Uri>

    suspend fun editUserProfile(fileName: String, uri: Uri)

    suspend fun getFollowingStatus(uid: String): Boolean

    suspend fun unfollowing(uid: String)

    suspend fun following(uid: String)
}