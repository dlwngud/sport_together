package com.wngud.sport_together.ui.chatting

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wngud.sport_together.domain.model.Chatting
import com.wngud.sport_together.domain.repository.ChattingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChattingViewModel @Inject constructor(
    private val chattingRepository: ChattingRepository
) : ViewModel() {

    private val _chattingList = MutableStateFlow<List<Chatting>>(emptyList())
    val chattingList = _chattingList.asStateFlow()

//    private val _roomId = MutableStateFlow<String>("")
//    val roomId = _roomId.asStateFlow()
//
//    fun selectRoom(roomId: String){
//        _roomId.update { roomId }
//    }

    fun sendChatting(chatting: Chatting, users: List<String>) = viewModelScope.launch {
        chattingRepository.sendChatting(chatting, users)
    }

    fun getChatting(roomId: String) = viewModelScope.launch {
        chattingRepository.getChatting(roomId).collect { chattings ->
            Log.i("viewmodel", "채팅 사이즈 ${chattings.size}")
            _chattingList.update { chattings }
        }
    }
}