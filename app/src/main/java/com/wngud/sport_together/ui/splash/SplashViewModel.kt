package com.wngud.sport_together.ui.splash

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kakao.sdk.auth.AuthApiClient
import com.kakao.sdk.common.model.KakaoSdkError
import com.kakao.sdk.user.Constants
import com.kakao.sdk.user.UserApiClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class SplashEvent {
    object MoveToMain : SplashEvent()
    object MoveToOnboard : SplashEvent()
}

@HiltViewModel
class SplashViewModel @Inject constructor() : ViewModel() {
    private val _eventFlow = MutableSharedFlow<SplashEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        hasToken()
    }

    private fun startEvent(event: SplashEvent) = viewModelScope.launch {
        _eventFlow.emit(event)
    }

    private fun hasToken() {
        if (AuthApiClient.instance.hasToken()) {
            UserApiClient.instance.accessTokenInfo { token, error ->
                if (error != null) {
                    if (error is KakaoSdkError && error.isInvalidTokenError() == true) {
                        //로그인 필요
                        startEvent(SplashEvent.MoveToOnboard)
                    } else {
                        //기타 에러
                        startEvent(SplashEvent.MoveToOnboard)
                    }
                } else {
                    //토큰 유효성 체크 성공(필요 시 토큰 갱신됨)
                    startEvent(SplashEvent.MoveToMain)
                    Log.i(Constants.TAG, "만료시간: ${token?.expiresIn}")
                }
            }
        } else {
            //로그인 필요
            startEvent(SplashEvent.MoveToOnboard)
        }
    }
}