package com.wngud.sport_together.domain.model

data class Review(
    val reviewId: String = "",
    val uid: String = "",
    val profileImage: String = "",
    val nickname: String = "",
    val time: String = "",
    val images: List<String> = emptyList(),
    val content: String = "",
    val like: Int = 0,
    val comment: List<String> = emptyList(),
    val createTime: Long = 0L
)
