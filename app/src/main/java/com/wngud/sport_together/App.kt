package com.wngud.sport_together

import android.app.Application
import com.naver.maps.map.NaverMapSdk

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        NaverMapSdk.getInstance(this).client =
            NaverMapSdk.NaverCloudPlatformClient("mktto2sczm")
    }
}