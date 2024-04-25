package com.wngud.sport_together.domain.repository

import android.net.Uri
import com.google.android.gms.tasks.Task
import com.wngud.sport_together.domain.model.User

interface UserRepository {
    suspend fun getUserInfo(uid: String): MutableList<User>

    suspend fun saveUserInfo(user: User)

    suspend fun getUserProfile(fileName: String): Task<Uri>

    suspend fun editUserProfile(fileName: String, uri: Uri)
}