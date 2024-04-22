package com.wngud.sport_together

import android.app.Application
import com.google.firebase.firestore.FirebaseFirestore
import com.kakao.sdk.common.KakaoSdk
import com.naver.maps.map.NaverMapSdk
import com.wngud.sport_together.domain.model.User
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App: Application() {

    companion object {
        lateinit var db: FirebaseFirestore
        lateinit var currentUser: User
    }
    override fun onCreate() {
        super.onCreate()
        NaverMapSdk.getInstance(this).client =
            NaverMapSdk.NaverCloudPlatformClient(BuildConfig.NAVER_CLINET_ID)
        KakaoSdk.init(this, BuildConfig.KAKAO_API_KEY)
        db = FirebaseFirestore.getInstance()
    }
}