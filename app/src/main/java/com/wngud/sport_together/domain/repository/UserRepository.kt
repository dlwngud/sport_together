package com.wngud.sport_together.domain.repository

import com.wngud.sport_together.domain.model.User

interface UserRepository {
    suspend fun getUserInfo(uid: String): MutableList<User>

    suspend fun saveUserInfo(user: User)
}