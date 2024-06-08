package com.wngud.sport_together.data.repository

import android.net.Uri
import com.google.android.gms.tasks.Task
import com.wngud.sport_together.data.db.remote.UserDataSource
import com.wngud.sport_together.domain.model.User
import com.wngud.sport_together.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(private val userDataSource: UserDataSource) :
    UserRepository {
    override suspend fun getMyInfo(uid: String): Flow<User> {
        return userDataSource.getMyInfo(uid)
    }

    override suspend fun getUserInfo(uid: String): User {
        return userDataSource.getUserInfo(uid)
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

    override suspend fun getFollowingStatus(uid: String): Boolean {
        return userDataSource.getFollowingStatus(uid)
    }
}