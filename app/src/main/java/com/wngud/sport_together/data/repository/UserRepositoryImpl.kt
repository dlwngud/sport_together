package com.wngud.sport_together.data.repository

import android.net.Uri
import android.util.Log
import com.google.android.gms.tasks.Task
import com.wngud.sport_together.data.db.remote.UserDataSource
import com.wngud.sport_together.domain.model.User
import com.wngud.sport_together.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(private val userDataSource: UserDataSource) :
    UserRepository {
    override suspend fun getUserInfo(uid: String): Flow<User> {
        val userInfo = userDataSource.getUserInfo(uid)
        Log.i("tag", "repo "+userInfo.first().profileImage)
        return userInfo
    }

    override suspend fun saveUserInfo(user: User) {
        userDataSource.saveUserInfo(user)
    }

    override suspend fun getUserProfile(fileName: String): Task<Uri> {
        return userDataSource.getUserProfile(fileName)
    }

    override suspend fun editUserProfile(fileName: String, uri: Uri) {
        userDataSource.editUserProfile(fileName, uri)
    }
}