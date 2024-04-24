package com.wngud.sport_together.data.repository

import com.wngud.sport_together.data.db.remote.UserDataSource
import com.wngud.sport_together.domain.model.User
import com.wngud.sport_together.domain.repository.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(private val userDataSource: UserDataSource) :
    UserRepository {
    override suspend fun getUserInfo(uid: String): MutableList<User> {
        return userDataSource.getUserInfo(uid)
    }

    override suspend fun saveUserInfo(user: User) {
        userDataSource.saveUserInfo(user)
    }
}