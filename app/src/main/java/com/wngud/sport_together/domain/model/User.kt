package com.wngud.sport_together.domain.model

data class User(
    val uid: String = "",
    val email: String = "",
    val nickname: String = "",
    val introduce: String = "",
    val profileImage: String = "",
    val follower: List<String> = emptyList(),
    val following: List<String> = emptyList()
) {
    companion object {
        val Default = User(
            "",
            "",
            "",
            "나를 소개해보세요",
        )
    }
}