package com.wngud.sport_together.domain.repository

import com.wngud.sport_together.domain.model.User

interface UserRepository {
    suspend fun getUserInfo(uid: String): User
}