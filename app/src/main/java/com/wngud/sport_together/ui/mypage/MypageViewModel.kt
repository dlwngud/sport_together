package com.wngud.sport_together.ui.mypage

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.tasks.Task
import com.kakao.sdk.user.Constants
import com.kakao.sdk.user.UserApiClient
import com.wngud.sport_together.App
import com.wngud.sport_together.domain.model.User
import com.wngud.sport_together.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class MypageEvent {
    object MoveToLogin : MypageEvent()
}

@HiltViewModel
class MypageViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    private val _user = MutableStateFlow(User())
    val user = _user.asStateFlow()

    private val _eventFlow = MutableSharedFlow<MypageEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        if (App.auth.currentUser != null) {
            viewModelScope.launch {
                getCurrentUser(App.auth.currentUser!!.uid)
            }
        } else {
            Log.i("tag", "로그인 없음")
        }
    }

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

    private fun getCurrentUser(uid: String) = viewModelScope.launch {
        userRepository.getUserInfo(uid).collect { user ->
            _user.update { user }
        }
    }

    suspend fun getUserById(uid: String) = userRepository.getUserInfo(uid)

    suspend fun editUserInfo(editNickname: String, editIntroduce: String, profileImage: String) =
        viewModelScope.launch {
            val editUser = user.value.copy(
                profileImage = profileImage,
                nickname = editNickname,
                introduce = editIntroduce
            )
            userRepository.saveUserInfo(editUser)
            _user.update { editUser }
        }

    fun getUserProfile(fileName: String, callback: (Task<Uri>) -> Unit) = viewModelScope.launch {
        userRepository.getUserProfile(fileName).addOnCompleteListener(callback)
    }

    fun editUserProfile(fileName: String, uri: Uri) = viewModelScope.launch {
        userRepository.editUserProfile(fileName, uri)
    }

    fun follow(uid: String) = viewModelScope.launch {
        userRepository.follow(uid)
    }
}