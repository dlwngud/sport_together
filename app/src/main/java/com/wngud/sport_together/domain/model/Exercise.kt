package com.wngud.sport_together.domain.model

data class Exercise(
    var exerciseId: String = "",
    val uid: String = "",
    val type: String = "",
    val location: String = "",
    val title: String = "",
    val nickname: String = "",
    val profileImage: String = ""
)