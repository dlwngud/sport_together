package com.wngud.sport_together.ui.login

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.Constants.TAG
import com.kakao.sdk.user.UserApiClient
import com.wngud.sport_together.domain.model.User
import com.wngud.sport_together.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface LoginUiState {
    object Success : LoginUiState
    data class Error(val errorMessage: String) : LoginUiState
    object Loading : LoginUiState
    object Default: LoginUiState
}

sealed class LoginEvent {
    object MoveToMain : LoginEvent()
    object KakaoLoginFail : LoginEvent()
}

@HiltViewModel
class LoginViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {

    private var auth: FirebaseAuth = Firebase.auth

    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Default)
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    private val _eventFlow = MutableSharedFlow<LoginEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private fun startEvent(event: LoginEvent) = viewModelScope.launch {
        _eventFlow.emit(event)
    }

    private fun signUpFirebase() {
        UserApiClient.instance.me { user, error ->
            if (error != null) {
                Log.e(TAG, "사용자 정보 요청 실패", error)
            } else if (user != null) {
                auth.createUserWithEmailAndPassword(user.kakaoAccount?.email!!, user.id.toString())
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d(TAG, "createUserWithEmail:success")
                            val currentUser = auth.currentUser
                            val userInfo = User(
                                currentUser?.uid!!,
                                currentUser.email!!,
                                user.kakaoAccount?.profile?.nickname!!,
                                "나를 소개해보세요"
                            )
                            viewModelScope.launch {
                                userRepository.saveUserInfo(userInfo)
                            }
                            _uiState.update { LoginUiState.Success }
                            startEvent(LoginEvent.MoveToMain)
                        } else {
                            _uiState.update { LoginUiState.Error("로그인 실패") }
                            Log.w(TAG, "createUserWithEmail:failure", task.exception)
                        }
                    }
            }
        }
    }

    fun signInFirebase() {
        UserApiClient.instance.me { user, error ->
            if (error != null) {
                Log.e(TAG, "사용자 정보 요청 실패", error)
            } else if (user != null) {
                auth.signInWithEmailAndPassword(user.kakaoAccount?.email!!, user.id.toString())
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success")
                            viewModelScope.launch {
                                userRepository.getMyInfo(auth.uid!!)
                            }
                            _uiState.update { LoginUiState.Success }
                            startEvent(LoginEvent.MoveToMain)
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.exception)
                            signUpFirebase()
                        }
                    }
            }
        }
    }

    fun loginKakao(context: Context) {
        _uiState.update { LoginUiState.Loading }
        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                Log.e(TAG, "카카오계정으로 로그인 실패", error)
                _uiState.update { LoginUiState.Default }
                startEvent(LoginEvent.KakaoLoginFail)
            } else if (token != null) {
                Log.i(TAG, "카카오계정으로 로그인 성공 ${token.accessToken}")
                signInFirebase()
            }
        }

        // 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
            UserApiClient.instance.loginWithKakaoTalk(context) { token, error ->
                if (error != null) {
                    Log.e(TAG, "카카오톡으로 로그인 실패", error)
                    startEvent(LoginEvent.KakaoLoginFail)
                    // 사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인을 취소한 경우,
                    // 의도적인 로그인 취소로 보고 카카오계정으로 로그인 시도 없이 로그인 취소로 처리 (예: 뒤로 가기)
                    if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                        _uiState.update { LoginUiState.Default }
                        return@loginWithKakaoTalk
                    }

                    // 카카오톡에 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인 시도
                    UserApiClient.instance.loginWithKakaoAccount(context, callback = callback)
                } else if (token != null) {
                    Log.i(TAG, "카카오톡으로 로그인 성공 ${token.accessToken}")
                    signInFirebase()
                }
            }
        } else {
            UserApiClient.instance.loginWithKakaoAccount(context, callback = callback)
        }
    }
}