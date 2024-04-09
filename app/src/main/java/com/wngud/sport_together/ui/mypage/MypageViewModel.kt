package com.wngud.sport_together.ui.mypage

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kakao.sdk.user.Constants
import com.kakao.sdk.user.UserApiClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class MypageEvent {
    object MoveToLogin : MypageEvent()
}

@HiltViewModel
class MypageViewModel @Inject constructor() : ViewModel() {
    private val _eventFlow = MutableSharedFlow<MypageEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private fun startEvent(event: MypageEvent) = viewModelScope.launch {
        _eventFlow.emit(event)
    }

    fun logout() {
        UserApiClient.instance.logout { error ->
            if (error != null) {
                Log.e(Constants.TAG, "로그아웃 실패. SDK에서 토큰 삭제됨", error)
            } else {
                Log.i(Constants.TAG, "로그아웃 성공. SDK에서 토큰 삭제됨")
                startEvent(MypageEvent.MoveToLogin)
            }
        }
    }
}